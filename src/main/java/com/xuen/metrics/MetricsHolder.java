package com.xuen.metrics;

import com.codahale.metrics.Metric;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import java.lang.management.CompilationMXBean;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

/**
 * 将 metrics 的存储和用户静态 api 拆分出来
 * 这个类负责存储 metrics 数据
 *
 * @author zhenwei.liu
 * @since 2016-11-23
 */
public class MetricsHolder {

    final Cache<MetricKey, Metric> metrics = CacheBuilder.newBuilder().build();

    final List<Delta> deltas = Lists.newCopyOnWriteArrayList();
    final List<ResettableTimer> timers = Lists.newCopyOnWriteArrayList();

    private long lastUpdate = 0;

    private Calendar calendar = Calendar.getInstance();

    public MetricsHolder(boolean isJavaClient) {
        if (isJavaClient) {
            initJVM();
            initTomcat();
        }
        initScheduler();
    }

    <T extends Metric> T register(final MetricKey key, final T metric) throws IllegalArgumentException {
        try {
            return (T) metrics.get(key, () -> {
                if (Delta.class.isInstance(metric)) {
                    deltas.add((Delta) metric);
                }
                if (ResettableTimer.class.isInstance(metric)) {
                    timers.add((ResettableTimer) metric);
                }
                return metric;
            });
        } catch (ExecutionException e) {
            throw new IllegalArgumentException("fail to register metric,metric key:" + key);
        }

    }

    @SuppressWarnings("unchecked")
    protected <T extends Metric> T getOrAdd(MetricKey key, MetricsOps.MetricBuilder<T> builder) {
        final Metric metric = metrics.getIfPresent(key);

        if (builder.isInstance(metric)) {
            return (T) metric;
        } else if (metric == null) {
            try {
                boolean delta = false;
                boolean keep = false;
                if (DeltaKeyWrapper.class.isInstance(key)) {
                    DeltaKeyWrapper<T> _key = (DeltaKeyWrapper<T>) key;
                    delta = _key.delta;
                    keep = _key.keep;
                }
                return register(key, builder.newMetric(delta, keep));
            } catch (IllegalArgumentException e) {// 被别人并发抢注了
                final Metric added = metrics.getIfPresent(key);// 这个地方是一定有值的，因为只有注册的方法，并没有移除的方法,上面出异常证明已经注册过了.
                if (builder.isInstance(added)) {
                    return (T) added;
                }
            }
        }

        throw new IllegalArgumentException(key + " is already used for a different type of metric");
    }

    /**
     * 定时 tick 任务, 用于计算 delta 和 timer 每分钟内的 snapshot
     */
    private void initScheduler() {
        Executors.newScheduledThreadPool(10).scheduleAtFixedRate(() -> {
            String name = Thread.currentThread().getName();
            try {
                Thread.currentThread().setName("tc-metrics");
                long current = System.currentTimeMillis();
                if (current - lastUpdate < 50000L) {
                    return;
                }
                calendar.setTimeInMillis(current);
                if (calendar.get(Calendar.SECOND) > 10) {
                    return;
                }
                lastUpdate = current;

                for (Delta delta : deltas) {
                    delta.tick();
                }

                for (ResettableTimer timer : timers) {
                    timer.refreshTimeSnapshot();
                }
            } finally {
                Thread.currentThread().setName(name);
            }
        }, 0, 2, TimeUnit.SECONDS);
    }

    private void initJVM() {
        final Pattern whitespace = Pattern.compile("\\s+");

        // thread
        register(new MetricKey("JVM_Thread_Count"), (com.codahale.metrics.Gauge<Double>) () -> (double) ManagementFactory.getThreadMXBean().getThreadCount());

        for (final GarbageCollectorMXBean bean : ManagementFactory.getGarbageCollectorMXBeans()) {
            DeltaGauge count = new DeltaGauge(bean::getCollectionCount, false);
            DeltaGauge time = new DeltaGauge(bean::getCollectionTime, false); // gc 时间即使没有变化也不需要保持前面的值

            String name = "JVM_" + bean.getName();
            register(new MetricKey(whitespace.matcher(name + "_Count").replaceAll("_")), count);
            register(new MetricKey(whitespace.matcher(name + "_Time").replaceAll("_")), time);
        }

        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        addMemoryUsageBytes(memoryMXBean);

        final CompilationMXBean compilationMXBean = ManagementFactory.getCompilationMXBean();
        DeltaGauge jitCompileTimeGauge = new DeltaGauge(new Gauge() {
            @Override
            public double getValue() {
                return compilationMXBean.getTotalCompilationTime();
            }
        }, false);
        register(new MetricKey("JVM_JIT_Compilation_Time"), jitCompileTimeGauge);

    }

    private void addMemoryUsageBytes(final MemoryMXBean memoryMXBean) {
        register(new MetricKey("JVM_Heap_Memory_Usage_MBytes"), new com.codahale.metrics.Gauge<Double>() {
            @Override
            public Double getValue() {
                return (double) memoryMXBean.getHeapMemoryUsage().getUsed() / 1024 / 1024;
            }
        });
    }

    private void initTomcat() {
        if (isNotTomcat()) return;
        try {
            final MBeanServer server = ManagementFactory.getPlatformMBeanServer();
            initThreadPool(server);
            initExecutor(server);
        } catch (Exception ignore) {
        }
    }

    private boolean isNotTomcat() {
        return System.getProperty("catalina.home") == null;
    }

    private void initExecutor(final MBeanServer server) throws MalformedObjectNameException {
        Set<ObjectName> names = server.queryNames(new ObjectName("Catalina:type=Executor,*"), null);
        for (final ObjectName name : names) {
            String executorName = name.getKeyProperty("name");

            register(new MetricKey(DefaultMetrics.TOMCAT_MAX_THREADS.name()).tag("name", executorName), new com.codahale.metrics.Gauge<Double>() {
                @Override
                public Double getValue() {
                    return getAttribute(server, name, "maxThreads");
                }
            });
            register(new MetricKey(DefaultMetrics.TOMCAT_CURRENT_THREADS.name()).tag("name", executorName), new com.codahale.metrics.Gauge<Double>() {
                @Override
                public Double getValue() {
                    return getAttribute(server, name, "poolSize");
                }
            });

            register(new MetricKey(DefaultMetrics.TOMCAT_ACTIVE_THREADS.name()), new com.codahale.metrics.Gauge<Double>() {
                @Override
                public Double getValue() {
                    return getAttribute(server, name, "activeCount");
                }
            });
        }
    }

    private void initThreadPool(final MBeanServer server) throws MalformedObjectNameException {
        Set<ObjectName> names = server.queryNames(new ObjectName("Catalina:type=ThreadPool,*"), null);
        for (final ObjectName name : names) {
            String executorName = name.getKeyProperty("name");

            register(new MetricKey(DefaultMetrics.TOMCAT_MAX_THREADS.name()).tag("name", executorName), new com.codahale.metrics.Gauge<Double>() {
                @Override
                public Double getValue() {
                    return getAttribute(server, name, "maxThreads");
                }
            });
            register(new MetricKey(DefaultMetrics.TOMCAT_CURRENT_THREADS.name()).tag("name", executorName), new com.codahale.metrics.Gauge<Double>() {
                @Override
                public Double getValue() {
                    return getAttribute(server, name, "currentThreadCount");
                }
            });

            register(new MetricKey(DefaultMetrics.TOMCAT_ACTIVE_THREADS.name()).tag("name", executorName), new com.codahale.metrics.Gauge<Double>() {
                @Override
                public Double getValue() {
                    return getAttribute(server, name, "currentThreadsBusy");
                }
            });
        }
    }

    private double getAttribute(MBeanServer server, ObjectName name, String attr) {
        try {
            return Double.valueOf(server.getAttribute(name, attr).toString());
        } catch (Exception e) {
            return 0D;
        }
    }

    public Map<MetricKey, Metric> getMetrics() {
        return metrics.asMap();
    }

    public List<MetricsItem> toMetricsItemList(String app) {

        long timestamp = System.currentTimeMillis();
        List<MetricsItem> metricItemList = Lists.newArrayList();

        for (Map.Entry<MetricKey, Metric> entry : metrics.asMap().entrySet()) {
            MetricKey key = entry.getKey();
            Metric metric = entry.getValue();

            MetricsItem item = new MetricsItem();
            item.setApp(app);
            item.setName(key.getName());
            item.setTags(key.tagMap());
            item.setHost(key.getHost());
            item.setAlias(key.getAlias());
            MetricType metricType = Metrics.typeOf(metric);
            item.setMetricValue(ItemValue.valueOf(metricType, metric));
            item.setType(metricType);
            item.setTs(timestamp);

            metricItemList.add(item);
        }

        return metricItemList;
    }
}

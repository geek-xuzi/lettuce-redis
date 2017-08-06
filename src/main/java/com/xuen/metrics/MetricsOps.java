package com.xuen.metrics;


import com.codahale.metrics.Metric;
import com.xuen.metrics.adapter.CounterAdapter;
import com.xuen.metrics.adapter.MeterAdapter;

/**
 * 使用<a href="http://metrics.codahale.com/">Metrics</a>的监控客户端
 *
 * @author zhongyuan.zhang
 */
public class MetricsOps {

    public final MetricsHolder holder;

    public MetricsOps(boolean isJavaClient) {
        holder = new MetricsHolder(isJavaClient);
    }

    /**
     * 记录一个瞬间单值.
     * <p/>
     * 比如当前线程数
     *
     * @param name 指标名
     * @return
     */
    public GaugeKey gauge(final String name) {
        return new GaugeKey(name);
    }

    /**
     * 计数器.
     * <p/>
     * 可以增加和减少数值
     *
     * @param name 指标名
     * @return
     */
    public DeltaKeyWrapper<Counter> counter(final String name) {
        return new DeltaKeyWrapper<Counter>(name) {
            @Override
            public Counter get() {
                return new CounterAdapter(holder.getOrAdd(this, MetricBuilder.COUNTERS));
            }
        };
    }

    /**
     * 用于测量特定时间段内某个事件的频率
     *
     * @param name 指标名 }
     * @return
     */
    public KeyWrapper<Meter> meter(final String name) {
        return new KeyWrapper<Meter>(name) {
            @Override
            public Meter get() {
                return new MeterAdapter(holder.getOrAdd(this, MetricBuilder.METERS));
            }
        };
    }

    /**
     * 记录meter同时记录时间
     *
     * @param name
     * @return
     */
    public KeyWrapper<Timer> timer(final String name) {
        return new DeltaKeyWrapper<Timer>(name) {

            @Override
            public Timer get() {
                return new ResettableTimerAdapter(holder.getOrAdd(this, MetricBuilder.TIMERS));
            }
        }.delta();
    }

    protected interface MetricBuilder<T extends Metric> {

        MetricBuilder<com.codahale.metrics.Counter> COUNTERS = new MetricBuilder<com.codahale.metrics.Counter>() {
            @Override
            public com.codahale.metrics.Counter newMetric(boolean delta, boolean keep) {
                return delta ? new DeltaCounter(keep) : new com.codahale.metrics.Counter();
            }

            @Override
            public boolean isInstance(Metric metric) {
                return com.codahale.metrics.Counter.class.isInstance(metric);
            }
        };

        MetricBuilder<com.codahale.metrics.Meter> METERS = new MetricBuilder<com.codahale.metrics.Meter>() {
            @Override
            public com.codahale.metrics.Meter newMetric(boolean delta, boolean keep) {
                return new com.codahale.metrics.Meter();
            }

            @Override
            public boolean isInstance(Metric metric) {
                return com.codahale.metrics.Meter.class.isInstance(metric);
            }
        };

        MetricBuilder<ResettableTimer> TIMERS = new MetricBuilder<ResettableTimer>() {
            @Override
            public ResettableTimer newMetric(boolean delta, boolean keep) {
                return new ResettableTimer();
            }

            @Override
            public boolean isInstance(Metric metric) {
                return ResettableTimer.class.isInstance(metric);
            }
        };

        T newMetric(boolean delta, boolean keep);

        boolean isInstance(Metric metric);
    }

    public MetricsHolder getHolder() {
        return holder;
    }
}

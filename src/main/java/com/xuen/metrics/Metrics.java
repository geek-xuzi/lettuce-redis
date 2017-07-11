package com.xuen.metrics;

import com.codahale.metrics.Metric;

/**
 * 使用<a href="http://metrics.codahale.com/">Metrics</a>的监控客户端
 *
 * @author zhongyuan.zhang
 */
public class Metrics {

    public static final MetricsOps INSTANCE = new MetricsOps(true);

    /**
     * 记录一个瞬间单值.
     * <p/>
     * 比如当前线程数
     *
     * @param name 指标名
     * @return
     */
    public static GaugeKey gauge(final String name) {
        return INSTANCE.gauge(name);
    }

    /**
     * 计数器.
     * <p/>
     * 可以增加和减少数值
     *
     * @param name 指标名
     * @return
     */
    public static DeltaKeyWrapper<Counter> counter(final String name) {
        return INSTANCE.counter(name);
    }

    /**
     * 用于测量特定时间段内某个事件的频率
     *
     * @param name 指标名 }
     * @return
     */
    public static KeyWrapper<Meter> meter(final String name) {
        return INSTANCE.meter(name);
    }

    /**
     * 记录meter同时记录时间
     *
     * @param name
     * @return
     */
    public static KeyWrapper<Timer> timer(final String name) {
        return INSTANCE.timer(name);
    }

    public static MetricType typeOf(Metric metric) {
        if (metric instanceof com.codahale.metrics.Gauge) {
            return MetricType.GAUGE;
        }
        if (metric instanceof com.codahale.metrics.Counter) {
            return MetricType.COUNTER;
        }
        if (metric instanceof com.codahale.metrics.Meter) {
            return MetricType.METER;
        }
        if (metric instanceof com.codahale.metrics.Timer) {
            return MetricType.TIMER;
        }
        if (metric instanceof ResettableTimer) {
            return MetricType.TIMER;
        }
        return null;
    }

}

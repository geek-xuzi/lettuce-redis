package com.xuen.metrics;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;

/**
 * @author zheng.xu
 * @since 2017-06-09
 */
public class Metric {

    private static MetricRegistry metricRegistry;

    static {
        metricRegistry = new MetricHolder().getMetricsRegistry();
    }

    public static Counter Count(String name) {

        return metricRegistry.counter(name);
    }

    public static Histogram histogram(String name) {
        return metricRegistry.histogram(name);
    }

    public static Timer timer(String name) {
        return metricRegistry.timer(name);
    }

}

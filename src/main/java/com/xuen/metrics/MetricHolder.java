package com.xuen.metrics;

import com.codahale.metrics.MetricRegistry;

/**
 * @author zheng.xu
 * @since 2017-06-09
 */
public class MetricHolder {


    private MetricRegistry metricsRegistry;

    public MetricRegistry getMetricsRegistry() {
        if (metricsRegistry == null) {
            metricsRegistry = new MetricRegistry();
        }
        return this.metricsRegistry;
    }
}

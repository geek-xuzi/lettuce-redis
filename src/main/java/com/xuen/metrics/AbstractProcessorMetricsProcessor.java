package com.xuen.metrics;

import com.codahale.metrics.Metric;
import org.apache.commons.lang3.StringUtils;

import java.io.PrintWriter;
import java.util.Map;

/**
 * @acthor xuzheng
 * @create 2017-07-26
 **/
public abstract class AbstractProcessorMetricsProcessor implements MetricsProcessor {

    private MetricProcessor processor;

    public AbstractProcessorMetricsProcessor(MetricProcessor processor) {
        this.processor = processor;
    }

    protected void prepare(MetricsHolder metricsHolder, PrintWriter writer, String name) {

    }

    protected void finish(MetricsHolder metricsHolder, PrintWriter writer, String name) {
        writer.flush();
    }

    protected void doReport(MetricsHolder metricsHolder, PrintWriter writer, String name) {
        // 默认打开所有指标
        // $name|type|tag|value
        for (Map.Entry<MetricKey, Metric> e : metricsHolder.metrics.asMap().entrySet()) {
            MetricKey key = e.getKey();
            Metric value = e.getValue();
            if (!StringUtils.isEmpty(name) && !name.equals(key.name)) {
                continue;
            }
            processor.process(writer, key, value);
        }
    }

    @Override
    public void report(MetricsHolder metricsHolder, PrintWriter writer, String name) {
        prepare(metricsHolder, writer, name);
        doReport(metricsHolder, writer, name);
        finish(metricsHolder, writer, name);
    }

    interface MetricProcessor {
        void process(PrintWriter writer, MetricKey key, Metric value);
    }
}
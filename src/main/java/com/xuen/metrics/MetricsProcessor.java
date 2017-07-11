package com.xuen.metrics;

import com.codahale.metrics.Metric;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 * @author zheng.xu
 * @since 2017-07-11
 */
public interface MetricsProcessor {

    void report(MetricsHolder metricsHolder, PrintWriter writer, String name);
}

abstract class AbstractProcessorMetricsProcessor implements MetricsProcessor {

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

/**
 * Cerberus实现。
 *
 * @since 16 September 2015
 */
class CerberusPullProcessorMetricsProcessor extends AbstractProcessorMetricsProcessor {

    public CerberusPullProcessorMetricsProcessor() {
        super(DEFAULT_PROCESSOR);
    }

    @Override
    protected void prepare(MetricsHolder metricsHolder, PrintWriter writer, String name) {
        final long timestamp = System.currentTimeMillis();

        // default format
        writer.println(timestamp);
        writer.println();
    }

    static final MetricProcessor DEFAULT_PROCESSOR = (writer, key, value) -> {

        MetricType type = Metrics.typeOf(value);
        // {name}|
        StringBuilder line = new StringBuilder().append(key.name).append('|');
        // {type}|
        line.append(type.code()).append('|');
        // {tag}|
        Iterator<Tag> iter = key.tags.getTags().iterator();
        if (iter.hasNext()) {
            // first
            Tag tag = iter.next();
            line.append(tag.key).append('=').append(tag.value);
        }
        while (iter.hasNext()) {
            Tag tag = iter.next();
            line.append(',').append(tag.key).append('=').append(tag.value);
        }
        line.append('|');

        // {value}
        float[] data = ItemValue.valueOf(type, value);
        int idx = 0;
        line.append(data[idx++]);
        while (idx < data.length) {
            line.append(',').append(data[idx++]);
        }

        // {name}|{type}|[key=value]|[data]
        writer.println(line);
    };
}
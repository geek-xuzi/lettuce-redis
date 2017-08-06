package com.xuen.metrics;

/**
 * @acthor xuzheng
 * @create 2017-07-26
 **/

import java.io.PrintWriter;
import java.util.Iterator;

/**
 * Cerberus实现。
 *
 * @since 16 September 2015
 */
public class CerberusPullProcessorMetricsProcessor extends AbstractProcessorMetricsProcessor {

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
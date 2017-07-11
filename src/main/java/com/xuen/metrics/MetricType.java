package com.xuen.metrics;


import static com.xuen.metrics.ValueType.*;



import com.google.common.collect.Maps;
import java.util.Map;

/**
 * 指标类别
 *
 */
public enum MetricType {

    GAUGE(0, count), //
    COUNTER(1, count), //
    METER(2, mean_rate, m1_rate, m5_rate, m15_rate), //
    TIMER(3, mean_rate, m1_rate, m5_rate, mean, std, p75, p98, count), //
    COUNTER_DELTA(4, count); //

    /** 该类型对应的值存储序列 */
    private final Map<ValueType, Integer> sequence; // valueType -> index
    private int code;

    MetricType(int code, ValueType... valueTypes) {
        this.code = code;
        this.sequence = Maps.newHashMap();
        for (int i = 0; i < valueTypes.length; i++) {
            sequence.put(valueTypes[i], i);
        }
    }

    public Map<ValueType, Integer> sequence() {
        return sequence;
    }

    public int code() {
        return code;
    }

    public static MetricType codeOf(int code) {
        for (MetricType metricType : values()) {
            if (metricType.code() == code) {
                return metricType;
            }
        }
        throw new IllegalArgumentException("unrecognized code " + code);
    }

    public int indexOf(ValueType valueType) {
        Integer index = sequence.get(valueType);
        return index == null ? -1 : index;
    }

    public boolean contains(ValueType type) {
        return indexOf(type) >= 0;
    }
}

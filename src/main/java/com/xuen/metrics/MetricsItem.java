package com.xuen.metrics;

import com.google.common.base.Strings;

import java.util.Map;

/**
 * @author zhenwei.liu
 * @since 2016-09-22
 */
public class MetricsItem {

    private String app;
    private String name;
    private String host;
    private String alias;
    private MetricType type;
    private Map<String, String> tags;
    private float[] metricValue;
    private long ts;

    public String getName() {
        return name;
    }

    public MetricsItem setName(String name) {
        this.name = name;
        return this;
    }

    public MetricType getType() {
        return type;
    }

    public MetricsItem setType(MetricType type) {
        this.type = type;
        return this;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public MetricsItem setTags(Map<String, String> tags) {
        this.tags = tags;
        return this;
    }

    public float[] getMetricValue() {
        return metricValue;
    }

    public MetricsItem setMetricValue(float[] metricValue) {
        this.metricValue = metricValue;
        return this;
    }

    public long getTs() {
        return ts;
    }

    public MetricsItem setTs(long ts) {
        this.ts = ts;
        return this;
    }

    public String getApp() {
        return app;
    }

    public MetricsItem setApp(String app) {
        this.app = app;
        return this;
    }

    public String getHost() {
        return host;
    }

    public MetricsItem setHost(String host) {
        this.host = host;
        return this;
    }

    public String getAlias() {
        return alias;
    }

    public MetricsItem setAlias(String alias) {
        this.alias = alias;
        return this;
    }

    public String getPanelName() {
        return Strings.isNullOrEmpty(alias) ? name : alias;
    }
}

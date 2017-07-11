package com.xuen.metrics;

/**
 * @author zheng.xu
 * @since 2017-07-11
 */
public class GaugeKey extends MetricKey{

    protected boolean delta = false;
    protected boolean keep = false;

    public GaugeKey(String name) {
        super(name);
    }

    @Override
    public GaugeKey tag(String key, String value) {
        return (GaugeKey) super.tag(key, value);
    }

    public GaugeKey delta(){
        this.delta = true;
        return this;
    }

    public GaugeKey keep() {
        this.keep = true;
        return this;
    }
}

package com.xuen.metrics;

import com.google.common.util.concurrent.AtomicDouble;

class DeltaGauge implements com.codahale.metrics.Gauge<Double>, Delta {

    // last, value
    private AtomicDouble[] value = new AtomicDouble[2];
    // data
    private final Gauge gauge;
    // keep value if delta=0?
    private final boolean keep;

    public DeltaGauge(Gauge gauge, boolean keep) {
        this.gauge = gauge;
        this.keep = keep;
        value[0] = new AtomicDouble();
        value[1] = new AtomicDouble();
    }

    public void tick() {
        double cur_cnt = (double) gauge.getValue();
        double last_cnt = value[0].get();

        double delta = cur_cnt - last_cnt;
        if (keep && delta == 0) {
            return;
        }
        value[0].set(cur_cnt);
        value[1].set(delta);
    }

    public Double getValue() {
        return value[1].get();
    }
}

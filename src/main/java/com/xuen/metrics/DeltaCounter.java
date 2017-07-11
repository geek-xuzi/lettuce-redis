package com.xuen.metrics;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author zheng.xu
 * @since 2017-07-11
 */
public class DeltaCounter extends com.codahale.metrics.Counter implements Delta {

    private AtomicLong[] value = new AtomicLong[2];

    private final boolean keep;

    public DeltaCounter(boolean keep) {
        this.keep = keep;
        value[0] = new AtomicLong();
        value[1] = new AtomicLong();
    }

    @Override
    public void tick() {

        long cur_cnt = super.getCount();
        long last_cnt = value[0].get();

        long delta = cur_cnt - last_cnt;
        if (keep && delta == 0){
            return;
        }

        value[0].set(cur_cnt);
        value[1].set(delta);

    }

    @Override
    public long getCount() {
        return value[1].get();
    }
}

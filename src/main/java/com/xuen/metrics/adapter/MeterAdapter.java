package com.xuen.metrics.adapter;

import com.xuen.metrics.Meter;

/**
 * @author zheng.xu
 * @since 2017-07-11
 */
public class MeterAdapter implements Meter {

    private final com.codahale.metrics.Meter _meter;

    public MeterAdapter(com.codahale.metrics.Meter meter) {
        this._meter = meter;
    }

    @Override
    public void mark() {
        mark(1);
    }

    @Override
    public void mark(long n) {
        _meter.mark(n);
    }

    @Override
    public long getCount() {
        return  _meter.getCount();
    }
}

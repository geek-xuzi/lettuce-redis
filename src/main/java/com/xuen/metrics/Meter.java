package com.xuen.metrics;

/**
 * @author zheng.xu
 * @since 2017-07-11
 */
public interface Meter {

    void mark();

    void mark(long n);

    long getCount();
}

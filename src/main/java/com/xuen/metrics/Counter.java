package com.xuen.metrics;

/**
 * @author zheng.xu
 * @since 2017-07-11
 */
public interface Counter {

    void inc();

    void inc(long n);

    void dec();

    void dec(long n);

    long getCount();

}

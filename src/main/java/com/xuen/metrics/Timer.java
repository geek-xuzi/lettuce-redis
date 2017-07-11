package com.xuen.metrics;

import java.util.concurrent.TimeUnit;

/**
 * @author zheng.xu
 * @since 2017-07-11
 */
public interface Timer {

    interface Context {

        long stop();
    }

    void update(long duration, TimeUnit unit);

    Context time();

}

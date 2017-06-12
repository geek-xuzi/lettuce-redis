package com.xuen.metrics;

import com.codahale.metrics.Counter;

/**
 * @author zheng.xu
 * @since 2017-06-09
 */
public class DefaCounter extends Counter {

    public DefaCounter tag(String key, String value) {
        return this;
    }
}

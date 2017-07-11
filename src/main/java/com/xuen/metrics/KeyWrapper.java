package com.xuen.metrics;

/**
 * @author zheng.xu
 * @since 2017-07-11
 */
public abstract class KeyWrapper<T> extends MetricKey {

    public KeyWrapper(String name) {
        super(name);
    }


    public KeyWrapper<T> tag(String key, String value){
        return (KeyWrapper<T>) super.tag(key, value);
    }

    public abstract T get();
}

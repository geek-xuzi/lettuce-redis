package com.xuen.metrics;

/**
 * @author zheng.xu
 * @since 2017-07-11
 */
public abstract class DeltaKeyWrapper<T> extends KeyWrapper<T> {

    protected boolean delta = false;
    protected boolean keep = false;

    public DeltaKeyWrapper(String name) {
        super(name);
    }

    /**
     * 只记录变化量
     */
    public DeltaKeyWrapper<T> tag(String key, String value) {
        return (DeltaKeyWrapper<T>) super.tag(key, value);
    }

    public DeltaKeyWrapper<T> delta() {
        delta = true;
        return this;
    }

    /**
     * 当两次检查数据一致时，维持变化量
     */
    public DeltaKeyWrapper<T> keep() {
        keep = true;
        return this;
    }
}

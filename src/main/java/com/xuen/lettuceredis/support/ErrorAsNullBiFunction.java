package com.xuen.lettuceredis.support;

import java.util.function.BiFunction;

/**
 * @author zheng.xu
 * @since 2017-05-15
 */
public class ErrorAsNullBiFunction<Input> implements BiFunction<Input, Throwable, Input> {

    @Override
    public Input apply(Input input, Throwable throwable) {
        if (throwable != null) {
            return null;
        }
        return input;
    }
}

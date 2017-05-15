package com.xuen.lettuceredis.support;

import java.util.List;
import java.util.function.Function;

/**
 * @author zheng.xu
 * @since 2017-05-15
 */
public interface AggregationFunction<Input, Output> extends Function<List<Input>, Output> {

}

package com.xuen.metrics;

import com.google.common.collect.ImmutableSet;
import java.util.Set;

/**
 * @author zheng.xu
 * @since 2017-07-11
 */
public interface PrometheusConstants {

    /**
     * prometheus 保留字, 不可用于 tag name
     */
    String JOB = "job";
    String INSTANCE = "instance";
    String VAL_TYPE = "vt";
    String HOST = "host";
    String ENV = "env";

    Set<String> RESERVED_SET = ImmutableSet.of(JOB, INSTANCE, VAL_TYPE, HOST, ENV);

    static boolean isReserved(String name) {
        return RESERVED_SET.contains(name);
    }
}

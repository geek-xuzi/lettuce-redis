package com.xuen.metrics;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * 默认监控指标
 * Created by yujie.li on 14-12-26.
 */
public enum DefaultMetrics {
    defaultTopMonitor("default_top_monitor"),

    //http request RT
    httpRequestTimer("http-process-time"),
    //http request exception
    httpRequestError("http-process-error"),

    //dubbo RT
    dubboProviderTimer("dubbo-provider-timer"),
    dubboConsumerTimer("dubbo-consumer-timer"),

    //并发数
    dubboProviderConcurrency("dubbo-provider-concurrency"),
    dubboConsumerConcurrency("dubbo-consumer-concurrency"),

    //dubbo请求超时
    dubboProviderTimeoutException("dubbo-provider-timeout-exception"),
    dubboConsumerTimeoutException("dubbo-consumer-timeout-exception"),

    //dubbo处理业务异常
    dubboProviderBizException("dubbo-provider-biz-exception"),
    dubboConsumerBizException("dubbo-consumer-biz-exception"),

    //dubbo处理网络等其他异常
    dubboProviderProcessError("dubbo-provider-process-error"),
    dubboConsumerProcessError("dubbo-consumer-process-error"),

    access_control_rejects("access_control_rejects"),

    JVM_PS_Scavenge_Time("JVM_PS_Scavenge_Time"),
    JVM_PS_MarkSweep_Time("JVM_PS_MarkSweep_Time"),
    JVM_PS_Scavenge_Count("JVM_PS_Scavenge_Count"),
    JVM_Thread_Count("JVM_Thread_Count"),
    JVM_PS_MarkSweep_Count("JVM_PS_MarkSweep_Count"),
    JVM_Heap_Memory_Usage_MBytes("JVM_Heap_Memory_Usage_MBytes"),
    JVM_JIT_Compilation_Time("JVM_JIT_Compilation_Time"),

    TOMCAT_MAX_THREADS("Tomcat_MaxThreads"),
    TOMCAT_CURRENT_THREADS("Tomcat_CurrentThreads"),
    TOMCAT_ACTIVE_THREADS("Tomcat_ActiveThreads");

    final private String metricName;

    DefaultMetrics(String metricName) {
        this.metricName = metricName;
    }

    public String metricName() {
        return this.metricName;
    }

    private final static Set<String> defaultMetricSet = Sets.newHashSet();

    static {
        for (DefaultMetrics metrics : DefaultMetrics.values()) {
            defaultMetricSet.add(metrics.metricName);
        }
    }

    public static boolean contains(String metricName) {
        return defaultMetricSet.contains(metricName);
    }
}

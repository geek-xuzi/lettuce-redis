package com.xuen.lettuceredis;

import com.codahale.metrics.Metric;
import com.lambdaworks.redis.api.sync.RedisCommands;
import com.xuen.lock.DistributedLock;
import com.xuen.metrics.MetricKey;
import com.xuen.metrics.Metrics;
import com.xuen.metrics.MetricsHandler;
import com.xuen.metrics.MetricsHolder;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author zheng.xu
 * @since 2017-06-16
 */
public class LockTest {

    public static void main(String[] args) {
        Metrics.timer("test1").tag("a","b").get().update(200, TimeUnit.MILLISECONDS);
        Metrics.timer("test1").tag("c","b").get().update(200, TimeUnit.MILLISECONDS);
        Metrics.timer("test2").tag("a","b").get().update(200, TimeUnit.MILLISECONDS);
        Metrics.counter("count1").tag("a","b").get().inc();
        Map<MetricKey, Metric> metrics = Metrics.INSTANCE.getHolder().getMetrics();
        metrics.entrySet().forEach(item -> System.out.println(item.getKey() + ":" + item.getValue()));
    }
}

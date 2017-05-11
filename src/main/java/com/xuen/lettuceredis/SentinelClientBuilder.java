package com.xuen.lettuceredis;

import com.google.common.collect.Lists;
import com.lambdaworks.redis.RedisURI;
import com.lambdaworks.redis.RedisURI.Builder;
import com.xuen.lettuceredis.bean.Server;
import java.util.List;

/**
 * @author zheng.xu
 * @since 2017-05-11
 */
public class SentinelClientBuilder extends RedisClientBuilder {

    private String masterId; // sentinel master id
    private List<Server> sentinelServers = Lists.newArrayList();

    public SentinelClientBuilder(String masterId, List<Server> sentinelServers) {
        setMasterId(masterId);
        setSentinelServers(sentinelServers);
    }

    protected Builder redisURIBuilder() {
        final Builder builder = RedisURI.builder().withSentinelMasterId(masterId);
        sentinelServers.forEach(server -> builder.withSentinel(server.getHost(), server.getPort()));
        return builder;
    }

    public SentinelClientBuilder setMasterId(String masterId) {
        this.masterId = masterId;
        return this;
    }

    public SentinelClientBuilder setSentinelServers(List<Server> sentinelServers) {
        this.sentinelServers = sentinelServers;
        return this;
    }
}

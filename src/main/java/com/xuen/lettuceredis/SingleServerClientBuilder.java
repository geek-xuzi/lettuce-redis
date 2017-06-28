package com.xuen.lettuceredis;

import com.lambdaworks.redis.RedisURI;
import com.lambdaworks.redis.RedisURI.Builder;
import com.lambdaworks.redis.api.sync.RedisCommands;
import com.xuen.lock.DistributedLock;

/**
 * @author zheng.xu
 * @since 2017-05-10
 */
public class SingleServerClientBuilder extends RedisClientBuilder {

    private String host;
    private int prot;

    public SingleServerClientBuilder(String host, int prot) {
        this.host = host;
        this.prot = prot;
    }

    protected Builder redisURIBuilder() {
        return RedisURI.builder().withHost(host).withPort(prot);
    }



    public SingleServerClientBuilder setHost(String host) {
        this.host = host;
        return this;
    }


    public SingleServerClientBuilder setProt(int prot) {
        this.prot = prot;
        return this;
    }
}

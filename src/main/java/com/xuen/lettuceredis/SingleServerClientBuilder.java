package com.xuen.lettuceredis;

import com.lambdaworks.redis.RedisURI;
import com.lambdaworks.redis.RedisURI.Builder;

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

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getProt() {
        return prot;
    }

    public void setProt(int prot) {
        this.prot = prot;
    }
}

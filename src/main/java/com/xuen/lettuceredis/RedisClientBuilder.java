package com.xuen.lettuceredis;

import com.google.common.base.Preconditions;
import com.lambdaworks.redis.ClientOptions;
import com.lambdaworks.redis.ClientOptions.DisconnectedBehavior;
import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisURI;
import com.lambdaworks.redis.RedisURI.Builder;
import com.lambdaworks.redis.api.async.RedisAsyncCommands;
import com.lambdaworks.redis.api.sync.RedisCommands;
import io.netty.util.internal.StringUtil;
import java.util.concurrent.TimeUnit;

/**
 * @author zheng.xu
 * @since 2017-05-10
 */
public abstract class RedisClientBuilder {

    private static final long DEF_TIMEOUT = 1500;
    private static final TimeUnit DEF_TIMEOUT_UNIT = TimeUnit.MILLISECONDS;
    private static final String DEF_PASSWORD = "";
    // 这个选项必须关闭, 否则会造成连接 sentinel 发送 auth 命令造成无法连接, 因为 sentinel 不支持 auth
    private static final boolean DEF_PING_BEFORE_ACTIVATE_CONNECTION = false;
    private static final boolean DEF_AUTO_RECONNECT = true;
    private static final boolean DEF_CANCEL_COMMANDS_ON_RECONNECT_FAILURE = false;
    private static final boolean DEF_SUSPEND_RECONNECT_ON_PROTOCOL_FAILURE = false;
    private static final int DEF_REQUEST_QUEUE_SIZE = 100000;
    private static final DisconnectedBehavior DEF_DISCONNECTED_BEHAVIOR = DisconnectedBehavior.DEFAULT;

    private long timeout = DEF_TIMEOUT;
    private TimeUnit timeoutUnit = DEF_TIMEOUT_UNIT;
    private String password = DEF_PASSWORD;

    private boolean pingBeforeActivateConnection = DEF_PING_BEFORE_ACTIVATE_CONNECTION;
    private boolean autoReconnect = DEF_AUTO_RECONNECT;
    private boolean cancelCommandsOnReconnectFailure = DEF_CANCEL_COMMANDS_ON_RECONNECT_FAILURE;
    private boolean suspendReconnectOnProtocolFailure = DEF_SUSPEND_RECONNECT_ON_PROTOCOL_FAILURE;
    private int requestQueueSize = DEF_REQUEST_QUEUE_SIZE;
    private DisconnectedBehavior disconnectedBehavior = DEF_DISCONNECTED_BEHAVIOR;


    public static SingleServerClientBuilder createSingle(String host, int prot) {
        Preconditions.checkArgument(!StringUtil.isNullOrEmpty(host), "host is null");
        Preconditions
                .checkArgument(!(prot < 0 || prot > 65535), "The port number is not legitimate");
        return new SingleServerClientBuilder(host, prot);
    }

    private ClientOptions buildClientOptions() {
        return ClientOptions.builder()
                .pingBeforeActivateConnection(pingBeforeActivateConnection)
                .autoReconnect(autoReconnect)
                .cancelCommandsOnReconnectFailure(cancelCommandsOnReconnectFailure)
                .suspendReconnectOnProtocolFailure(suspendReconnectOnProtocolFailure)
                .requestQueueSize(requestQueueSize)
                .disconnectedBehavior(disconnectedBehavior)
                .build();
    }

    public RedisAsyncCommands<String, String> buildAsync() {
        RedisURI redisURI = redisURIBuilder()
                .withTimeout(timeout, timeoutUnit)
                .withPassword(password)
                .build();

        RedisClient redisClient = RedisClient.create(redisURI);
        redisClient.setOptions(buildClientOptions());

        return redisClient.connect().async();
    }

    public RedisCommands<String, String> buildSync() {
        RedisURI redisURI = redisURIBuilder()
                .withTimeout(timeout, timeoutUnit)
                .withPassword(password)
                .build();

        RedisClient redisClient = RedisClient.create(redisURI);
        redisClient.setOptions(buildClientOptions());

        return redisClient.connect().sync();
    }

    protected abstract Builder redisURIBuilder();

    public RedisClientBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public RedisClientBuilder setTimeoutUnit(TimeUnit timeoutUnit) {
        this.timeoutUnit = timeoutUnit;
        return this;
    }

    public RedisClientBuilder setTimeout(long timeout) {
        this.timeout = timeout;
        return this;
    }

    public RedisClientBuilder setAutoReconnect(boolean autoReconnect) {
        this.autoReconnect = autoReconnect;
        return this;
    }

    public RedisClientBuilder setCancelCommandsOnReconnectFailure(
            boolean cancelCommandsOnReconnectFailure) {
        this.cancelCommandsOnReconnectFailure = cancelCommandsOnReconnectFailure;
        return this;
    }

    public RedisClientBuilder setSuspendReconnectOnProtocolFailure(
            boolean suspendReconnectOnProtocolFailure) {
        this.suspendReconnectOnProtocolFailure = suspendReconnectOnProtocolFailure;
        return this;
    }

    public RedisClientBuilder setRequestQueueSize(int requestQueueSize) {
        this.requestQueueSize = requestQueueSize;
        return this;
    }

    public RedisClientBuilder setDisconnectedBehavior(
            DisconnectedBehavior disconnectedBehavior) {
        this.disconnectedBehavior = disconnectedBehavior;
        return this;
    }

}

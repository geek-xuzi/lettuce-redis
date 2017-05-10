package com.xuen.lettuceredis;

import com.lambdaworks.redis.ClientOptions.DisconnectedBehavior;
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
}

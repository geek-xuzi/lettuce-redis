package com.xuen.lock;

import com.lambdaworks.redis.api.sync.RedisCommands;
import java.util.Random;
import org.apache.commons.lang3.StringUtils;

/**
 * @author zheng.xu
 * @since 2017-06-16
 */
public class DistributedLock {

    /**
     * 锁超时时间,防止线程在入锁以后,无限的执行等待
     */
    private int expireMsecs = 60 * 1000;

    /**
     * 锁等待时间
     */
    private int timeoutMsecs = 10 * 1000;

    private volatile boolean locked = false;

    //Lock key
    private String lockKey;

    private final Random random = new Random();

    private RedisCommands<String, String> redisCommands;

    public DistributedLock(String lockKey, RedisCommands<String, String> redisCommands) {
        this.lockKey = lockKey;
        this.redisCommands = redisCommands;
    }

    DistributedLock(String lockKey, RedisCommands<String, String> redisCommands,
            int timeoutMsecs) {
        this(lockKey, redisCommands);
        this.timeoutMsecs = timeoutMsecs;
    }

    public synchronized boolean lock() throws InterruptedException {
        int timeOut = timeoutMsecs;
        while (timeOut > 0) {
            // 锁的过期时间
            long expire = System.currentTimeMillis() + expireMsecs + 1;
            String expireStr = String.valueOf(expire);
            if (redisCommands.setnx(this.lockKey, expireStr)) {
                this.locked = true;
                return true;
            }
            String currentValue = redisCommands.get(this.lockKey);
            // 如果当前的进程的时间大于了锁的过期时间,就可以获取锁,并为其设置新的过期时间
            if (StringUtils.isNotEmpty(currentValue) && Long.parseLong(currentValue) < System
                    .currentTimeMillis()) {
                String oldValue = redisCommands.getset(this.lockKey, expireStr);
                if (StringUtils.isNotEmpty(oldValue) && oldValue.equals(currentValue)) {
                    this.locked = true;
                    return true;
                }
            }
            // 使用随机数作为进程的申请频率,尽量保证进程可以公平竞争锁
            int applyTime = random.nextInt(200);
            timeOut -= applyTime;
            Thread.sleep(applyTime);
        }

        return false;
    }


    public synchronized void unLock() {
        if (locked) {
            redisCommands.del(lockKey);
        }

    }

}

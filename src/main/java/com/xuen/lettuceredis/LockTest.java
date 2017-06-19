package com.xuen.lettuceredis;

import com.lambdaworks.redis.api.sync.RedisCommands;
import com.xuen.lock.DistributedLock;

/**
 * @author zheng.xu
 * @since 2017-06-16
 */
public class LockTest {

    public static void main(String[] args) {
        //同步
        RedisCommands<String, String> redisCommands = RedisClientBuilder
                .createSingle("xuzi520.cn", 6379)
                .setPassword("xuen123456")
                .buildSync();

        DistributedLock lock = new DistributedLock("lock", redisCommands);

        try {
            lock.lock();
            System.out.println("hello word");
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unLock();
        }
    }
}

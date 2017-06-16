package com.xuen.lettuceredis;

import com.lambdaworks.redis.api.sync.RedisCommands;
import com.xuen.lock.DistributedLock;

/**
 * @author zheng.xu
 * @since 2017-05-10
 */
public class TestLettuce {

    public static void main(String[] args) {
//        //异步
//        RedisAsyncCommands<String, String> redisAsyncCommands = RedisClientBuilder
//                .createSingle("xuzi520.cn", 6379)
//                .setPassword("xuen123456")
//                .buildAsync();
//        RedisFuture<String> redisStrFuture = redisAsyncCommands.get("xuen-str");
//        RedisFuture<Map<String, String>> redisHashFuture = redisAsyncCommands.hgetall("xuen-hash");
//        try {
//            String str = redisStrFuture.get();
//            Map<String, String> redisHash = redisHashFuture.get();
//            System.out.println(str);
//            for (Map.Entry entry : redisHash.entrySet()) {
//                System.out.println(entry.getKey() + ":" + entry.getValue());
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
        //同步
        RedisCommands<String, String> redisCommands = RedisClientBuilder
                .createSingle("xuzi520.cn", 6379)
                .setPassword("xuen123456")
                .buildSync();

        DistributedLock lock = new DistributedLock("lock", redisCommands);

        try {
            lock.lock();
            System.out.println("hello word");
            lock.unLock();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unLock();
        }

    }


}

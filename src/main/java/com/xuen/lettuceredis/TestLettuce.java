package com.xuen.lettuceredis;

import com.lambdaworks.redis.api.sync.RedisCommands;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        redisCommands.set("watchKey", "0");

        ExecutorService executor = Executors.newFixedThreadPool(20);
        for (int i = 0; i < 100000; i++) {// 测试一万人同时访问
            executor.execute(() -> {
                try {
                    String userInfo = UUID.randomUUID().toString().replaceAll("-", "");
                    Thread.sleep(10);
                    redisCommands.watch("watchKey");
                    String val = redisCommands.get("watchKey");
                    Integer valInt = Integer.valueOf(val);
                    if (valInt < 10) {
                        redisCommands.multi();
                        redisCommands.set("watchKey", String.valueOf(valInt + 1));
                        List<Object> list = redisCommands.exec();
                        if (list != null) {
                            System.out.println(userInfo + ":抢购成功");
                            redisCommands.sadd("success", userInfo);
                        } else {
                            System.out.println(userInfo + ":抢购失败");
//                            redisCommands.sadd("filed", userInfo);
                        }
                    } else {
                        System.out.println(userInfo + ":抢购失败:没有库存");
                        return;
                    }
                } catch (Exception e) {
                }
            });
        }
        executor.shutdown();

//        DistributedLock lock = new DistributedLock("lock", redisCommands);
//
//        try {
//            lock.lock();
//            System.out.println("hello word");
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } finally {
//            lock.unLock();
//        }

    }


}

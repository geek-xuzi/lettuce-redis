# lettuce-redis

## 一个基于lettcue框架的redis工具

- 异步
  ``` 
    RedisAsyncCommands<String, String> redisAsyncCommands = RedisClientBuilder
          .createSingle("host", 6379)
          .setPassword("password")
          .buildAsync();
  ```
          
- 同步
  ```
     RedisCommands<String, String> redisCommands = RedisClientBuilder
          .createSingle("host", 6379)
          .setPassword("password")
          .buildSync();
  ```
  ---
  - 单redis的同步和异步操作
  - Redis Sentinel HA方案的同步和异步操作
## 增加Future聚合工具
 
 - 用法 
   ```
     CompletionStage<Integer> future1 = CompletableFuture.supplyAsync(() -> 1);
        CompletionStage<Integer> future2 = CompletableFuture.supplyAsync(() -> 2);
        CompletionStage<Integer> future3 = CompletableFuture.supplyAsync(() -> 3);
        ArrayList<CompletionStage<Integer>> inputs = Lists
                .newArrayList(future1, future2, future3);
        CompletionStage<String> combine = LettuceSupport.combine(inputs, chain -> chain.get(0) + chain.get(1) + chain.get(2));
        try {
            String s = LettuceSupport.get(combine);
            System.out.println(s);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
   ```

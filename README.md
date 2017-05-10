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
  - redis集群的同步和异步操作(未实现)

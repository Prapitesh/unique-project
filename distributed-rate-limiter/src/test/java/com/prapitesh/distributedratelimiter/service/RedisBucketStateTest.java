package com.prapitesh.distributedratelimiter.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class RedisBucketStateTest {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    void shouldStoreAndReadBucketStateFromRedis() {

        String key = "rate_limit:user123";

        HashOperations<String, String, String> hashOperations =
                redisTemplate.opsForHash();

        hashOperations.put(key, "tokens", "3");
        hashOperations.put(key, "lastRefillTime", "123456789");

        String tokens = hashOperations.get(key, "tokens");
        String lastRefillTime = hashOperations.get(key, "lastRefillTime");

        assertEquals("3", tokens);
        assertEquals("123456789", lastRefillTime);
    }
}
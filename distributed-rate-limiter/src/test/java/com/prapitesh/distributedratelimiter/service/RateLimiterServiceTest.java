package com.prapitesh.distributedratelimiter.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class RateLimiterServiceTest {

    @Autowired
    private RateLimiterService rateLimiterService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @BeforeEach
    void cleanRedis() {
        redisTemplate.delete("rate_limit:user123");
        redisTemplate.delete("rate_limit:user456");
    }

    @Test
    void shouldMaintainSeparateBucketsForDifferentClients() {

        assertTrue(rateLimiterService.isAllowed("user123"));
        assertTrue(rateLimiterService.isAllowed("user123"));
        assertTrue(rateLimiterService.isAllowed("user123"));

        assertFalse(rateLimiterService.isAllowed("user123"));

        assertTrue(rateLimiterService.isAllowed("user456"));
    }
}
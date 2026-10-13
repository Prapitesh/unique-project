package com.prapitesh.distributedratelimiter.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    void shouldEnforceLimitForConcurrentRequests() throws Exception {
        String clientId = "concurrent-user";
        String redisKey = "rate_limit:" + clientId;

        redisTemplate.delete(redisKey);

        int requestCount = 20;
        int threadCount = 20;

        ExecutorService executor =
                Executors.newFixedThreadPool(threadCount);

        CountDownLatch ready = new CountDownLatch(threadCount);
        CountDownLatch start = new CountDownLatch(1);
        AtomicInteger allowedCount = new AtomicInteger();

        try {
            List<Callable<Void>> tasks = java.util.stream.IntStream
                    .range(0, requestCount)
                    .mapToObj(i -> (Callable<Void>) () -> {
                        ready.countDown();
                        start.await();

                        if (rateLimiterService.isAllowed(clientId)) {
                            allowedCount.incrementAndGet();
                        }

                        return null;
                    })
                    .toList();

            List<Future<Void>> futures = tasks.stream()
                    .map(executor::submit)
                    .toList();

            assertTrue(ready.await(10, TimeUnit.SECONDS));
            start.countDown();

            for (Future<Void> future : futures) {
                future.get(15, TimeUnit.SECONDS);
            }

            assertEquals(3, allowedCount.get(),
                    "Only 3 concurrent requests should be allowed");
        } finally {
            start.countDown();
            executor.shutdownNow();
            redisTemplate.delete(redisKey);
        }
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
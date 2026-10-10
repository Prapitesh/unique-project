
package com.prapitesh.distributedratelimiter.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class LeakyBucketTest {

    @Autowired
    private RateLimiterService rateLimiterService;

    @Test
    void shouldRejectRequestsWhenBucketIsFull() {

        String clientId = "leaky-test-" + UUID.randomUUID();

        // Capacity is configured as 3.
        List<Long> first = rateLimiterService.isAllowedLeakyBucket(clientId);
        List<Long> second = rateLimiterService.isAllowedLeakyBucket(clientId);
        List<Long> third = rateLimiterService.isAllowedLeakyBucket(clientId);
        List<Long> fourth = rateLimiterService.isAllowedLeakyBucket(clientId);

        assertNotNull(first);
        assertNotNull(second);
        assertNotNull(third);
        assertNotNull(fourth);

        assertEquals(1L, first.get(0));
        assertEquals(1L, second.get(0));
        assertEquals(1L, third.get(0));

        // The bucket is full, so the fourth request should be rejected.
        assertEquals(0L, fourth.get(0));

        // Retry delay should be present and non-negative.
        assertTrue(fourth.get(1) >= 0,
                "Retry delay must not be negative");
    }
}

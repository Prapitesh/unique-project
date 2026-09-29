package com.prapitesh.distributedratelimiter.service;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
public class RateLimiterServiceTest {
    @Test
    void shouldMaintainSeparateBucketsForDifferentClients() {
        RateLimiterService rateLimiterService = new RateLimiterService();
        // user123 gets 3 tokens
        assertTrue(rateLimiterService.isAllowed("user123"));
        assertTrue(rateLimiterService.isAllowed("user123"));
        assertTrue(rateLimiterService.isAllowed("user123"));
        // user123 has exhausted its bucket
        assertFalse(rateLimiterService.isAllowed("user123"));
        // user456 has its own separate bucket
        assertTrue(rateLimiterService.isAllowed("user456"));
    }
}

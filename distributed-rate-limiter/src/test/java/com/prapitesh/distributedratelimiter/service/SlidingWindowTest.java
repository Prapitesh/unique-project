package com.prapitesh.distributedratelimiter.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class SlidingWindowTest {

    @Autowired
    private RateLimiterService rateLimiterService;

    @Test
    void shouldAllowOnlyThreeRequestsWithinWindow() {

        String clientId = "sliding-test-user";

        assertTrue(
                rateLimiterService.isAllowedSlidingWindow(clientId)
        );

        assertTrue(
                rateLimiterService.isAllowedSlidingWindow(clientId)
        );

        assertTrue(
                rateLimiterService.isAllowedSlidingWindow(clientId)
        );

        assertFalse(
                rateLimiterService.isAllowedSlidingWindow(clientId)
        );
    }
}
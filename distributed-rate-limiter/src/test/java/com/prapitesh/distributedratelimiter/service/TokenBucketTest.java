package com.prapitesh.distributedratelimiter.service;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
public class TokenBucketTest {
    @Test
    void shouldAllowRequestsUptoCapacity(){
            TokenBucket tokenBucket = new TokenBucket(3,1);
            assertTrue(tokenBucket.allow());
        assertTrue(tokenBucket.allow());
        assertTrue(tokenBucket.allow());
        assertFalse(tokenBucket.allow());
    }
}

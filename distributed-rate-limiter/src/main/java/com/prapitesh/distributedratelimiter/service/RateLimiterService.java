package com.prapitesh.distributedratelimiter.service;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimiterService {
    Map<String,TokenBucket> tokenBucketMap = new ConcurrentHashMap<>();
    public boolean isAllowed(String clientId){
        System.out.println("Checking rate limit for client: " + clientId);
        TokenBucket tokenBucket=tokenBucketMap.computeIfAbsent(clientId,id->new TokenBucket(3,1));
        return tokenBucket.allow();
    }
}

package com.prapitesh.distributedratelimiter.service;
import org.springframework.stereotype.Service;
@Service
public class RateLimiterService {
    private final TokenBucket tokenBucket;
    public RateLimiterService(){
        this.tokenBucket=new TokenBucket(3,1);
    }
    public boolean isAllowed(String clientId){
        System.out.println("Checking rate limit for client: " + clientId);
        return tokenBucket.allow();
    }
}

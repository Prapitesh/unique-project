package com.prapitesh.distributedratelimiter.service;
import org.springframework.stereotype.Service;
@Service
public class RateLimiterService {
    public boolean isAllowed(String clientId){
        System.out.println("Checking rate limit for client: " + clientId);
        return true;
    }
}

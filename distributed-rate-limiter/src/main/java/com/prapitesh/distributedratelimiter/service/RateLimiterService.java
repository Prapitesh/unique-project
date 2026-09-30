package com.prapitesh.distributedratelimiter.service;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RateLimiterService {
    private final StringRedisTemplate redisTemplate;
    private final RedisScript<Long> rateLimiterScript;
    public RateLimiterService(
            StringRedisTemplate redisTemplate,
            RedisScript<Long> rateLimiterScript) {
        this.redisTemplate = redisTemplate;
        this.rateLimiterScript = rateLimiterScript;
    }
    public boolean isAllowed(String clientId){
        System.out.println("Checking rate limit for client: " + clientId);
        String k="rate_limit:"+clientId;
        Long r=redisTemplate.execute(rateLimiterScript, List.of(k),"3","1",String.valueOf(System.currentTimeMillis()));
        return r!=null&&r==1L;
    }
}

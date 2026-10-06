package com.prapitesh.distributedratelimiter.service;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RateLimiterService {
    private final StringRedisTemplate redisTemplate;
    private final RedisScript<Long> rateLimiterScript;
    private final RedisScript<Long> slidingWindowScript;
    private final RedisScript<List<Long>> leakyBucketScript;
    public RateLimiterService(
            StringRedisTemplate redisTemplate,
            RedisScript<Long> rateLimiterScript,
            RedisScript<Long> slidingWindowScript,
            RedisScript<List<Long>> leakyBucketScript) {
        this.redisTemplate = redisTemplate;
        this.rateLimiterScript = rateLimiterScript;
        this.slidingWindowScript = slidingWindowScript;
        this.leakyBucketScript = leakyBucketScript;
    }
    public boolean isAllowed(String clientId){
        System.out.println("Checking rate limit for client: " + clientId);
        String k="rate_limit:"+clientId;
        Long r=redisTemplate.execute(rateLimiterScript, List.of(k),"3","1",String.valueOf(System.currentTimeMillis()));
        return r!=null&&r==1L;
    }

    public boolean isAllowedSlidingWindow(String clientId){
        System.out.println("Checking sliding window rate limit for client: " + clientId);
        String k="rate_limit:sliding"+clientId;
        String requestId=clientId+":"+System.nanoTime();
        Long r=redisTemplate.execute(slidingWindowScript, List.of(k),"3","10",requestId);
        return r!=null&&r==1L;
    }
    public List<Long> isAllowedLeakyBucket(String clientId){
        System.out.println("Checking leaky bucket rate limit for client: " + clientId);
        String k="rate_limit:leaky"+clientId;
        return redisTemplate.execute(leakyBucketScript,List.of(k),"3","1");
//        return r!=null&&r.get(0)==1L;
    }
}

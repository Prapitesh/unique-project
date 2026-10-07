package com.prapitesh.distributedratelimiter.service;
import com.prapitesh.distributedratelimiter.config.RateLimitProperties;
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
    private final RateLimitProperties rateLimitProperties;
    public RateLimiterService(
            StringRedisTemplate redisTemplate,
            RedisScript<Long> rateLimiterScript,
            RedisScript<Long> slidingWindowScript,
            RedisScript<List<Long>> leakyBucketScript,
            RateLimitProperties rateLimitProperties) {
        this.redisTemplate = redisTemplate;
        this.rateLimiterScript = rateLimiterScript;
        this.slidingWindowScript = slidingWindowScript;
        this.leakyBucketScript = leakyBucketScript;
        this.rateLimitProperties=rateLimitProperties;
    }
    public boolean isAllowed(String clientId){
        System.out.println("Checking rate limit for client: " + clientId);
        String k="rate_limit:"+clientId;
        Long r=redisTemplate.execute(rateLimiterScript, List.of(
                k,
                String.valueOf(rateLimitProperties.getCapacity()),
                String.valueOf(rateLimitProperties.getRefillRate())
        ),String.valueOf(System.currentTimeMillis()));
        return r!=null&&r==1L;
    }

    public boolean isAllowedSlidingWindow(String clientId){
        System.out.println("Checking sliding window rate limit for client: " + clientId);
        String k="rate_limit:sliding"+clientId;
        String requestId=clientId+":"+System.nanoTime();
        Long r=redisTemplate.execute(slidingWindowScript,List.of(
                k,
                String.valueOf(rateLimitProperties.getCapacity()),
                String.valueOf(rateLimitProperties.getWindowSeconds()),
                requestId
        ));
        return r!=null&&r==1L;
    }
    public List<Long> isAllowedLeakyBucket(String clientId){
        System.out.println("Checking leaky bucket rate limit for client: " + clientId);
        String k="rate_limit:leaky"+clientId;
        return redisTemplate.execute(
                leakyBucketScript,
                List.of(k),
                String.valueOf(rateLimitProperties.getCapacity()),
                String.valueOf(rateLimitProperties.getLeakRate())
        );
//        return r!=null&&r.get(0)==1L;
    }
}

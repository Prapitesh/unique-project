package com.prapitesh.distributedratelimiter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.List;

@Configuration
public class RedisConfig {

    @Bean
    public RedisScript<Long> rateLimiterScript() {
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        script.setLocation(new ClassPathResource("scripts/rate_limiter.lua"));
        script.setResultType(Long.class);
        return script;
    }

    @Bean
    public RedisScript<Long> slidingWindowScript() {
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        script.setLocation(new ClassPathResource("scripts/sliding_window.lua"));
        script.setResultType(Long.class);
        return script;
    }

    @Bean
    public RedisScript<List<Long>> leakyBucketScript() {
        DefaultRedisScript<List<Long>> script = new DefaultRedisScript<>();
        script.setLocation(new ClassPathResource("scripts/leaky_bucket.lua"));
        script.setResultType((Class)List.class);
        return script;
    }
}
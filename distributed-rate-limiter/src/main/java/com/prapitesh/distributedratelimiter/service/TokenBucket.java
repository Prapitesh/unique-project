package com.prapitesh.distributedratelimiter.service;

public class TokenBucket {
    private int capacity;
    private double currentTokens;
    private double refillRate;
    private long lastRefillTime;

    public TokenBucket(int capacity, long refillRate) {
        this.capacity = capacity;
        this.currentTokens = capacity;
        this.refillRate = refillRate;
        lastRefillTime = System.currentTimeMillis();
    }

    public boolean allow() {
        long currentTime = System.currentTimeMillis();
        long elapsed = currentTime - lastRefillTime;
        double elapsedseconds = elapsed / 1000.0;
        double newTokens=elapsedseconds*refillRate;
        currentTokens=Math.min(capacity,currentTokens+newTokens);
        lastRefillTime = currentTime;
        if(currentTokens>=1){
            currentTokens--;
            return  true;
        }
        else{
            return  false;
        }
    }
}


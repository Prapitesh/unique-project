package com.prapitesh.distributedratelimiter.service;

public class RedisBucketState {
    private double tokens;
    private long lastRefillTime;
    public RedisBucketState(double tokens, long lastRefillTime) {
        this.tokens = tokens;
        this.lastRefillTime = lastRefillTime;
    }
    public double getTokens() {
        return tokens;
    }
    public long getLastRefillTime() {
        return lastRefillTime;
    }
}

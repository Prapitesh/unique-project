package com.prapitesh.distributedratelimiter.filter;
import com.prapitesh.distributedratelimiter.service.RateLimiterService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;

public class RateLimiterFilter extends OncePerRequestFilter {
    private final RateLimiterService rateLimiterService;
    public RateLimiterFilter(RateLimiterService rateLimiterService) {
        this.rateLimiterService = rateLimiterService;
    }
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        System.out.println("Request received");
        String clientId = request.getHeader("X-Client-Id");
        System.out.println("Client ID: " + clientId);
        List<Long> result = rateLimiterService.isAllowedLeakyBucket(clientId);

        boolean allowed = result != null && result.get(0) == 1L;

        if (allowed) {
            chain.doFilter(request, response);
        } else {
            long retryAfterMs = result.get(1);
            long retryAfterSeconds = (retryAfterMs + 999) / 1000;

            response.setStatus(429);
            response.setHeader("Retry-After", String.valueOf(retryAfterSeconds));
            response.setHeader("X-RateLimit-Limit", "3");
            response.setHeader("X-RateLimit-Remaining", "0");
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Too many requests\"}");
        }
    }
}
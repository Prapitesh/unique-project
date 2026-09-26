package com.prapitesh.distributedratelimiter.filter;
import com.prapitesh.distributedratelimiter.service.RateLimiterService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
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
        boolean allowed = rateLimiterService.isAllowed(clientId);
        if (allowed) {
            chain.doFilter(request, response);
        } else {
            response.setStatus(429);
        }
    }
}
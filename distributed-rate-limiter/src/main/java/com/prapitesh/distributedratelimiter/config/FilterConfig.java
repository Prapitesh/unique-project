package com.prapitesh.distributedratelimiter.config;
import com.prapitesh.distributedratelimiter.filter.RateLimiterFilter;
import com.prapitesh.distributedratelimiter.service.RateLimiterService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<RateLimiterFilter> filterRegistrationBean(
            RateLimiterService rateLimiterService) {
        RateLimiterFilter filter =
                new RateLimiterFilter(rateLimiterService);
        FilterRegistrationBean<RateLimiterFilter> registration =
                new FilterRegistrationBean<>();
        registration.setFilter(filter);
        registration.addUrlPatterns("/api/*");
        return registration;
    }
}
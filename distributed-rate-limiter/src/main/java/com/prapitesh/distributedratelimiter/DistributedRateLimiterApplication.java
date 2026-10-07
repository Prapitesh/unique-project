package com.prapitesh.distributedratelimiter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
@ConfigurationPropertiesScan
@SpringBootApplication
public class DistributedRateLimiterApplication {

	public static void main(String[] args) {
		SpringApplication.run(DistributedRateLimiterApplication.class, args);
	}

}

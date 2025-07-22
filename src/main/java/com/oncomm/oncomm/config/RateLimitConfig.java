// RateLimitConfig.java
package com.oncomm.oncomm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RateLimitConfig {

    @Bean
    public Duration defaultRateLimitWindow() {
        return Duration.ofMinutes(1); // 기본 1분 윈도우
    }
}
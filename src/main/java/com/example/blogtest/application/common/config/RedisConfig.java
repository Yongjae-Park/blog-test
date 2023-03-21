package com.example.blogtest.application.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

@Configuration
public class RedisConfig {

    @Bean
    public ZSetOperations zSetOperations(StringRedisTemplate redisTemplate) {
        return redisTemplate.opsForZSet();
    }

}

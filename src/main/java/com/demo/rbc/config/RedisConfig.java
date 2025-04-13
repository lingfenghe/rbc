package com.demo.rbc.config;

import com.demo.rbc.entity.Account;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.concurrent.Executor;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Account> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Account> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        // JSON序列化配置
        Jackson2JsonRedisSerializer<Account> serializer = new Jackson2JsonRedisSerializer<>(Account.class);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);

        return template;
    }

//    @Bean
//    public Executor delayedWriteExecutor() {
//        return Executors.newVirtualThreadPerTaskExecutor(); // 使用虚拟线程池
//    }
}

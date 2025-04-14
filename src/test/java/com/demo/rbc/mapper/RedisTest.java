package com.demo.rbc.mapper;

import com.demo.rbc.entity.Account;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;

@SpringBootTest
public class RedisTest {

    // 通过类名初始化Logger实例
    private static final Logger logger = LoggerFactory.getLogger(RedisTest.class);

    @Autowired
    private RedisTemplate<String, Account> redisTemplate;

    @Test
    void testSetAccountToCache() {
        Account account = new Account();
        account.setAccountNo("AC0004608826");
        account.setBalance(new BigDecimal("31916.33"));
        account.setVersion(0);
        redisTemplate.opsForValue().set("account:AC0004608826", account);
    }

    @Test
    void getAccountFromCache() {
        Account account = redisTemplate.opsForValue().get("account:AC0004608826");
        logger.info("account : {}", account);
        logger.info("boolean : {}", account != null);
    }

}

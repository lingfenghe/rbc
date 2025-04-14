package com.demo.rbc.service.impl;

import com.demo.rbc.dto.BalanceResponse;
import com.demo.rbc.entity.Account;
import com.demo.rbc.mapper.AccountMapper;
import com.demo.rbc.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Date;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private RedisTemplate<String, Account> redisTemplate;

    @Autowired
    private TaskScheduler taskScheduler;

    @Override
    public BalanceResponse getBalance(String accountNo) {
        BalanceResponse response = null;
        Account account = null;

        //cachaside模式,先读缓存，没有再读数据库，并写入缓存
        account = redisTemplate.opsForValue().get(buildCacheKey(accountNo));
        if (account != null) {
            response = new BalanceResponse();
            response.setAccountNo(accountNo);
            response.setBalance(account.getBalance());
            logger.info("return from cache, account: {}", account);
            return response;
        }

        account = accountMapper.findByAccountNo(accountNo);

        //防止缓存击穿
        if (account == null) {
            response = new BalanceResponse();
            response.setAccountNo("NULL");
            response.setBalance(new BigDecimal("0.00"));
            logger.info("return from db, account: {}", account);

            redisTemplate.opsForValue().set(buildCacheKey(accountNo), account, Duration.ofMinutes(1));
            logger.info("write to cache, account : {}", account);
            return response;
        }

        response = new BalanceResponse();
        response.setAccountNo(accountNo);
        response.setBalance(account.getBalance());
        logger.info("return from db, account: {}", account);

        redisTemplate.opsForValue().set(buildCacheKey(accountNo), account, Duration.ofMinutes(30));
        logger.info("write to cache, account : {}", account);

        return response;
    }

    private String buildCacheKey(String accountNo) {
        return "account:" + accountNo;
    }

    @Override
    public void preloadtRecentUpdatedAccountsToCache() {
        List<Account> accounts = accountMapper.selectRecentUpdatedAccounts();
        // 分批次处理（建议每批1000条）
        int batchSize = 1000;
        for (int i = 0; i < accounts.size(); i += batchSize) {
            List<Account> subList = accounts.subList(i, Math.min(i + batchSize, accounts.size()));
            // 序列化器需与RedisTemplate配置一致
            redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
                StringRedisSerializer keySerializer = (StringRedisSerializer) redisTemplate.getKeySerializer();
                Jackson2JsonRedisSerializer<Account> valueSerializer =
                        (Jackson2JsonRedisSerializer<Account>) redisTemplate.getValueSerializer();

                subList.forEach(account -> {
                    // 生成缓存键（与原有buildCacheKey逻辑保持一致）
                    String key = buildCacheKey(account.getAccountNo());
                    byte[] serializedKey = keySerializer.serialize(key);
                    byte[] serializedValue = valueSerializer.serialize(account);

                    // 设置带过期时间[7](@ref)
                    connection.setEx(
                            serializedKey,
                            Duration.ofMinutes(30).getSeconds(), // 转换为秒
                            serializedValue
                    );
                });
                return null; // 必须返回null
            });
        }
    }

}
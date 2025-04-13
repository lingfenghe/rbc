package com.demo.rbc.service.impl;

import com.demo.rbc.dto.BalanceResponse;
import com.demo.rbc.entity.Account;
import com.demo.rbc.mapper.AccountMapper;
import com.demo.rbc.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;

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
            response.setAccountId(accountNo);
            response.setBalance(account.getBalance());
            logger.info("return from cache, account: {}", account);
            return response;
        }

        account = accountMapper.findByAccountNo(accountNo);

        response = new BalanceResponse();
        response.setAccountId(accountNo);
        response.setBalance(account.getBalance());
        logger.info("return from db, account: {}", account);

        redisTemplate.opsForValue().set(buildCacheKey(accountNo), account, Duration.ofMinutes(30));
        logger.info("write to cache, account : {}", account);

        return response;
    }

    private String buildCacheKey(String accountNo) {
        return "account:" + accountNo;
    }

}
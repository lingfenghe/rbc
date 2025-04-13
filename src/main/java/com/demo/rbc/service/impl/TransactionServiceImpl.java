package com.demo.rbc.service.impl;

import com.demo.rbc.constant.ErrorCode;
import com.demo.rbc.entity.Account;
import com.demo.rbc.entity.Transaction;
import com.demo.rbc.exception.BusinessException;
import com.demo.rbc.exception.OptimisticLockException;
import com.demo.rbc.mapper.AccountMapper;
import com.demo.rbc.mapper.TransactionMapper;
import com.demo.rbc.service.TransactionService;
import com.demo.rbc.util.SnowFlakeIdUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private RedisTemplate<String, Account> redisTemplate;

    @Autowired
    private TaskScheduler taskScheduler;

    @Override
    @Retryable(
        include = {OptimisticLockException.class},
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000)
    )
    @Transactional(rollbackFor = Exception.class)
    public String transferFund(String sourceAccountNo, String targetAccountNo, BigDecimal transferAmt) throws BusinessException, OptimisticLockException {
        // 查询账户信息
        Account sourceAccount = accountMapper.findByAccountNo(sourceAccountNo);
        Account targetAccount = accountMapper.findByAccountNo(targetAccountNo);

        if (sourceAccount == null || targetAccount == null) {
            throw new BusinessException(ErrorCode.ACCOUNT_NOT_EXIST);
        }

        if (sourceAccount.getBalance().compareTo(transferAmt) < 0) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_BALANCE);
        }

        // 使用乐观锁扣减账户余额,失败则通过springboot retry重试
        int deductCount = accountMapper.deductBalanceWithVersion(sourceAccountNo, transferAmt, sourceAccount.getVersion());
        if (deductCount == 0) {
            throw new OptimisticLockException();
        }

        // 使用乐观锁增加账户余额,失败则通过springboot retry重试
        int addCount = accountMapper.addBalanceWithVersion(targetAccountNo, transferAmt, targetAccount.getVersion());
        if (addCount == 0) {
            throw new OptimisticLockException();
        }

        List<String> cacheKeyList = new ArrayList<>();
        cacheKeyList.add(buildCacheKey(sourceAccountNo));
        cacheKeyList.add(buildCacheKey(targetAccountNo));
        redisTemplate.delete(cacheKeyList);
        logger.info("delete cache for the 1st time, cacheKeyList : {}", cacheKeyList);

        //异步延迟1S做2次删除,解决极端条件下的数据一致性问题
        taskScheduler.schedule(() -> {
            redisTemplate.delete(cacheKeyList);
            logger.info("delete cache for the 2ed time, cacheKeyList : {}", cacheKeyList);
        }, new Date(System.currentTimeMillis() + 1000));

        Transaction transaction = new Transaction();
        transaction.setTxId(SnowFlakeIdUtils.getId());
        transaction.setSourceAccountNo(sourceAccountNo);
        transaction.setTargetAccountNo(targetAccountNo);
        transaction.setAmount(transferAmt);
        transactionMapper.insert(transaction);

        return transaction.getTxId();
    }

    private String buildCacheKey(String accountNo) {
        return "account:" + accountNo;
    }
}

package com.demo.rbc.service.impl;

import cn.hutool.core.util.IdUtil;
import com.demo.rbc.constant.ErrorCode;
import com.demo.rbc.entity.Account;
import com.demo.rbc.entity.Transaction;
import com.demo.rbc.exception.BusinessException;
import com.demo.rbc.exception.OptimisticLockException;
import com.demo.rbc.mapper.AccountMapper;
import com.demo.rbc.mapper.TransactionMapper;
import com.demo.rbc.service.TransactionService;
import com.demo.rbc.util.SnowFlakeIdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private TransactionMapper transactionMapper;

    @Override
    @Retryable(
        value = {OptimisticLockException.class},
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

        Transaction transaction = new Transaction();
        transaction.setTxId(SnowFlakeIdUtils.getId());
        transaction.setSourceAccountNo(sourceAccountNo);
        transaction.setTargetAccountNo(targetAccountNo);
        transaction.setAmount(transferAmt);
        transactionMapper.insert(transaction);

        return transaction.getTxId();
    }
}

package com.demo.rbc.service;

import com.demo.rbc.exception.BusinessException;
import com.demo.rbc.exception.OptimisticLockException;

import java.math.BigDecimal;

public interface TransactionService {

    String transferFund(String sourceAccountNo, String targetAccountNo, BigDecimal transferAmt) throws BusinessException, OptimisticLockException;

}

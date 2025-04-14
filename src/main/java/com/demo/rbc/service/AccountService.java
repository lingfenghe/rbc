package com.demo.rbc.service;

import com.demo.rbc.dto.BalanceResponse;

import java.math.BigDecimal;

public interface AccountService {

    BalanceResponse getBalance(String accountNo);

    void preloadtRecentUpdatedAccountsToCache();

}
package com.demo.rbc.service;

import com.demo.rbc.dto.BalanceResponse;

public interface AccountService {
    BalanceResponse getBalance(String accountNo);
}
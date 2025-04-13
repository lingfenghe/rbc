package com.demo.rbc.service.impl;

import com.demo.rbc.dto.BalanceResponse;
import com.demo.rbc.entity.Account;
import com.demo.rbc.mapper.AccountMapper;
import com.demo.rbc.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public BalanceResponse getBalance(String accountNo) {
        Account account = accountMapper.findByAccountNo(accountNo);
        if (account == null) {
            return null;
        }

        BalanceResponse response = new BalanceResponse();
        response.setAccountId(accountNo);
        response.setBalance(account.getBalance());
        return response;
    }

}
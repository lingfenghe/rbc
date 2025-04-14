package com.demo.rbc.controller;

import com.demo.rbc.constant.ErrorCode;
import com.demo.rbc.dto.ApiResponse;
import com.demo.rbc.dto.BalanceResponse;
import com.demo.rbc.service.AccountService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/balance/{accountNo}")
    public ApiResponse<BalanceResponse> getBalance(@PathVariable String accountNo) {
        BalanceResponse response = accountService.getBalance(accountNo);
        if (StringUtils.equals(response.getAccountNo(), "NULL")) {
            return ApiResponse.error(ErrorCode.ACCOUNT_NOT_EXIST.getCode(), 
                                   ErrorCode.ACCOUNT_NOT_EXIST.getMsg());
        }
        return ApiResponse.success(response);
    }
}
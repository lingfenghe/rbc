package com.demo.rbc.controller;

import com.demo.rbc.dto.ApiResponse;
import com.demo.rbc.dto.BalanceResponse;
import com.demo.rbc.service.AccountService;
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
        if (response == null) {
            return ApiResponse.error(-101, "account not exist");
        }
        return ApiResponse.success(response);
    }
}
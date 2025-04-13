package com.demo.rbc.controller;

import com.demo.rbc.constant.ErrorCode;
import com.demo.rbc.dto.ApiResponse;
import com.demo.rbc.dto.TransactionRequest;
import com.demo.rbc.dto.TransactionResponse;
import com.demo.rbc.exception.BusinessException;
import com.demo.rbc.exception.OptimisticLockException;
import com.demo.rbc.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/transaction")
    public ApiResponse<TransactionResponse> createTransaction(@Valid @RequestBody TransactionRequest transactionRequest) {
        String txId = null;

        try {
            txId = transactionService.transferFund(transactionRequest.getSourceAccountNo(), transactionRequest.getTargetAccountNo(), transactionRequest.getAmount());
        } catch (BusinessException e) {
            return ApiResponse.error(e.getCode(), e.getMessage());
        } catch (OptimisticLockException e) {
            return ApiResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error(ErrorCode.UNKNOWN_EXCEPTION.getCode(), ErrorCode.UNKNOWN_EXCEPTION.getMsg());
        }

        return ApiResponse.success(new TransactionResponse(txId));
    }

}

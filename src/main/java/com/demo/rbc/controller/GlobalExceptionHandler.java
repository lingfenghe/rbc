package com.demo.rbc.controller;

import com.demo.rbc.constant.ErrorCode;
import com.demo.rbc.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<?> handleValidationException(MethodArgumentNotValidException ex) {
        // 提取所有字段错误信息
        String errorMsg = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));

        // 返回自定义 JSON 结构
        return ApiResponse.error(ErrorCode.INVALID_PARAM_EXCEPTION.getCode(), errorMsg);
    }

}

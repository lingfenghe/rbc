package com.demo.rbc.exception;

import com.demo.rbc.constant.ErrorCode;
import lombok.Data;

@Data
public class BusinessException extends Exception {

    private final int code;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMsg());
        this.code = errorCode.getCode();
    }

}
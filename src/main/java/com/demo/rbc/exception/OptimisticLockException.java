package com.demo.rbc.exception;

import com.demo.rbc.constant.ErrorCode;
import lombok.Data;

@Data
public class OptimisticLockException extends Exception{

    private final int code;

    public OptimisticLockException() {
        super(ErrorCode.LOCK_VERSION_FAILED.getMsg());
        this.code = ErrorCode.LOCK_VERSION_FAILED.getCode();
    }

}

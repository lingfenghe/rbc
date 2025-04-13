package com.demo.rbc.constant;

public enum ErrorCode {
    ACCOUNT_NOT_EXIST(-101, "账户不存在"),
    INSUFFICIENT_BALANCE(-102, "账户余额不足"),
    LOCK_VERSION_FAILED(-103, "乐观锁更新失败"),
    UNKNOWN_EXCEPTION(-104, "运行时未知异常"),
    INVALID_PARAM_EXCEPTION(-105, "参数校验异常");

    private final int code;
    private final String msg;

    ErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
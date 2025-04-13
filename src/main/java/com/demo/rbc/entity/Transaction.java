package com.demo.rbc.entity;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class Transaction {

    private String txId;

    private String sourceAccountNo;

    private String targetAccountNo;

    private BigDecimal amount;

    private Integer txStatus;

}
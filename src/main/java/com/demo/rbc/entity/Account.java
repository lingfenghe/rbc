package com.demo.rbc.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Account {

    private String accountNo;

    private BigDecimal balance;

    private Integer version;
}
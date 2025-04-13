package com.demo.rbc.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class BalanceResponse {
    private String accountId;
    private BigDecimal balance;
}
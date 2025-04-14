package com.demo.rbc.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class BalanceResponse {
    private String accountNo;
    private BigDecimal balance;
    @JsonIgnore
    private Integer version;
}
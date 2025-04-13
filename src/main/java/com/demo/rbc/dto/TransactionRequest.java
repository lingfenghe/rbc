package com.demo.rbc.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {

    @NotBlank(message = "源账号不能为空")
    private String sourceAccountNo;

    @NotBlank(message = "目标账号不能为空")
    private String targetAccountNo;

    @DecimalMin(value = "0.01", message = "金额必须大于0")
    private BigDecimal amount;

}

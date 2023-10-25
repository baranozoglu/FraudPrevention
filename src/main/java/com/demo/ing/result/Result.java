package com.demo.ing.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    public Long transactionId;
    public Long originatorId;
    public Long destinationId;
    public BigDecimal incomeAmount;
    public BigDecimal storedAmount;
    public ResultEnum resultEnum;
    public Long pdata;
}

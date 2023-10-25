package com.demo.ing.transaction.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IncomeTransaction extends Transaction {
    private Long PID;
    private BigDecimal PAMOUNT;
    private Long PDATA;

}

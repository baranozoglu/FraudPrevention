package com.demo.ing.result;

import java.math.BigDecimal;

public enum ResultEnum {

    INCOME_AMOUNT_MORE_THAN_STORED_AMOUNT,
    INCOME_AND_STORED_AMOUNT_EQUAL,
    STORED_AMOUNT_MORE_THAN_INCOME_AMOUNT;


    public static ResultEnum determineResultEnum(BigDecimal incomeAmount, BigDecimal storedAmount) {
        int comparison = incomeAmount.compareTo(storedAmount);
        switch (comparison) {
            case 1:
                return INCOME_AMOUNT_MORE_THAN_STORED_AMOUNT;
            case -1:
                return STORED_AMOUNT_MORE_THAN_INCOME_AMOUNT;
            default:
                return INCOME_AND_STORED_AMOUNT_EQUAL;
        }
    }

}

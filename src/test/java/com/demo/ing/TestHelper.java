package com.demo.ing;

import com.demo.ing.result.Result;
import com.demo.ing.result.ResultEnum;
import com.demo.ing.transaction.model.IncomeTransaction;
import com.demo.ing.transaction.model.StoredTransaction;
import com.demo.ing.transaction.model.TransactionMetaData;
import com.google.gson.Gson;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TestHelper {

    public static String asJsonString(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    public static StoredTransaction getStoredTransaction(Long id, BigDecimal amount) {
        return StoredTransaction.builder()
                .id(id)
                .amount(amount)
                .metadata(TransactionMetaData.builder()
                        .originatorId(2L)
                        .destinationId(1L)
                        .build())
                .build();
    }

    public static IncomeTransaction getIncomeTransaction(Long id, BigDecimal amount) {
        return IncomeTransaction.builder()
                .PID(id)
                .PAMOUNT(amount)
                .PDATA(1234567890L)
                .build();
    }

    public static Result getResult(Long id, BigDecimal incomeAmount, BigDecimal storedAmount, ResultEnum resultEnum) {
        return Result.builder()
                .transactionId(id)
                .destinationId(1L)
                .originatorId(2L)
                .incomeAmount(incomeAmount)
                .storedAmount(storedAmount)
                .pdata(1234567890L)
                .resultEnum(resultEnum)
                .build();
    }
}

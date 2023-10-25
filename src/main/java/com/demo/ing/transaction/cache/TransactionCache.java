package com.demo.ing.transaction.cache;

import com.demo.ing.exception.BadRequestException;
import com.demo.ing.transaction.model.StoredTransaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionCache implements Serializable {
    private static final String NULL_ERROR_MESSAGE = "Amount or Metadata was null!";
    private BigDecimal amount;
    private TransactionMetaDataCache metadata;

    public static TransactionCache convertToTransactionCache(StoredTransaction transaction) {
        if (transaction.getAmount() == null || Objects.isNull(transaction.getMetadata())) {
            throw new BadRequestException(NULL_ERROR_MESSAGE);
        }
        return TransactionCache.builder()
                .amount(transaction.getAmount())
                .metadata(TransactionMetaDataCache.convertToTransactionMetaDataCache(transaction.getMetadata()))
                .build();
    }
}

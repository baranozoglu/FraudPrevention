package com.demo.ing.transaction.model;

import com.demo.ing.exception.BadRequestException;
import com.demo.ing.transaction.cache.TransactionCache;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoredTransaction extends Transaction {
    private static final String NULL_ERROR_MESSAGE = "Id, amount or metadata was null!";
    private Long id;
    private BigDecimal amount;
    private TransactionMetaData metadata;

    public static StoredTransaction convertToTransaction(Long transactionId, TransactionCache transactionCache) {
        if (transactionId == null || transactionCache.getAmount() == null || transactionCache.getMetadata() == null) {
            throw new BadRequestException(NULL_ERROR_MESSAGE);
        }
        return StoredTransaction.builder()
                .id(transactionId)
                .amount(transactionCache.getAmount())
                .metadata(TransactionMetaData.convertToTransactionMetaData(transactionCache.getMetadata())).
                build();
    }
}

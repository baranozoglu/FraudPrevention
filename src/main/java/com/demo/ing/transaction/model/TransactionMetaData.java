package com.demo.ing.transaction.model;

import com.demo.ing.exception.BadRequestException;
import com.demo.ing.transaction.cache.TransactionMetaDataCache;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionMetaData {
    private static final String NULL_ERROR_MESSAGE = "OriginatorId or DestinationId was null!";

    private Long originatorId;
    private Long destinationId;

    public static TransactionMetaData convertToTransactionMetaData(TransactionMetaDataCache transactionMetaDataCache) {
        if (transactionMetaDataCache.getOriginatorId() == null || transactionMetaDataCache.getDestinationId() == null) {
            throw new BadRequestException(NULL_ERROR_MESSAGE);
        }
        return TransactionMetaData.builder()
                .originatorId(transactionMetaDataCache.getOriginatorId())
                .destinationId(transactionMetaDataCache.getDestinationId())
                .build();
    }

}

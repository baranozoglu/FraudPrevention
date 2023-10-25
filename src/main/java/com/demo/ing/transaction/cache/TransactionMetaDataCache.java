package com.demo.ing.transaction.cache;

import com.demo.ing.exception.BadRequestException;
import com.demo.ing.transaction.model.TransactionMetaData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionMetaDataCache implements Serializable {
    private static final String NULL_ERROR_MESSAGE = "OriginatorId or DestinationId was null!";
    private Long originatorId;
    private Long destinationId;

    public static TransactionMetaDataCache convertToTransactionMetaDataCache(TransactionMetaData transactionMetaData) {
        if (transactionMetaData.getOriginatorId() == null || transactionMetaData.getDestinationId() == null) {
            throw new BadRequestException(NULL_ERROR_MESSAGE);
        }
        return TransactionMetaDataCache.builder()
                .originatorId(transactionMetaData.getOriginatorId())
                .destinationId(transactionMetaData.getDestinationId())
                .build();
    }

}

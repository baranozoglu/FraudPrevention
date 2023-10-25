package com.demo.ing.result;

import com.demo.ing.exception.TransactionNotFoundException;
import com.demo.ing.transaction.model.IncomeTransaction;
import com.demo.ing.transaction.model.StoredTransaction;
import com.demo.ing.transaction.service.StoredTransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ResultService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private static final String COULD_NOT_FOUND_ERROR_MESSAGE = "Could not find any stored transaction with id %d";
    private final StoredTransactionService transactionService;
    private final ResultRepository resultRepository;

    public ResultService(StoredTransactionService transactionService,
                         ResultRepository resultRepository) {
        this.transactionService = transactionService;
        this.resultRepository = resultRepository;
    }

    public String getResult(Long transactionId) {
        return resultRepository.getCachedResult(transactionId);
    }

    public void insertResult(Long transactionId, String result) {
        resultRepository.insertCachedResult(transactionId, result);
    }

    public Result determineResult(IncomeTransaction incomeTransaction) {
        StoredTransaction storedTransaction = transactionService.findByTransactionId(incomeTransaction.getPID());
        throwErrorIfStoredTransactionIsNull(incomeTransaction, storedTransaction);
        return Result.builder()
                .transactionId(incomeTransaction.getPID())
                .destinationId(storedTransaction.getMetadata().getDestinationId())
                .originatorId(storedTransaction.getMetadata().getOriginatorId())
                .incomeAmount(incomeTransaction.getPAMOUNT())
                .storedAmount(storedTransaction.getAmount())
                .pdata(incomeTransaction.getPDATA())
                .resultEnum(ResultEnum.determineResultEnum(incomeTransaction.getPAMOUNT(), storedTransaction.getAmount()))
                .build();
    }

    private void throwErrorIfStoredTransactionIsNull(IncomeTransaction incomeTransaction, StoredTransaction storedTransaction) {
        if (Objects.isNull(storedTransaction)) {
            String error = String.format(COULD_NOT_FOUND_ERROR_MESSAGE, incomeTransaction.getPID());
            LOGGER.error(error);
            throw new TransactionNotFoundException(error);
        }
    }
}

package com.demo.ing.transaction.service;

import com.demo.ing.exception.TransactionNotFoundException;
import com.demo.ing.transaction.cache.TransactionCache;
import com.demo.ing.transaction.cache.TransactionCacheService;
import com.demo.ing.transaction.model.StoredTransaction;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class StoredTransactionService {
    private final TransactionCacheService transactionCacheService;
    private static final String NOT_FOUND_ERROR_MESSAGE = "Transaction could not found!";

    public StoredTransactionService(TransactionCacheService transactionCacheService) {
        this.transactionCacheService = transactionCacheService;
    }

    public void save(List<StoredTransaction> transactions) {
        transactions.forEach(transaction ->
                transactionCacheService.save(transaction.getId(), TransactionCache.convertToTransactionCache(transaction))
        );
    }

    public StoredTransaction findByTransactionId(Long transactionId) {
        TransactionCache transactionCache = transactionCacheService.findByTransactionId(transactionId);
        if (Objects.isNull(transactionCache)) {
            throw new TransactionNotFoundException(NOT_FOUND_ERROR_MESSAGE);
        }
        return StoredTransaction.convertToTransaction(transactionId, transactionCache);
    }

    public void deleteTransaction(Long transactionId) {
        transactionCacheService.deleteByTransactionId(transactionId);
    }

}

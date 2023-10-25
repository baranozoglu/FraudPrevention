package com.demo.ing.transaction.cache;

import org.springframework.stereotype.Service;

@Service
public class TransactionCacheService {
    private final TransactionCacheRepository transactionCacheRepository;

    public TransactionCacheService(TransactionCacheRepository transactionCacheRepository) {
        this.transactionCacheRepository = transactionCacheRepository;
    }

    public void save(Long transactionId, TransactionCache transaction) {
        transactionCacheRepository.insertCachedTransaction(transactionId, transaction);
    }

    public TransactionCache findByTransactionId(Long transactionId) {
        return transactionCacheRepository.getCachedTransaction(transactionId);
    }

    public void deleteByTransactionId(Long transactionId) {
        transactionCacheRepository.deleteCachedTransaction(transactionId);
    }

}

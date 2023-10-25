package com.demo.ing.transaction.service;

import com.demo.ing.exception.TransactionNotFoundException;
import com.demo.ing.transaction.cache.TransactionCache;
import com.demo.ing.transaction.cache.TransactionCacheService;
import com.demo.ing.transaction.cache.TransactionMetaDataCache;
import com.demo.ing.transaction.model.StoredTransaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import static com.demo.ing.TestHelper.getStoredTransaction;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class StoredTransactionServiceTest {
    @Mock
    private TransactionCacheService transactionCacheService;
    @InjectMocks
    private StoredTransactionService storedTransactionService;

    @Before
    public void setUp() {
        reset(transactionCacheService);
    }

    @Test
    public void testSaveWhenCalledThenCallsTransactionCacheServiceSave() {
        StoredTransaction transaction1 = getStoredTransaction(123L, BigDecimal.ONE);
        StoredTransaction transaction2 = getStoredTransaction(124L, BigDecimal.ONE);

        storedTransactionService.save(Arrays.asList(transaction1, transaction2));

        verify(transactionCacheService, times(1)).save(transaction1.getId(), TransactionCache.convertToTransactionCache(transaction1));
        verify(transactionCacheService, times(1)).save(transaction2.getId(), TransactionCache.convertToTransactionCache(transaction2));
    }

    @Test
    public void testSaveWhenEmptyListThenDoesNotCallTransactionCacheServiceSave() {
        storedTransactionService.save(Collections.emptyList());

        verify(transactionCacheService, never()).save(anyLong(), any(TransactionCache.class));
    }

    @Test
    public void testFindByTransactionIdWhenTransactionIdIsValidThenReturnsCorrectStoredTransaction() {
        Long transactionId = 1L;
        TransactionCache transactionCache = TransactionCache.builder()
                .amount(BigDecimal.TEN)
                .metadata(TransactionMetaDataCache.builder()
                        .originatorId(1L)
                        .destinationId(2L)
                        .build())
                .build();

        when(transactionCacheService.findByTransactionId(transactionId)).thenReturn(transactionCache);

        StoredTransaction result = storedTransactionService.findByTransactionId(transactionId);

        assertEquals(transactionId, result.getId());
        assertEquals(transactionCache.getAmount(), result.getAmount());
    }

    @Test(expected = TransactionNotFoundException.class)
    public void testFindByTransactionIdWhenTransactionIdIsInvalidThenThrowsTransactionNotFoundException() {
        Long transactionId = 1L;
        when(transactionCacheService.findByTransactionId(transactionId)).thenReturn(null);
        storedTransactionService.findByTransactionId(transactionId);
    }

}
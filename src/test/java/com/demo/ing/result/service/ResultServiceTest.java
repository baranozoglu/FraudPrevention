package com.demo.ing.result.service;

import com.demo.ing.exception.TransactionNotFoundException;
import com.demo.ing.result.Result;
import com.demo.ing.result.ResultEnum;
import com.demo.ing.result.ResultRepository;
import com.demo.ing.result.ResultService;
import com.demo.ing.transaction.model.IncomeTransaction;
import com.demo.ing.transaction.model.StoredTransaction;
import com.demo.ing.transaction.service.StoredTransactionService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static com.demo.ing.TestHelper.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.testng.AssertJUnit.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ResultServiceTest {

    @Mock
    private StoredTransactionService transactionService;
    @Mock
    private ResultRepository resultRepository;
    @InjectMocks
    private ResultService resultService;

    @BeforeEach
    public void setUp() {
        resultService = new ResultService(transactionService, resultRepository);
    }

    @Test
    public void test_determineResult_when_incomeAmountMoreThanStoredAmount() {
        IncomeTransaction incomeTransaction = getIncomeTransaction(123L, BigDecimal.valueOf(100.00));
        StoredTransaction storedTransaction = getStoredTransaction(123L, BigDecimal.valueOf(50.00));

        when(transactionService.findByTransactionId(anyLong())).thenReturn(storedTransaction);

        Result expected = getResult(123L, BigDecimal.valueOf(100.00), BigDecimal.valueOf(50.00), ResultEnum.INCOME_AMOUNT_MORE_THAN_STORED_AMOUNT);

        Result actual = resultService.determineResult(incomeTransaction);
        assertEquals(expected, actual);
    }

    @Test
    public void test_determineResult_when_storedAmountMoreThanIncomeAmountTORED_AMOUNT_MORE_THAN_INCOME_AMOUNT() {
        IncomeTransaction incomeTransaction = getIncomeTransaction(123L, BigDecimal.valueOf(100.00));
        StoredTransaction storedTransaction = getStoredTransaction(123L, BigDecimal.valueOf(150.00));


        when(transactionService.findByTransactionId(anyLong())).thenReturn(storedTransaction);

        Result expected = getResult(123L,
                BigDecimal.valueOf(100.00),
                BigDecimal.valueOf(150.00),
                ResultEnum.STORED_AMOUNT_MORE_THAN_INCOME_AMOUNT);

        Result actual = resultService.determineResult(incomeTransaction);
        assertEquals(expected, actual);
    }

    @Test
    public void test_determineResult_when_incomeAndStoredAmountEqual() {
        IncomeTransaction incomeTransaction = getIncomeTransaction(123L, BigDecimal.valueOf(100.00));
        StoredTransaction storedTransaction = getStoredTransaction(123L, BigDecimal.valueOf(100.00));

        when(transactionService.findByTransactionId(anyLong())).thenReturn(storedTransaction);

        Result expected = getResult(123L,
                BigDecimal.valueOf(100.00),
                BigDecimal.valueOf(100.00),
                ResultEnum.INCOME_AND_STORED_AMOUNT_EQUAL);

        Result actual = resultService.determineResult(incomeTransaction);
        assertEquals(expected, actual);
    }

    @Test(expected = TransactionNotFoundException.class)
    public void test_determineResult_when_storedTransactionIsNull() {
        IncomeTransaction incomeTransaction = getIncomeTransaction(123L, BigDecimal.valueOf(100.00));
        StoredTransaction storedTransaction = null;

        when(transactionService.findByTransactionId(anyLong())).thenReturn(storedTransaction);

        resultService.determineResult(incomeTransaction);
    }


}

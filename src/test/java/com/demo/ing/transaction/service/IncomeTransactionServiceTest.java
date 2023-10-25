package com.demo.ing.transaction.service;

import com.demo.ing.transaction.model.IncomeTransaction;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.demo.ing.TestHelper.getIncomeTransaction;
import static com.demo.ing.common.KafkaProperties.TRANSACTION_TOPIC;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class IncomeTransactionServiceTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private IncomeTransactionService incomeTransactionService;

    @Test
    void testPublishIncomeTransactionWhenCalledThenSendIsInvoked() {
        List<IncomeTransaction> transactions = new ArrayList<>(List.of(getIncomeTransaction(123L, BigDecimal.TEN), getIncomeTransaction(124L, BigDecimal.TEN)));
        String expectedJson = "[{\"PID\":123,\"PAMOUNT\":10,\"PDATA\":1234567890},{\"PID\":124,\"PAMOUNT\":10,\"PDATA\":1234567890}]";
        incomeTransactionService.publishIncomeTransaction(transactions);
        verify(kafkaTemplate, times(1)).send(TRANSACTION_TOPIC, expectedJson);
    }

}
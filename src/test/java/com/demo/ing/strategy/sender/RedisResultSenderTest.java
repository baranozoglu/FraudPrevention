package com.demo.ing.strategy.sender;

import com.demo.ing.result.Result;
import com.demo.ing.result.ResultEnum;
import com.demo.ing.result.ResultService;
import com.demo.ing.strategy.ResultFormatterStrategy;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static com.demo.ing.TestHelper.getResult;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RedisResultSenderTest {

    @Mock
    private ResultService resultService;

    @Mock
    private ResultFormatterStrategy resultFormatterStrategy;

    @InjectMocks
    private RedisResultSender redisResultSender;


    @Test
    void testSendResultWhenCalledThenFormatAndInsertResult() {
        Result result = getResult(123L, BigDecimal.ONE, BigDecimal.ONE, ResultEnum.INCOME_AND_STORED_AMOUNT_EQUAL);

        String formattedResult = "formattedResult";
        when(resultFormatterStrategy.format(result)).thenReturn(formattedResult);

        redisResultSender.sendResult(resultFormatterStrategy, result);

        verify(resultFormatterStrategy, times(1)).format(result);
        verify(resultService, times(1)).insertResult(result.getTransactionId(), formattedResult);
    }

}
package com.demo.ing.strategy.sender;

import com.demo.ing.result.Result;
import com.demo.ing.result.ResultService;
import com.demo.ing.strategy.ResultFormatterStrategy;
import com.demo.ing.strategy.ResultSenderStrategy;
import org.springframework.stereotype.Component;

@Component("redisResultSender")
public class RedisResultSender implements ResultSenderStrategy {
    private final ResultService resultService;

    public RedisResultSender(ResultService resultService) {
        this.resultService = resultService;
    }

    @Override
    public void sendResult(ResultFormatterStrategy resultFormatterStrategy, Result result) {
        String formatedResultString = resultFormatterStrategy.format(result);
        resultService.insertResult(result.getTransactionId(), formatedResultString);
    }

}

package com.demo.ing.transaction;

import com.demo.ing.result.Result;
import com.demo.ing.result.ResultService;
import com.demo.ing.strategy.ResultFormatterStrategy;
import com.demo.ing.strategy.ResultSenderStrategy;
import com.demo.ing.transaction.model.IncomeTransaction;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TransactionEventHandler {
    private final ResultService resultService;
    private final ResultSenderStrategy kafkaResultSender;
    private final ResultSenderStrategy redisResultSender;
    private final ResultFormatterStrategy jsonResultFormatter;

    public TransactionEventHandler(ResultService resultService,
                                   @Qualifier("kafkaResultSender") ResultSenderStrategy kafkaResultSender,
                                   @Qualifier("redisResultSender") ResultSenderStrategy redisResultSender,
                                   @Qualifier("jsonResultFormatter") ResultFormatterStrategy jsonResultFormatter) {
        this.resultService = resultService;
        this.kafkaResultSender = kafkaResultSender;
        this.redisResultSender = redisResultSender;
        this.jsonResultFormatter = jsonResultFormatter;
    }


    public void handler(List<IncomeTransaction> incomeTransactionList) {
        for (IncomeTransaction incomeTransaction : incomeTransactionList) {
            Result result = resultService.determineResult(incomeTransaction);
            kafkaResultSender.sendResult(jsonResultFormatter, result);
            redisResultSender.sendResult(jsonResultFormatter, result);
        }
    }

}

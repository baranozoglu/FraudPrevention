package com.demo.ing.strategy.sender;

import com.demo.ing.result.Result;
import com.demo.ing.strategy.ResultFormatterStrategy;
import com.demo.ing.strategy.ResultSenderStrategy;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static com.demo.ing.common.KafkaProperties.RESULT_TOPIC;

@Component("kafkaResultSender")
public class KafkaResultSender implements ResultSenderStrategy {
    private KafkaTemplate<String, String> kafkaTemplate;

    public KafkaResultSender(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendResult(ResultFormatterStrategy resultFormatterStrategy, Result result) {
        String formatedResultString = resultFormatterStrategy.format(result);
        kafkaTemplate.send(RESULT_TOPIC, formatedResultString);
    }
}

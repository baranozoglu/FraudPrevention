package com.demo.ing.transaction.service;

import com.demo.ing.transaction.model.IncomeTransaction;
import com.google.gson.Gson;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.demo.ing.common.KafkaProperties.TRANSACTION_TOPIC;

@Service
public class IncomeTransactionService {
    private KafkaTemplate<String, String> kafkaTemplate;

    public IncomeTransactionService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishIncomeTransaction(List<IncomeTransaction> transactions) {
        String incomeTransactionJsonString = convertToString(transactions);
        kafkaTemplate.send(TRANSACTION_TOPIC, incomeTransactionJsonString);
    }

    private String convertToString(List<IncomeTransaction> transactions) {
        Gson gson = new Gson();
        return gson.toJson(transactions);
    }
}

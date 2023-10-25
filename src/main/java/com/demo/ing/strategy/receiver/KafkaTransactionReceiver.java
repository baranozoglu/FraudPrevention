package com.demo.ing.strategy.receiver;

import com.demo.ing.exception.KafkaInputCouldNotReadException;
import com.demo.ing.strategy.TransactionReceiverStrategy;
import com.demo.ing.transaction.TransactionEventHandler;
import com.demo.ing.transaction.model.IncomeTransaction;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.demo.ing.common.KafkaProperties.GROUP_ID;
import static com.demo.ing.common.KafkaProperties.TRANSACTION_TOPIC;

@Component("kafkaTransactionReceiver")
public class KafkaTransactionReceiver implements TransactionReceiverStrategy {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private static final String SQUARE_BRACKET = "[";
    private static final String COULD_NOT_READ_ERROR_MESSAGE = "Input from kafka topic could not read!";
    private final TransactionEventHandler transactionEventHandler;


    public KafkaTransactionReceiver(TransactionEventHandler transactionEventHandler) {
        this.transactionEventHandler = transactionEventHandler;
    }

    @Override
    @KafkaListener(topics = TRANSACTION_TOPIC, groupId = GROUP_ID)
    public void receiveTransactions(String data) {
        List<IncomeTransaction> incomeTransactions = new ArrayList<>();
        Gson g = new Gson();
        try {
            if (isArray(data)) {
                mapEachArrayItemToTransactionAndAddList(data, incomeTransactions, g);
            } else {
                mapItemToTransactionAndAddList(data, incomeTransactions, g);
            }
        } catch (Exception e) {
            LOGGER.error(COULD_NOT_READ_ERROR_MESSAGE, e);
            throw new KafkaInputCouldNotReadException(COULD_NOT_READ_ERROR_MESSAGE);
        }
        transactionEventHandler.handler(incomeTransactions);
    }

    private void mapItemToTransactionAndAddList(String data, List<IncomeTransaction> incomeTransactions, Gson g) {
        IncomeTransaction transaction = g.fromJson(data, IncomeTransaction.class);
        incomeTransactions.add(transaction);
    }

    private void mapEachArrayItemToTransactionAndAddList(String data, List<IncomeTransaction> incomeTransactions, Gson g) {
        JSONArray jsonArray = new JSONArray(data);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            mapItemToTransactionAndAddList(object.toString(), incomeTransactions, g);
        }
    }

    private boolean isArray(String data) {
        return SQUARE_BRACKET.equals(data.substring(0, 1));
    }
}


package com.demo.ing.strategy.sender;

import com.demo.ing.result.Result;
import com.demo.ing.strategy.ResultFormatterStrategy;
import com.demo.ing.strategy.ResultSenderStrategy;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component("apiResultSender")
public class ApiResultSender implements ResultSenderStrategy {
    private static final String API_URL = "https://example.com/api/sendResult";

    @Override
    public void sendResult(ResultFormatterStrategy resultFormatterStrategy, Result result) {
        String formatedResultString = resultFormatterStrategy.format(result);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForEntity(API_URL, formatedResultString, String.class);
    }

}
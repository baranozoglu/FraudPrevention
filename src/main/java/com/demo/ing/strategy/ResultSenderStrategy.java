package com.demo.ing.strategy;

import com.demo.ing.result.Result;

public interface ResultSenderStrategy {
    void sendResult(ResultFormatterStrategy resultFormatterStrategy, Result result);
}

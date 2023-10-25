package com.demo.ing.strategy;

import com.demo.ing.result.Result;

public interface ResultFormatterStrategy {
    String format(Result result);
}

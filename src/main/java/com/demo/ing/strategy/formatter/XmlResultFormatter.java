package com.demo.ing.strategy.formatter;

import com.demo.ing.result.Result;
import com.demo.ing.strategy.ResultFormatterStrategy;
import org.springframework.stereotype.Component;

@Component("xmlResultFormatter")
public class XmlResultFormatter implements ResultFormatterStrategy {

    @Override
    public String format(Result result) {
        return "please implement xml formatter";
    }
}

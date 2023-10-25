package com.demo.ing.strategy.formatter;

import com.demo.ing.exception.ResultCouldNotConvertException;
import com.demo.ing.result.Result;
import com.demo.ing.strategy.ResultFormatterStrategy;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("jsonResultFormatter")
public class JsonResultFormatter implements ResultFormatterStrategy {
    private static final String COULD_NOT_CONVERT_ERROR_MESSAGE = "Result object could not convert to json string!";
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public String format(Result result) {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = "";
        try {
            json = ow.writeValueAsString(result);
        } catch (JsonProcessingException e) {
            LOGGER.error(COULD_NOT_CONVERT_ERROR_MESSAGE, e);
            throw new ResultCouldNotConvertException(COULD_NOT_CONVERT_ERROR_MESSAGE);
        }
        return json;
    }
}

package com.demo.ing.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class ResultCouldNotConvertException extends RuntimeException {
    public ResultCouldNotConvertException(String message) {
        super(message);
    }
}

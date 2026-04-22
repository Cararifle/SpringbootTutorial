package com.ccsw.tutorial.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateClientException extends RuntimeException {

    public DuplicateClientException(String message) {
        super(message);
    }
}

package com.ccsw.tutorial.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessConflictException.class)
    public ResponseEntity<?> handleConflict(BusinessConflictException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("errorCode", ex.getErrorCode(), "message", ex.getMessage(), "field", ex.getField()));
    }

}

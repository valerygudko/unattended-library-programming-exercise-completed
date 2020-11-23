package com.sky.library.exception;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@PropertySource("classpath:errorMessages.properties")
public class GlobalExceptionHandler {

    private final static String ERROR = "error";

    @Value("${validation.book.reference}")
    private String validationBookReference;

    @Value("${not.found.book.reference}")
    private String notFoundBookReference;

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleBookNotFoundException() {
        Map<String, Object> response = new HashMap<>();

        response.put(ERROR, notFoundBookReference);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException() {
        Map<String, Object> response = new HashMap<>();

        response.put(ERROR, validationBookReference);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}

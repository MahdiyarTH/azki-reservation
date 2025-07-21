package com.azki.common.exception;

import com.azki.common.exception.model.ApiException;
import com.azki.common.model.HttpResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @ResponseBody
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException e) throws JsonProcessingException {
        return createResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ResponseBody
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<?> handleException(ApiException e) throws JsonProcessingException {
        return createResponseEntity(e.getMessage(), e.getHttpStatus());
    }

    private ResponseEntity<?> createResponseEntity(String message, HttpStatus httpStatus) throws JsonProcessingException {
        return ResponseEntity
                .status(httpStatus)
                .body(
                        HttpResponse.builder()
                                .message(message)
                                .timestamp(new Date())
                                .build()
                );
    }

}

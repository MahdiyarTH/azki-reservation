package com.azki.common.exception;

import com.azki.common.exception.model.ApiException;
import com.azki.common.model.HttpResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseBody
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException e) throws JsonProcessingException {
        return createResponseEntity(
                ApiException.builder()
                        .message(e.getMessage())
                        .httpStatus(HttpStatus.UNAUTHORIZED)
                        .build()
        );
    }

    @ResponseBody
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<?> handleException(ApiException e) throws JsonProcessingException {
        log.error(e.getMessage(), e);
        return createResponseEntity(e);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> body = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach((error) -> {
            body.put(error.getField(), error.getDefaultMessage());
        });

        return createResponseEntity(
                ApiException.builder()
                        .message("Bad request")
                        .httpStatus(HttpStatus.BAD_GATEWAY)
                        .extraParams(body)
                        .build()
        );
    }

    private ResponseEntity<Object> createResponseEntity(ApiException apiException) {
        return ResponseEntity
                .status(apiException.getHttpStatus())
                .body(
                        HttpResponse.builder()
                                .message(apiException.getMessage())
                                .timestamp(new Date())
                                .data(apiException.getExtraParams())
                                .build()
                );
    }

}

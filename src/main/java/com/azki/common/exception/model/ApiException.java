package com.azki.common.exception.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class ApiException extends RuntimeException {

    @Builder.Default
    private final HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    @Builder.Default
    private final String message = "Internal server exception";

}

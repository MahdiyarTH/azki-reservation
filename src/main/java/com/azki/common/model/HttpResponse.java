package com.azki.common.model;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Builder
@Getter
public class HttpResponse<T> {

    private final T data;

    private final String message;

    private final boolean success;

    private final Date timestamp;

    public static <T> HttpResponse<T> success() {
        return success(null);
    }

    public static <T> HttpResponse<T> success(T data) {
        return HttpResponse.<T>builder()
                .data(data)
                .success(true)
                .message("Operation successful")
                .timestamp(new Date())
                .build();
    }

    public static <T> HttpResponse<T> failure() {
        return success(null);
    }

    public static <T> HttpResponse<T> failure(T data) {
        return HttpResponse.<T>builder()
                .data(data)
                .success(false)
                .message("Operation failed")
                .timestamp(new Date())
                .build();
    }

}

package com.example.blogtest.application.util;


import com.example.blogtest.application.common.exception.ErrorResponse;

import java.time.LocalDateTime;

public class ExceptionHandlerUtil {
    public static ErrorResponse createError(LocalDateTime timestamp, String message) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(timestamp)
                .message(message)
                .build();

        return errorResponse;
    }
}

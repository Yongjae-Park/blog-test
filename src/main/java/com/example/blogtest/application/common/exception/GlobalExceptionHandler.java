package com.example.blogtest.application.common.exception;

import com.example.blogtest.application.common.exception.blog.PageSizeValueOutOfBoundsException;
import com.example.blogtest.application.common.exception.blog.PageValueOutOfBoundsException;
import com.example.blogtest.application.util.ExceptionHandlerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(URISyntaxException.class)
    public final ResponseEntity<ErrorResponse> URISyntaxExceptionHandler(URISyntaxException exception) {
        String message = exception.getMessage();
        ErrorResponse errorResponse = ExceptionHandlerUtil.createError(LocalDateTime.now(), message);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(PageSizeValueOutOfBoundsException.class)
    public final ResponseEntity<ErrorResponse> PageSizeValueOutOfBoundsExceptionHandler(PageSizeValueOutOfBoundsException exception) {
        String message = exception.getMessage();
        ErrorResponse errorResponse = ExceptionHandlerUtil.createError(LocalDateTime.now(), message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(PageValueOutOfBoundsException.class)
    public final ResponseEntity<ErrorResponse> PageValueOutOfBoundsExceptionHandler(PageValueOutOfBoundsException exception) {
        String message = exception.getMessage();
        ErrorResponse errorResponse = ExceptionHandlerUtil.createError(LocalDateTime.now(), message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public final ResponseEntity<ErrorResponse> MissingServletRequestParameterExceptionHandler(MissingServletRequestParameterException exception) {
        String message = exception.getParameterName() + "값은 null일 수 없습니다.";
        ErrorResponse errorResponse = ExceptionHandlerUtil.createError(LocalDateTime.now(), message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public final ResponseEntity<ErrorResponse> ConstraintViolationExceptionHandler(ConstraintViolationException exception) {
        String message = exception.getMessage();
        ErrorResponse errorResponse = ExceptionHandlerUtil.createError(LocalDateTime.now(), message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

}

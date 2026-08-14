package com.sonumangal.tech.exception;

import com.sonumangal.tech.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(RateLimitException.class)
    public ResponseEntity<ErrorResponse> handleRateLimitException(RateLimitException exception) {
        ErrorResponse response = new ErrorResponse(
                "RATE_LIMIT_EXCEEDED",
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.TOO_MANY_REQUESTS)
                .body(response);
    }
}

package com.sonumangal.tech.exception;

public class RateLimitException extends RuntimeException {

    RateLimitException() {
        super("Rate limit exceed");
    }

    public RateLimitException(String message) {
        super(message);
    }

}

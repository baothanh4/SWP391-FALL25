package com.example.SWP391_FALL25.ExceptionHandler;

public class OtpExpiredException extends RuntimeException {
    public OtpExpiredException(String message) {
        super(message);
    }
}

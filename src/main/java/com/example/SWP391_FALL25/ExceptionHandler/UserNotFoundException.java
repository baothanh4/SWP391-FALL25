package com.example.SWP391_FALL25.ExceptionHandler;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}

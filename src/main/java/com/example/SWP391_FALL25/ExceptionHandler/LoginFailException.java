package com.example.SWP391_FALL25.ExceptionHandler;

public class LoginFailException extends RuntimeException {
  public LoginFailException(String message) {
        super(message);
    }
  public LoginFailException() {
    super("Invalid phone number or password");
  }
}

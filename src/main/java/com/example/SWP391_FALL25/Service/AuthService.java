package com.example.SWP391_FALL25.Service;

import com.example.SWP391_FALL25.DTO.Auth.LoginRequest;
import com.example.SWP391_FALL25.DTO.Auth.LoginResponse;
import com.example.SWP391_FALL25.DTO.Auth.RegisterRequest;

public interface AuthService {

    LoginResponse login(LoginRequest request);

    LoginResponse register(RegisterRequest request);
}

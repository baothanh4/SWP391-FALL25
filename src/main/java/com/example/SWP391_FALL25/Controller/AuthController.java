package com.example.SWP391_FALL25.Controller;

import com.example.SWP391_FALL25.DTO.Auth.LoginRequest;
import com.example.SWP391_FALL25.DTO.Auth.LoginResponse;
import com.example.SWP391_FALL25.DTO.Auth.RegisterRequest;
import com.example.SWP391_FALL25.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request){
        return authService.login(request);
    }

    @PostMapping("/register")
    public LoginResponse register(@RequestBody RegisterRequest request){
        return authService.register(request);
    }
}

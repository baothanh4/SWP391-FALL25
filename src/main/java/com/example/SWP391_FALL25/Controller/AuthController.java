package com.example.SWP391_FALL25.Controller;

import com.example.SWP391_FALL25.DTO.Auth.LoginRequest;
import com.example.SWP391_FALL25.DTO.Auth.LoginResponse;
import com.example.SWP391_FALL25.DTO.Auth.RegisterRequest;
import com.example.SWP391_FALL25.DTO.Auth.UsersDTO;
import com.example.SWP391_FALL25.Entity.Users;
import com.example.SWP391_FALL25.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping("/all-account")
    public List<Users> getAllAccount(){
        return authService.getAll();
    }

    @GetMapping("/profile/{id}")
    public UsersDTO getProfile(@PathVariable("id") Long id){ return authService.getAccountById(id); }
}

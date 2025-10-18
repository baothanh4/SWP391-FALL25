package com.example.SWP391_FALL25.Service;

import com.example.SWP391_FALL25.DTO.Auth.LoginRequest;
import com.example.SWP391_FALL25.DTO.Auth.LoginResponse;
import com.example.SWP391_FALL25.DTO.Auth.RegisterRequest;
import com.example.SWP391_FALL25.DTO.Auth.UsersDTO;
import com.example.SWP391_FALL25.Entity.Users;

import java.util.List;

public interface AuthService {

    LoginResponse login(LoginRequest request);

    LoginResponse register(RegisterRequest request);

    List<Users> getAll();


    UsersDTO getAccountById(Long id);
}

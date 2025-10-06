package com.example.SWP391_FALL25.DTO.Auth;

import lombok.Data;

@Data
public class LoginRequest {
    private String phone;
    private String password;
}

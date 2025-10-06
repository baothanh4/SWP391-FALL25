package com.example.SWP391_FALL25.DTO.Auth;

import lombok.Data;

@Data
public class RegisterRequest {
    private String phone;
    private String password;
    private String fullname;
    private String email;
    private String role;
    private String certificate;
}

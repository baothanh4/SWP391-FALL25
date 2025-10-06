package com.example.SWP391_FALL25.DTO.Auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private Long id;
    private String phone;
    private String fullname;
    private String email;
    private String role;
    private String certificate;
    private double rating;
    private String token;
}

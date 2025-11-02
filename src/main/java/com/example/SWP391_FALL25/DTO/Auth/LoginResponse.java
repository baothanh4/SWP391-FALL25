package com.example.SWP391_FALL25.DTO.Auth;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class LoginResponse {
    private Long id;
    private String phone;
    private String fullname;
    private String email;
    private String role;
    private String address;
    private LocalDate dob;
    private String accessToken;
    private String refreshToken;
}

package com.example.SWP391_FALL25.DTO.Auth;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RegisterRequest {
    private String phone;
    private String password;
    private String fullname;
    private String email;
    private String role;
    private String address;
    private LocalDate dob;
}

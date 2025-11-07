package com.example.SWP391_FALL25.DTO.Auth;
import lombok.Data;
import java.time.LocalDate;

@Data
public class UpdateUserProfileRequest {
    private String fullname;
    private String email;
    private String address;
    private LocalDate dob;
    private String password;
}

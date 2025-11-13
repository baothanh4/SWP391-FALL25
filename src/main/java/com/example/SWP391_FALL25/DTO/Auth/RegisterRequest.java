package com.example.SWP391_FALL25.DTO.Auth;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class RegisterRequest {

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "\\d{9,15}", message = "Phone number must be 9-15 digits")
    private String phone;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 50, message = "Password must be 6-50 characters")
    private String password;

    @NotBlank(message = "Full name is required")
    @Size(max = 255, message = "Full name must be at most 255 characters")
    private String fullname;

    @Email(message = "Email should be valid")
    @Size(max = 255, message = "Email must be at most 255 characters")
    private String email;

    @NotBlank(message = "Role is required")
    @Pattern(regexp = "CUSTOMER|TECHNICIAN|STAFF|ADMIN", message = "Role must be CUSTOMER, TECHNICIAN, STAFF, or ADMIN")
    private String role;

    @Size(max = 255, message = "Address must be at most 255 characters")
    private String address;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dob;
}

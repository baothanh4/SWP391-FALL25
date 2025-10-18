package com.example.SWP391_FALL25.DTO.Auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsersDTO {
    private Long id;
    private String phone;
    private String fullname;
    private String email;
    private String role;
    private String address;
    private LocalDate dob;
    private Set<VehicleDTO> vehicles;
}

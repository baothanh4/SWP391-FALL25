package com.example.SWP391_FALL25.Entity;

import com.example.SWP391_FALL25.Enum.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "\\d{9,15}", message = "Phone number must be 9-15 digits")
    @Column(nullable = false, unique = true)
    private String phone;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @NotBlank(message = "Full name is required")
    @Size(max = 255, message = "Full name must be at most 255 characters")
    @Column(nullable = false)
    private String fullname;

    @Email(message = "Email should be valid")
    @Size(max = 255, message = "Email must be at most 255 characters")
    @Column(unique = true)
    private String email;

    @NotNull(message = "Role is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Size(max = 255, message = "Address must be at most 255 characters")
    private String address;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dob;

    private boolean accountLocked = false;

    private int failAttempts = 0;

    private LocalDateTime lockTime;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Vehicle> vehicles = new ArrayList<>();
}

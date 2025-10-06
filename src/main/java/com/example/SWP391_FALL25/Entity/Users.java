package com.example.SWP391_FALL25.Entity;


import com.example.SWP391_FALL25.Enum.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Table(name = "Users")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String phone;
    private String password;
    private String fullname;
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String certificate;
    private double rating;

}

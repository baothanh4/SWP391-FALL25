package com.example.SWP391_FALL25.Service;

import com.example.SWP391_FALL25.Config.JwtTokenProvider;
import com.example.SWP391_FALL25.DTO.Auth.LoginRequest;
import com.example.SWP391_FALL25.DTO.Auth.LoginResponse;
import com.example.SWP391_FALL25.DTO.Auth.RegisterRequest;
import com.example.SWP391_FALL25.Entity.Users;
import com.example.SWP391_FALL25.Enum.Role;
import com.example.SWP391_FALL25.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public LoginResponse login(LoginRequest request){
        Users users=userRepository.findByPhone(request.getPhone()).orElseThrow(()->new RuntimeException("User not found"));

        if(!passwordEncoder.matches(request.getPassword(), users.getPassword())){
            throw new RuntimeException("Invalid password");
        }

        String token=jwtTokenProvider.generateToken(users.getPhone(), String.valueOf(users.getRole()));

        return new LoginResponse(users.getId(),
                users.getPhone(),
                users.getFullname(),
                users.getEmail(),
                users.getRole().name(),
                users.getCertificate(),
                users.getRating(),
                token);
    }

    @Override
    public LoginResponse register(RegisterRequest request){
        if(userRepository.findByPhone(request.getPhone()).isPresent()){
            throw new RuntimeException("Phone number already exists");
        }

        Users users=new Users();
        users.setPhone(request.getPhone());
        users.setPassword(passwordEncoder.encode(request.getPassword()));
        users.setFullname(request.getFullname());
        users.setEmail(request.getEmail());
        users.setCertificate(request.getCertificate());
        users.setRating(0.0);

        Role role=Role.valueOf(request.getRole()!=null ? request.getRole().toUpperCase():"CUSTOMER");
        users.setRole(role);

        userRepository.save(users);

        String token=jwtTokenProvider.generateToken(users.getPhone(), users.getRole().name());

        return new LoginResponse(users.getId(),
                users.getPhone(),
                users.getFullname(),
                users.getEmail(),
                users.getRole().name(),
                users.getCertificate(),
                users.getRating(),
                token);
    }
}

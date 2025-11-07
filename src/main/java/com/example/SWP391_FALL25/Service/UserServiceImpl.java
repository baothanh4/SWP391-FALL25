package com.example.SWP391_FALL25.Service;

import com.example.SWP391_FALL25.DTO.Auth.UpdateUserProfileRequest;
import com.example.SWP391_FALL25.Entity.Users;
import com.example.SWP391_FALL25.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    @Transactional
    public Users updateProfile(Long userId, UpdateUserProfileRequest request) {

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getFullname() != null && !request.getFullname().isEmpty()) {
            user.setFullname(request.getFullname());
        }
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            user.setEmail(request.getEmail());
        }
        if (request.getAddress() != null && !request.getAddress().isEmpty()) {
            user.setAddress(request.getAddress());
        }
        if (request.getDob() != null) {
            user.setDob(request.getDob());
        }
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        return userRepository.save(user);
    }
}

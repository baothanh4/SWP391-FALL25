package com.example.SWP391_FALL25.Service;

import com.example.SWP391_FALL25.DTO.Auth.UpdateUserProfileRequest;
import com.example.SWP391_FALL25.Entity.Users;

public interface UserService {
    Users updateProfile(Long userId, UpdateUserProfileRequest request);

}

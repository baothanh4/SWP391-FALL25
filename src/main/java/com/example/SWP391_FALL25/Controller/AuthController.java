package com.example.SWP391_FALL25.Controller;

import com.example.SWP391_FALL25.DTO.Auth.*;
import com.example.SWP391_FALL25.Entity.Users;
import com.example.SWP391_FALL25.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request){
        return authService.login(request);
    }

    @PostMapping("/register")
    public LoginResponse register(@RequestBody RegisterRequest request){
        return authService.register(request);
    }
    @GetMapping("/all-account")
    public List<Users> getAllAccount(){
        return authService.getAll();
    }

    @GetMapping("/profile/{id}")
    public UsersDTO getProfile(@PathVariable("id") Long id){ return authService.getAccountById(id); }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request){
        authService.sendOtpToEmail(request.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyOtpPassword request){
        boolean valid =authService.verifyOtp(request.getEmail(), request.getOtp());
        return valid?ResponseEntity.ok("OTP hop le"):ResponseEntity.badRequest().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request){
        boolean success=authService.resetPassword(request.getEmail(), request.getOtp(), request.getNewPassword());
        return success?ResponseEntity.ok("Doi mat khau thanh cong"):ResponseEntity.badRequest().build();
    }
}

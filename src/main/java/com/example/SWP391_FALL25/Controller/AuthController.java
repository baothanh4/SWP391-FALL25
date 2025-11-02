package com.example.SWP391_FALL25.Controller;

import com.example.SWP391_FALL25.Config.JwtTokenProvider;
import com.example.SWP391_FALL25.DTO.Auth.*;
import com.example.SWP391_FALL25.Entity.Users;
import com.example.SWP391_FALL25.Repository.UserRepository;
import com.example.SWP391_FALL25.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserRepository userRepository;

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

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String,String> body){
        String refreshToken = body.get("refreshToken");

        if(jwtTokenProvider.validateToken(refreshToken,"refresh")){
            String phone= jwtTokenProvider.getPhoneFromToken(refreshToken);
            Users user=userRepository.findByPhone(phone).orElseThrow(()->new RuntimeException("User not found"));

            String newAccessToken= jwtTokenProvider.generateAccessToken(phone,user.getRole().name());
            return ResponseEntity.ok(Map.of("accessToken",newAccessToken));

        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("Error","Invalid or expired token"));
        }
    }
}

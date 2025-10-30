package com.example.SWP391_FALL25.Service;

import com.example.SWP391_FALL25.Config.JwtTokenProvider;
import com.example.SWP391_FALL25.DTO.Auth.*;
import com.example.SWP391_FALL25.Entity.OtpToken;
import com.example.SWP391_FALL25.Entity.SystemLog;
import com.example.SWP391_FALL25.Entity.Users;
import com.example.SWP391_FALL25.Enum.Role;
import com.example.SWP391_FALL25.Repository.OtpTokenRepository;
import com.example.SWP391_FALL25.Repository.SystemLogRepository;
import com.example.SWP391_FALL25.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private EmailService emailService;

    @Autowired
    private OtpTokenRepository otpTokenRepository;

    @Autowired
    private JavaMailSender mailSender;
    private final BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();

    private final int OTP_EXPIRY_MINUTES = 15;

    @Autowired
    private SystemLogService systemLogService;

    @Override
    public LoginResponse login(LoginRequest request){
        Users users=userRepository.findByPhone(request.getPhone()).orElseThrow(()->new RuntimeException("User not found"));

        if(!passwordEncoder.matches(request.getPassword(), users.getPassword())){
            throw new RuntimeException("Invalid password");
        }

        String token=jwtTokenProvider.generateToken(users.getPhone(), String.valueOf(users.getRole()));
        systemLogService.log(users.getId(),"LOGIN");
        return new LoginResponse(users.getId(),
                users.getPhone(),
                users.getFullname(),
                users.getEmail(),
                users.getRole().name(),
                users.getAddress(),
                users.getDob(),
                token);
    }

    @Override
    public LoginResponse register(RegisterRequest request){
        if(userRepository.findByPhone(request.getPhone()).isPresent()){
            throw new RuntimeException("Phone number already exists");
        }

        Users users = new Users();
        users.setPhone(request.getPhone());
        users.setPassword(passwordEncoder.encode(request.getPassword()));
        users.setFullname(request.getFullname());
        users.setEmail(request.getEmail());
        users.setAddress(request.getAddress());
        users.setDob(request.getDob());
        Role role = Role.valueOf(request.getRole() != null ? request.getRole().toUpperCase() : "CUSTOMER");
        users.setRole(role);

        userRepository.save(users);


        String subject = "Đăng ký tài khoản thành công";
        String body = "Xin chào " + users.getFullname() + ",\n\n"
                + "Chúc mừng bạn đã đăng ký tài khoản thành công trên hệ thống của chúng tôi!\n"
                + "Thông tin tài khoản:\n"
                + "📱 Số điện thoại: " + users.getPhone() + "\n"
                + "📧 Email: " + users.getEmail() + "\n\n"
                + "Chúc bạn có trải nghiệm tuyệt vời.\n\n"
                + "Trân trọng,\nĐội ngũ hỗ trợ khách hàng.";

        try {
            emailService.sendEmail(users.getEmail(), subject, body);
        } catch (Exception e) {
            System.err.println("❌ Gửi email thất bại: " + e.getMessage());
        }

        String token = jwtTokenProvider.generateToken(users.getPhone(), users.getRole().name());
        systemLogService.log(users.getId(),"REGISTER");
        return new LoginResponse(
                users.getId(),
                users.getPhone(),
                users.getFullname(),
                users.getEmail(),
                users.getRole().name(),
                users.getAddress(),
                users.getDob(),
                token
        );
    }

    @Override
    public List<Users> getAll(){
        return userRepository.findAll();
    }

    @Override
    public UsersDTO getAccountById(Long id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Set<VehicleDTO> vehicleDTOs = user.getVehicles().stream()
                .map(v -> new VehicleDTO(
                        v.getId(),
                        v.getVin(),
                        v.getLicensePlate(),
                        v.getBrand(),
                        v.getModel(),
                        v.getYear(),
                        v.getOdometer()
                ))
                .collect(Collectors.toSet());

        UsersDTO dto = new UsersDTO(
                user.getId(),
                user.getPhone(),
                user.getFullname(),
                user.getEmail(),
                user.getRole().name(),
                user.getAddress(),
                user.getDob(),
                vehicleDTOs
        );
        return dto;
    }

    private String generateOtp(){
        return String.valueOf(100000+new Random().nextInt(900000));
    }

    @Override
    public void sendOtpToEmail(String email){
        String otp=generateOtp();

        OtpToken otpToken=OtpToken.builder().email(email).code(otp).createdAt(LocalDateTime.now()).exprireAt(LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES)).used(false).build();

        otpTokenRepository.save(otpToken);

        sendEmail(email,otp);

    }

    private void sendEmail(String email,String otp){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Mã xác minh OTP:");
        message.setText("Xin chào,\n\n Mã OTP của bạn là "+otp+"\n Mã này có hiệu lực trong 15 phút");
        mailSender.send(message);
    }

    @Override
    public boolean verifyOtp(String email, String otp){
        return otpTokenRepository.findFirstByEmailAndCodeAndUsedFalse(email,otp).filter(token ->token.getExprireAt().isAfter(LocalDateTime.now())).isPresent();
    }


    @Override
    public boolean resetPassword(String email, String otp, String newPassword){
        var optionalToken=otpTokenRepository.findFirstByEmailAndCodeAndUsedFalse(email,otp);
        if(optionalToken.isEmpty()){
            return false;
        }

        OtpToken token = optionalToken.get();
        if(token.getExprireAt().isBefore(LocalDateTime.now())){
            return false;
        }

        var optionalUser=userRepository.findByEmail(email);
        if(optionalUser.isEmpty()){
            return false;
        }

        Users user=optionalUser.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        token.setUsed(true);
        otpTokenRepository.save(token);
        return true;
    }


}

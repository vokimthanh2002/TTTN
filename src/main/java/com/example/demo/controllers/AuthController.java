package com.example.demo.controllers;

import com.example.demo.dto.UserDto;
import com.example.demo.entity.User;
import com.example.demo.services.EmailService;
import com.example.demo.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/api/auth")
public class AuthController {

    private String currentOtp; // Lưu trữ mã OTP hiện tại tạm thời
    private String currentEmail;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, HttpSession session) {
        if (userService.authenticate(email, password)) {
            User user = userService.findByEmail(email);
            session.setAttribute("user_login",user);
            return "Login successful!";
        } else {
            return "Invalid email or password!";
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDto userDto, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.OK);
        }
        // Kiểm tra xem email đã được sử dụng chưa
        if (userService.existsByEmail(userDto.getEmail())) {
            return ResponseEntity.ok("Email exist.");
        }
        // Validate userDto (username, email, password)
        User user = userService.registerUser(userDto);

        String confirmationUrl = "http://" + request.getServerName() + ":" + request.getServerPort() +
                "/api/auth/verify-email?token=" + user.getVerificationToken();

        // Gửi email xác minh
        String subject = "Xác minh email";
        try {
            emailService.sendCustomEmail(user.getEmail(), subject, userDto.getUsername(), confirmationUrl);
            return ResponseEntity.ok("User registered successfully. Please verify your email.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Failed to send email for verification.");
        }
    }

    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam("token") String token) {
        boolean verified = userService.verifyEmail(token);
        if (verified) {
            return "veryfi/veryfi-success";
        } else {
            return "veryfi/verified";
        }
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam String email) {
        // Tạo mã OTP mới
        currentOtp = emailService.generateOtp();
        currentEmail = email;
        // Gửi mã OTP đến email của người dùng
        String subject = "Password Reset OTP";
        String body = "Your OTP to reset password is: " + currentOtp;
        emailService.sendEmail(email, subject, body);

        return "OTP sent to your email. Please check and use it to reset your password.";
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> verifyOtp(@RequestParam String otp, String newPassword, String confirmPassword) {
        if (currentOtp != null && currentOtp.equals(otp)) {
            User user = userService.findByEmail(currentEmail);
            if(newPassword.equals(confirmPassword)){
                user.setPassword(passwordEncoder.encode(newPassword));
                userService.save(user);
            }
            return ResponseEntity.ok("Doi mk thanh cong.");
        } else {
            // Nếu không trùng khớp, thông báo lỗi
            return ResponseEntity.ok("Otp khong chinh xac.");
        }
    }

}
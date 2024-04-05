package com.example.demo.services;

import com.example.demo.dto.UserDto;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(UserDto userDto) {
        // Validate userDto and create a new user
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        // Hash password before saving to database
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        // Set verification token
        user.setVerificationToken(UUID.randomUUID().toString());
        userRepository.save(user);
        return user;
    }
    public boolean verifyEmail(String token) {
        User user = userRepository.findByVerificationToken(token);
        if (user != null && !user.isVerified()) {
            user.setVerified(true);
            userRepository.save(user);
            return true;
        }
        return false;
    }
    public boolean existsByEmail(String email){
        if(userRepository.existsByEmail(email)){
            return  true;
        }
        return false;
    }
   public User findByEmail(String email){
       return userRepository.findByEmail(email);
   }
   public void save(User user){
        userRepository.save(user);
   }
    public boolean authenticate(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            // Kiểm tra mật khẩu
            return passwordEncoder.matches(password, user.getPassword());
        }
        return false;
    }
    public void addAdmin(UserDto userDto){
        // Validate userDto and create a new user
        User user = new User();
        user.setRole(new Role(1L, "Admin"));
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        // Hash password before saving to database
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(user);
    }
}

package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserDto {
    @NotBlank(message = "Vui lòng nhập tài khoản")
    @Pattern(regexp = "^[a-zA-Z0-9]{4,12}$", message = "Tài khoản không hợp lệ")
    @Size(min = 4, max = 12, message = "Tài khoản phải có độ dài từ 4 đến 12 ký tự")
    private String username;
    @NotBlank(message = "Vui lòng nhập email")
    @Pattern(regexp = "^[a-zA-Z0-9_.+-]+@gmail.com+$", message = "Email không đúng định dạng, vui lòng nhập lại. Ex: tên_email@gmail.com")
    @Size(min = 6, max = 30, message = "Tên email chỉ được phép chứa từ 6 đến 30 kí tự")
    private String email;
    @NotBlank(message = "Vui lòng nhập mật khẩu")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{6,32}$", message = "Mật khẩu không hợp lệ")
    @Size(min = 6, max = 32, message = "Mật khẩu phải có độ dài từ 6 đến 32 ký tự")
    private String password;

    // Constructors
    public UserDto() {}

    public UserDto(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

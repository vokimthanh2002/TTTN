package com.example.demo.controllers;

import com.example.demo.dto.UserDto;
import com.example.demo.entity.Category;
import com.example.demo.entity.User;
import com.example.demo.services.CategoryService;
import com.example.demo.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminCotroller {
    @Autowired
    private UserService userService;
    @Autowired
    CategoryService categoryService;

    @PostMapping(value = "/add-admin")
    public ResponseEntity<?> addAdmin(@Valid @RequestBody UserDto userDto, BindingResult bindingResult, HttpSession session) {
        User user = (User) session.getAttribute("user_login");
        if (user != null && user.getRole().getRoleId() == 1) {
            if (bindingResult.hasErrors()) {
                return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.OK);
            }
            // Kiểm tra xem email đã được sử dụng chưa
            if (userService.existsByEmail(userDto.getEmail())) {
                return ResponseEntity.ok("Email exist.");
            }
            userService.addAdmin(userDto);
            return ResponseEntity.ok("Create admin success.");
        }
        return ResponseEntity.ok("Truy cap trai phep.");
    }

    @GetMapping(value = "/list-category")
    public ResponseEntity<?> showListCategory(HttpSession session) {
//        User user = (User) session.getAttribute("user_login");
//        if(user!= null && user.getRole().getRoleId() == 1){
        List<Category> categories = categoryService.listCategory();
        return new ResponseEntity<>(categories, HttpStatus.OK);
//            }
//        return ResponseEntity.ok("Truy cap trai phep.");
    }

    @PostMapping(value = "/add-category")
    public ResponseEntity<?> addCategory(@RequestBody Category category) {
        try {
            categoryService.addCategory(category);
            return new ResponseEntity<>("Thêm category thành công", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Thêm thất bại", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/update-category")
    public ResponseEntity<?> updateCategory(@RequestBody Category category) {
        try {
            if (categoryService.findById(category.getCategoryId()) != null) {
                categoryService.updateCategory(category);
                return new ResponseEntity<>("update thành công", HttpStatus.CREATED);
            }else {
                return new ResponseEntity<>("Update thất bại", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Update thất bại", HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping(value = "/delete-category/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id){
        Category category = categoryService.findById(id);
        if(category != null){
            categoryService.deleteCategory(id);
            return new ResponseEntity<>("Xoa thành công", HttpStatus.OK);
        }
        return new ResponseEntity<>("Xoa that bai", HttpStatus.OK);
    }
}

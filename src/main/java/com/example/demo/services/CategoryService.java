package com.example.demo.services;

import com.example.demo.entity.Category;
import com.example.demo.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository repository;

    public void addCategory(Category category){
        repository.save(category);
    }
    public void updateCategory(Category category){
        repository.save(category);
    }
    public void deleteCategory(Long id){
        repository.deleteById(id);
    }
    public List<Category> listCategory(){
        return repository.findAll();
    }
    public  Category findById(Long id){
       return repository.findById(id).orElse(null);
    }
}

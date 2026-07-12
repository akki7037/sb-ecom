package com.ecommerce.project.controller;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.service.CategoryServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryServiceImpl categoryServiceImpl;

    @GetMapping("/public/categories")
    private ResponseEntity<List<Category>> getAllCategories(){
        List<Category> categories =  categoryServiceImpl.getCategories();
        return new ResponseEntity<>(categories,HttpStatus.OK);
    }

    @PostMapping("/public/categories")
    private ResponseEntity<String> createNewCategories(@Valid @RequestBody Category category){
        categoryServiceImpl.createNewCategory(category);
        return new ResponseEntity<>("Category added successfully", HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId){
        String status = categoryServiceImpl.deleteCategory(categoryId);
        return new ResponseEntity<>(status,HttpStatus.OK);
    }

    @PutMapping("/public/categories/{categoryId}")
    public ResponseEntity<String> updateCategory(@RequestBody Category category, @PathVariable Long categoryId){
        Category updatedCategory = categoryServiceImpl.updateCategory(category, categoryId);
        return new ResponseEntity<>("category with catergory id "+categoryId+" updated successfully", HttpStatus.OK);
    }
}

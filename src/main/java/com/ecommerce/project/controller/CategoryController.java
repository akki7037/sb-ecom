package com.ecommerce.project.controller;

import com.ecommerce.project.config.AppConstants;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponseDTO;

import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.service.CategoryServiceImpl;
import com.ecommerce.project.service.ProductServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryServiceImpl categoryServiceImpl;

    @GetMapping("/public/categories")
    private ResponseEntity<CategoryResponseDTO> getAllCategories(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber
    , @RequestParam (name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam (name ="sortBy", defaultValue = AppConstants.SORT_CATEGORY_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder",defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder){
        CategoryResponseDTO categoryResponseDTO =  categoryServiceImpl.getCategories(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(categoryResponseDTO,HttpStatus.OK);
    }

    @PostMapping("/public/categories")
    private ResponseEntity<CategoryDTO> createNewCategories(@Valid @RequestBody CategoryDTO categoryDTO){
        CategoryDTO categoryDTOCreated = categoryServiceImpl.createNewCategory(categoryDTO);
        return new ResponseEntity<>(categoryDTOCreated, HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long categoryId){
        CategoryDTO deletedCategory = categoryServiceImpl.deleteCategory(categoryId);
        return new ResponseEntity<>(deletedCategory,HttpStatus.OK);
    }

    @PutMapping("/public/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@RequestBody CategoryDTO categoryDTO, @PathVariable Long categoryId){
        CategoryDTO updatedCategory = categoryServiceImpl.updateCategory(categoryDTO, categoryId);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }
}

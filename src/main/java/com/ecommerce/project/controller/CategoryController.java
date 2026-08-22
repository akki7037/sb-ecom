package com.ecommerce.project.controller;

import com.ecommerce.project.config.AppConstants;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponseDTO;

import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.service.CategoryServiceImpl;
import com.ecommerce.project.service.ProductServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Category APIs", description = "All the APIs related to category")
@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryServiceImpl categoryServiceImpl;

    @Operation(summary = "Get all Categories", description = "Fetches all the categories available in the database")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "All categories fetched successfully!"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/public/categories")
    private ResponseEntity<CategoryResponseDTO> getAllCategories(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber
    , @RequestParam (name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam (name ="sortBy", defaultValue = AppConstants.SORT_CATEGORY_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder",defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder){
        CategoryResponseDTO categoryResponseDTO =  categoryServiceImpl.getCategories(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(categoryResponseDTO,HttpStatus.OK);
    }

    @Operation(summary = "Add new category", description = "Add new category in the database")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "New category added successfully"),
            @ApiResponse(responseCode = "404", description = "Category already exists in the database"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/public/categories")
    private ResponseEntity<CategoryDTO> createNewCategories(@Valid @RequestBody CategoryDTO categoryDTO){
        CategoryDTO categoryDTOCreated = categoryServiceImpl.createNewCategory(categoryDTO);
        return new ResponseEntity<>(categoryDTOCreated, HttpStatus.CREATED);
    }

    @Operation(summary = "Delete a category", description = "Delete a category from database using the category unique identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category removed successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long categoryId){
        CategoryDTO deletedCategory = categoryServiceImpl.deleteCategory(categoryId);
        return new ResponseEntity<>(deletedCategory,HttpStatus.OK);
    }

    @Operation(summary = "Update a category", description = "Update a category in database using the category unique identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category updated successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/public/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@RequestBody CategoryDTO categoryDTO, @PathVariable Long categoryId){
        CategoryDTO updatedCategory = categoryServiceImpl.updateCategory(categoryDTO, categoryId);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }
}

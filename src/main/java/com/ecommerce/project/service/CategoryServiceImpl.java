package com.ecommerce.project.service;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{
//    private List<Category> categories = new ArrayList<>();
//    private Long nextId = 1L;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getCategories(){
        return categoryRepository.findAll();
    }

    @Override
    public void createNewCategory(Category category){
//        category.setCategoryId(nextId);
//        nextId++;
        categoryRepository.save(category);
    }

    @Override
    public String deleteCategory(Long categoryId) {

        Category categoryToBeDeleted = categoryRepository.findById(categoryId).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found")
        );

//        List<Category> categories = categoryRepository.findAll();
//        Category categoryToBeDeleted = categories.stream().filter(x-> x.getCategoryId().equals(categoryId)).findFirst().
//                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Resource not found"));

        // another way of doing it
//        boolean deleted = categories.removeIf(category -> category.getCategoryId().equals(categoryId));
        categoryRepository.delete(categoryToBeDeleted);
        return "category removed successfully";
    }

    @Override
    public Category updateCategory(Category category, Long categoryId) {

        Category categoryToBeUpdated = categoryRepository.findById(categoryId).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found")
        );

//        List<Category> categories = categoryRepository.findAll();
//        Category categoryToBeUpdated = categories.stream().filter(x-> x.getCategoryId().equals(categoryId)).
//                findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "category not found"));

        categoryToBeUpdated.setCategoryName(category.getCategoryName());
        categoryRepository.save(categoryToBeUpdated);
        return categoryToBeUpdated;
    }
}

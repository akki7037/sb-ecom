package com.ecommerce.project.service;

import com.ecommerce.project.exception.APIException;
import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getCategories(){
        List<Category> allCategories= categoryRepository.findAll();
        if(allCategories.isEmpty())
            throw new APIException("No category created till now");
        return allCategories;
    }

    @Override
    public void createNewCategory(Category category){
        Category categoryToBeCreated = categoryRepository.findByCategoryName(category.getCategoryName());
        if(categoryToBeCreated != null)
            throw  new APIException("Category with the name "+category.getCategoryName()+" already exists!!!");

        categoryRepository.save(category);
    }

    @Override
    public String deleteCategory(Long categoryId) {

        Category categoryToBeDeleted = categoryRepository.findById(categoryId).orElseThrow(
                ()-> new ResourceNotFoundException("Category", "categoryId",categoryId)
        );
        categoryRepository.delete(categoryToBeDeleted);
        return "category removed successfully";
    }

    @Override
    public Category updateCategory(Category category, Long categoryId) {

        Category categoryToBeUpdated = categoryRepository.findById(categoryId).orElseThrow(
                ()-> new ResourceNotFoundException("Category", "categoryId",categoryId)
        );
        categoryToBeUpdated.setCategoryName(category.getCategoryName());
        categoryRepository.save(categoryToBeUpdated);
        return categoryToBeUpdated;
    }
}

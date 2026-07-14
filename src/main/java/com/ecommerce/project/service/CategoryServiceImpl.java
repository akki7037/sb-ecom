package com.ecommerce.project.service;

import com.ecommerce.project.exception.APIException;
import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponseDTO;
import com.ecommerce.project.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponseDTO getCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder){

        System.out.println("Debug - sortBy: " + sortBy + ", sortOrder: " + sortOrder);
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Category> categoryPage = categoryRepository.findAll(pageDetails);
        List<Category> allCategories= categoryPage.getContent();
        if(allCategories.isEmpty())
            throw new APIException("No category created till now");

        List<CategoryDTO> categoryDTOS = allCategories.stream().map(category -> modelMapper.map(category, CategoryDTO.class)).collect(Collectors.toList());

        CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO();
        categoryResponseDTO.setContent(categoryDTOS);

        categoryResponseDTO.setPageNumber(categoryPage.getNumber());
        categoryResponseDTO.setPageSize(categoryPage.getSize());
        categoryResponseDTO.setTotalPages((long) categoryPage.getTotalPages());
        categoryResponseDTO.setTotalElements((Long) categoryPage.getTotalElements());
        categoryResponseDTO.setLastPage(categoryPage.isLast());
        return categoryResponseDTO;
    }

    @Override
    public CategoryDTO createNewCategory(CategoryDTO categoryDTO){

        Category category = modelMapper.map(categoryDTO, Category.class);
        Category categoryFromDb = categoryRepository.findByCategoryName(categoryDTO.getCategoryName());
        if(categoryFromDb != null)
            throw  new APIException("Category with the name "+categoryDTO.getCategoryName()+" already exists!!!");

        Category categoryToBeCreated = categoryRepository.save(category);
        CategoryDTO savedCategoryDTO = modelMapper.map(categoryToBeCreated, CategoryDTO.class);
        return  savedCategoryDTO;
    }

    @Override
    public CategoryDTO deleteCategory(Long categoryId) {

        Category categoryToBeDeleted = categoryRepository.findById(categoryId).orElseThrow(
                ()-> new ResourceNotFoundException("Category", "categoryId",categoryId)
        );
        CategoryDTO deletedCategoryDTO = modelMapper.map(categoryToBeDeleted, CategoryDTO.class);
        categoryRepository.delete(categoryToBeDeleted);
        return deletedCategoryDTO;
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId) {

        Category categoryToBeUpdated = categoryRepository.findById(categoryId).orElseThrow(
                ()-> new ResourceNotFoundException("Category", "categoryId",categoryId)
        );
        Category category = modelMapper.map(categoryDTO, Category.class);
        category.setCategoryId(categoryId);
        categoryToBeUpdated = categoryRepository.save(category);
        return modelMapper.map(categoryToBeUpdated, CategoryDTO.class);
    }
}

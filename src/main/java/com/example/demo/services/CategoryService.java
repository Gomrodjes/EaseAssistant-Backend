package com.example.demo.services;

import java.util.List;

import com.example.demo.models.category.CategoryResponseDTO;
import com.example.demo.models.category.CategorySaveDTO;
import com.example.demo.models.category.CategoryUpdateDTO;

public interface CategoryService {
    List<CategoryResponseDTO> getAllCategories();

    CategoryResponseDTO getCategoryById(Long id);

    CategoryResponseDTO createCategory(CategorySaveDTO categorySaveDTO);

    CategoryResponseDTO updateCategory(Long id, CategoryUpdateDTO categoryUpdateDTO);

    void deleteCategory(Long id);
}

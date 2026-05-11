package com.example.demo.services.implement;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.example.demo.entities.Category;
import com.example.demo.models.category.CategoryResponseDTO;
import com.example.demo.models.category.CategorySaveDTO;
import com.example.demo.models.category.CategoryUpdateDTO;
import com.example.demo.repositories.CategoryRepository;
import com.example.demo.services.CategoryService;

import jakarta.transaction.Transactional;

@org.springframework.stereotype.Service("categoryService")
public class CategoryServiceImpl implements CategoryService {

    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    @Qualifier("categoryRepository")
    private CategoryRepository categoryRepository;

    @Override
    public List<CategoryResponseDTO> getAllCategories() {
        List<CategoryResponseDTO> categories = new ArrayList<>();
        for (Category category : categoryRepository.findAll()) {
            categories.add(toResponseDTO(category));
        }
        return categories;
    }

    @Override
    public CategoryResponseDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            return null;
        }
        return toResponseDTO(category);
    }

    @Transactional
    @Override
    public CategoryResponseDTO createCategory(CategorySaveDTO categorySaveDTO) {
        if (categoryRepository.existsByName(categorySaveDTO.getName())) {
            throw new IllegalArgumentException("Category already exists with name: " + categorySaveDTO.getName());
        }

        Category category = modelMapper.map(categorySaveDTO, Category.class);
        category.setServices(new ArrayList<>());

        Category savedCategory = categoryRepository.save(category);

        return toResponseDTO(savedCategory);
    }

    @Transactional
    @Override
    public CategoryResponseDTO updateCategory(Long id, CategoryUpdateDTO categoryUpdateDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category does not exist to update"));

        if (categoryRepository.existsByNameAndIdNot(categoryUpdateDTO.getName(), id)) {
            throw new IllegalArgumentException(
                    "Another category already exists with name: " + categoryUpdateDTO.getName());
        }

        category.setName(categoryUpdateDTO.getName());
        category.setDescription(categoryUpdateDTO.getDescription());
        category.setActive(categoryUpdateDTO.isActive());

        categoryRepository.save(category);

        return toResponseDTO(category);
    }

    @Transactional
    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category does not exist to delete"));

        categoryRepository.delete(category);
    }

    private CategoryResponseDTO toResponseDTO(Category category) {
        CategoryResponseDTO response = modelMapper.map(category, CategoryResponseDTO.class);
        response.setServiceNames(category.getServices().stream().map(s -> s.getName()).toList());
        return response;
    }
}

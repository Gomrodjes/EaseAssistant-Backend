package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.category.CategoryResponseDTO;
import com.example.demo.models.category.CategorySaveDTO;
import com.example.demo.models.category.CategoryUpdateDTO;
import com.example.demo.models.response.ResponseApi;
import com.example.demo.services.CategoryService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    @Qualifier("categoryService")
    private CategoryService categoryService;

    @GetMapping()
    public ResponseEntity<?> getAllCategories() {
        return ResponseEntity
                .ok(new ResponseApi<>(true, categoryService.getAllCategories(), "Categories retrieved successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        CategoryResponseDTO category = categoryService.getCategoryById(id);
        if (category == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseApi<>(false, null, "Category not found with id: " + id));
        }
        return ResponseEntity
                .ok(new ResponseApi<>(true, category, "Category retrieved successfully"));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createCategory(@RequestBody @Valid CategorySaveDTO categorySaveDTO) {
        try {
            CategoryResponseDTO createdCategory = categoryService.createCategory(categorySaveDTO);
            return ResponseEntity.ok(new ResponseApi<>(true, createdCategory, "Category created successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseApi<>(false, null, e.getMessage()));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody @Valid CategoryUpdateDTO categoryUpdateDTO) {
        try {
            CategoryResponseDTO updatedCategory = categoryService.updateCategory(id, categoryUpdateDTO);
            return ResponseEntity.ok(new ResponseApi<>(true, updatedCategory, "Category updated successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseApi<>(false, null, e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok(new ResponseApi<>(true, null, "Category deleted successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseApi<>(false, null, e.getMessage()));
        }
    }
}

package com.example.demo.repositories;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Category;

@Repository("categoryRepository")
public interface CategoryRepository extends JpaRepository<Category, Serializable> {
    Optional<Category> findByName(String name);

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);
}

package com.example.library.service;

import com.example.library.model.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    List<Category> findAll();

    Category createCategory(String name);

    boolean existsByName(String name);

    Category findByName(String name);

    Category findById(UUID id);

    void deleteById(UUID id);
}

package com.example.library.service.impl;

import com.example.library.model.Category;
import com.example.library.repository.CategoryRepository;
import com.example.library.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category createCategory(String name) {
        if (existsByName(name)) return null;
        Category category = new Category();
        category.setName(name);
        categoryRepository.save(category);

        return category;
    }

    @Override
    public boolean existsByName(String name) {

        if (categoryRepository.findByName(name) != null) return true;

        return false;
    }

    @Override
    public Category findByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public Category findById(UUID id) {
        return categoryRepository.findById(id).get();
    }

    @Override
    public void deleteById(UUID id) {
        categoryRepository.deleteById(id);
    }
}

package com.example.library.mapper;

import com.example.library.dto.CategoryResponse;
import com.example.library.model.Category;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {

    public CategoryResponse categoryToResponse(Category category) {
        if (category == null) return null;
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .books(category.getBooks().size())
                .build();
    }

    public List<CategoryResponse> categoriesToResponses(List<Category> categories) {
        return categories.stream().map(this::categoryToResponse).collect(Collectors.toList());
    }
}

package com.ut.server.productservice.service;

import com.ut.server.productservice.dto.CategoryDto;
import com.ut.server.productservice.mapper.CategoryMapper;
import com.ut.server.productservice.model.Category;
import com.ut.server.productservice.repo.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    public CategoryDto createCategory(CategoryDto categoryDto, UUID userId) {

        // todo: validate
        Category category = categoryMapper.mapToEntity(categoryDto, userId);

        Category newCategory = categoryRepository.save(category);

        return categoryMapper.mapToDto(newCategory);
    }

    public List<CategoryDto> getAllCategoryByUserId(UUID userId) {
        List<Category> categories = categoryRepository.findCategoriesByUserId(userId);

        return categoryMapper.mapToDtos(categories);
    }
}

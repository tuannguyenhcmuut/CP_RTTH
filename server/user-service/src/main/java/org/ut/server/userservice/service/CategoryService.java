package org.ut.server.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.ut.server.userservice.dto.CategoryDto;
import org.ut.server.userservice.mapper.CategoryMapper;
import org.ut.server.userservice.model.Category;
import org.ut.server.userservice.repo.CategoryRepository;

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

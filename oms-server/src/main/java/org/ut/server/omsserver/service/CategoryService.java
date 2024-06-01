package org.ut.server.omsserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.ut.server.omsserver.dto.CategoryDto;
import org.ut.server.omsserver.mapper.CategoryMapper;
import org.ut.server.omsserver.model.Category;
import org.ut.server.omsserver.repo.CategoryRepository;

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

    public List<CategoryDto> getAllCategoryByUserId(UUID userId, Pageable pageable) {
        List<Category> categories;
        if (pageable != null) {
            categories = categoryRepository.findCategoriesByUserId(userId, pageable);
            return categoryMapper.mapToDtos(categories);
        }
        else {
            categories = categoryRepository.findCategoriesByUserId(userId);
            return categoryMapper.mapToDtos(categories);
        }


    }
}

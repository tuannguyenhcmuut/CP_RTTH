package org.ut.server.userservice.mapper;

import org.springframework.stereotype.Component;
import org.ut.server.userservice.dto.CategoryDto;
import org.ut.server.userservice.model.Category;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {

    public CategoryDto mapToDto(Category category) {
        if (category == null) {
            return null;
        }
        return CategoryDto.builder()
                .id(category.getId())
                .categoryName(category.getCategoryName())
                .description(category.getDescription())
                .build();
    }

    public List<CategoryDto> mapToDtos(List<Category> categories) {
        if (categories == null) {
            return null;
        }
        return categories.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public Category mapToEntity(CategoryDto categoryDto, UUID userId) {
        if (categoryDto == null) {
            return null;
        }
        return Category.builder()
                .id(categoryDto.getId())
                .categoryName(categoryDto.getCategoryName())
                .description(categoryDto.getDescription())
                .userId(userId)
                .build();
    }

    public List<Category> mapToEntities(List<CategoryDto> categories, UUID userId) {
        if (categories == null) {
            return null;
        }
        return categories.stream().map(
                categoryDto -> mapToEntity(categoryDto, userId)
        ).collect(Collectors.toList());
    }
}

package com.ut.server.productservice.mapper;

import com.ut.server.productservice.dto.CategoryRequest;
import com.ut.server.productservice.dto.CategoryResponse;
import com.ut.server.productservice.dto.ProductRequest;
import com.ut.server.productservice.dto.ProductResponse;
import com.ut.server.productservice.model.Category;
import com.ut.server.productservice.model.Product;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {
    public CategoryResponse mapToCategoryResponse(Category category) {
        if (category == null) {
            return null;
        }
        return CategoryResponse.builder()
                .id(category.getId())
                .categoryName(category.getCategoryName())
                .description(category.getDescription())
                .build();
    }

    public Category mapCategoryRequestToCategory(CategoryRequest categoryRequest, UUID userId) {
        if (categoryRequest == null) {
            return null;
        }
        return Category.builder()
                .id(categoryRequest.getId())
                .categoryName(categoryRequest.getCategoryName())
                .description(categoryRequest.getDescription())
                .userId(userId)
                .build();
    }

//    public List<Category> mapRequestsToEntities(List<CategoryRequest> productRequests, UUID userId) {
//        if (productRequests == null) {
//            return null;
//        }
//        return productRequests.stream().map(productRequest -> {
//            return mapCategoryRequestToCategory(productRequest, userId);
//        }).collect(Collectors.toList());
//    }

    public List<CategoryResponse> mapEntitiesToResponses(List<Category> categories) {
        if (categories == null) {
            return null;
        }
        return categories.stream().map(product -> {
            return mapToCategoryResponse(product);
        }).collect(Collectors.toList());
    }
}

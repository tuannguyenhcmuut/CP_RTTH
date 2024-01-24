package com.ut.server.productservice.service;

import com.ut.server.productservice.config.UserFeign;
import com.ut.server.productservice.dto.CategoryRequest;
import com.ut.server.productservice.dto.CategoryResponse;
import com.ut.server.productservice.dto.ProductResponse;
import com.ut.server.productservice.mapper.CategoryMapper;
import com.ut.server.productservice.mapper.ProductMapper;
import com.ut.server.productservice.model.Category;
import com.ut.server.productservice.model.Product;
import com.ut.server.productservice.repo.CategoryRepository;
import com.ut.server.productservice.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CategoryService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserFeign userFeign;
    private final CategoryMapper categoryMapper;
    private final ProductMapper productMapper;

    public CategoryResponse createCategory(CategoryRequest categoryRequest, UUID userId) {
        Category category = categoryMapper.mapCategoryRequestToCategory(categoryRequest, userId);
        categoryRepository.save(category);
        log.info("Category {} is saved", category.getId());
        CategoryResponse categoryResponse = categoryMapper.mapToCategoryResponse(category);
        return categoryResponse;
    }

    public List<CategoryResponse> getAllCategories(UUID userId) {
        List<Category> categories = categoryRepository.findCategoriesByUserId(userId);

        log.info("Categories: {}", categories);
        return categoryMapper.mapEntitiesToResponses(categories);
    }

    public List<ProductResponse> getProductByCategory(Long categoryId) {
        Category category = categoryRepository.findCategoryById(categoryId);
        List<Product> products = category.getProducts();

        log.info("Products: {}", products);
        return productMapper.mapEntitiesToResponses(products);

    }
}

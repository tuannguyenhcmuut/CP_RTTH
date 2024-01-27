package com.ut.server.productservice.repo;

import com.ut.server.productservice.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findCategoriesByUserId(UUID userId);
    Optional<Category> findCategoryByUserIdAndId(UUID userId, Long id);
    Optional<Category> findCategoryByUserIdAndCategoryName(UUID userId, String categoryName);
}

package com.ut.server.productservice.repo;

import com.ut.server.productservice.model.Category;
import com.ut.server.productservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findCategoriesByUserId(UUID userId);
    Category findCategoryById(Long id);
}

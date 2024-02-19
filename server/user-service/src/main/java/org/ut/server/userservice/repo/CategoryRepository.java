package org.ut.server.userservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ut.server.userservice.model.Category;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findCategoriesByUserId(UUID userId);
    Optional<Category> findCategoryByUserIdAndId(UUID userId, Long id);
    Optional<Category> findCategoryByUserIdAndCategoryName(UUID userId, String categoryName);
}

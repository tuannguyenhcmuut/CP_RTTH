package com.ut.server.productservice.repo;

import com.ut.server.productservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findProductsByUserId(UUID userId);
    Product findProductByUserIdAndId(UUID userId, Long productId);
    void deleteProductById(Long id);
    Product findProductByUserIdAndName(UUID userId, String name);
    Optional<Product> findProductByIdAndUserId(Long id, UUID userId);
}

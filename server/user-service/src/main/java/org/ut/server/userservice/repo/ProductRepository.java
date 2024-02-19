package org.ut.server.userservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ut.server.userservice.model.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findProductsByUserId(UUID userId);
//    Product findProductByUserIdAndId(UUID userId, Long productId);
    void deleteProductById(Long id);
    Product findProductByUserIdAndName(UUID userId, String name);
    Optional<Product> findProductByIdAndUserId(Long id, UUID userId);
}

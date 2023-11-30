package com.ut.server.productservice.repo;

import com.ut.server.productservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findProductsByUserId(Long userId);
    void deleteProductById(Long id);
    Product findProductByUserIdAndName(Long userId, String name);
}

package org.ut.server.omsserver.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ut.server.omsserver.model.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findProductsByShopOwner_Id(UUID userId);
//    Product findProductByUserIdAndId(UUID userId, Long productId);
    void deleteProductById(Long id);
    Product findProductByShopOwner_IdAndName(UUID userId, String name);
    Optional<Product> findProductByIdAndShopOwner_Id(Long id, UUID userId);
}

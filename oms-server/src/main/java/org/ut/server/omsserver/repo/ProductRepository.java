package org.ut.server.omsserver.repo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.ut.server.omsserver.model.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findProductsByShopOwner_Id(UUID userId);
//    Product findProductByUserIdAndId(UUID userId, Long productId);
    List<Product> findProductsByShopOwner_Id(UUID userId, Pageable pageable);
    void deleteProductById(Long id);
    Product findProductByShopOwner_IdAndName(UUID userId, String name);
    Optional<Product> findProductByIdAndShopOwner_Id(Long id, UUID userId);

    @Query(value = "SELECT COUNT(p) FROM product p WHERE p.user_id = :userId AND DATE(p.created_date) = CURRENT_DATE", nativeQuery = true)
    Long countTotalProductCreatedToday(@Param("userId") UUID userId);

}

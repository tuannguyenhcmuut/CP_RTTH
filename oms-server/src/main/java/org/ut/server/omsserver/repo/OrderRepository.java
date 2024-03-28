package org.ut.server.omsserver.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.ut.server.omsserver.model.Order;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.id = :orderId AND o.shopOwner.id = :userId")
    Optional<Order> findOrderByIdAndShopOwner_Id(Long orderId, UUID userId);

    List<Order> findOrdersByShopOwner_Id(UUID userId);

    Optional<Order> findByIdAndShopOwner_Id(Long orderId, UUID userId);
}

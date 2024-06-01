package org.ut.server.omsserver.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ut.server.omsserver.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Boolean existsOrderItemByProduct_Id(Long productId);
}

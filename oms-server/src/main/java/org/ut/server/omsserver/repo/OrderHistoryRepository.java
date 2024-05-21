package org.ut.server.omsserver.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ut.server.omsserver.model.OrderHistory;

import java.util.List;

//@Repository
public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {
    // get order histories by order id
    List<OrderHistory> findAllByOrderId(Long orderId);
    // get order histories by order id and sort by action date
    List<OrderHistory> findAllByOrderIdOrderByActionDateDesc(Long orderId);
}

package com.ut.server.orderservice.repo;

import com.ut.server.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    public Order findOrderByIdAndUserId(Long orderId, Long userId);
}

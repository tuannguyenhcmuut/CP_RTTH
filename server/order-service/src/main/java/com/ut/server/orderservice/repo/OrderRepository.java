package com.ut.server.orderservice.repo;

import com.ut.server.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, Long> {
    public Order findOrderByIdAndUserId(Long orderId, UUID userId);

    List<Order> findOrdersByUserId(UUID userId);
}

package org.ut.server.omsserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ut.server.omsserver.model.Order;
import org.ut.server.omsserver.model.OrderHistory;
import org.ut.server.omsserver.repo.OrderHistoryRepository;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class OrderHistoryService {
    // get order histories by order id
    // store order history with order id and message
    private final OrderHistoryRepository orderHistoryRepository;
    public OrderHistory storeOrderHistory(Order order, String message) {
        OrderHistory newOrderHistory = OrderHistory.builder()
                .order(order)
                .description(message)
                .build();
        return orderHistoryRepository.save(newOrderHistory);
    }
}

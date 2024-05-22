package org.ut.server.omsserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ut.server.omsserver.dto.OrderHistoryDto;
import org.ut.server.omsserver.model.Order;
import org.ut.server.omsserver.model.OrderHistory;
import org.ut.server.omsserver.repo.OrderHistoryRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

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

    // get order history by order code
    public List<OrderHistoryDto> getOrderHistoriesByOrderCode(String orderCode) {
        List<Object[]> orderHistories =  orderHistoryRepository.findOrderHistoryInfoByOrderCode(orderCode);
        return orderHistories.stream().map(
                obj -> OrderHistoryDto.builder()
                        .actionDate(((Timestamp) obj[0]).toLocalDateTime())
                        .description((String) obj[1])
                        .build()
        ).collect(Collectors.toList());
    }
}

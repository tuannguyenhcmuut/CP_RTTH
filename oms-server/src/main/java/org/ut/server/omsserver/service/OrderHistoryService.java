package org.ut.server.omsserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ut.server.omsserver.common.MessageConstants;
import org.ut.server.omsserver.dto.OrderHistoryDto;
import org.ut.server.omsserver.exception.OrderNotFoundException;
import org.ut.server.omsserver.model.Order;
import org.ut.server.omsserver.model.OrderHistory;
import org.ut.server.omsserver.repo.OrderHistoryRepository;
import org.ut.server.omsserver.repo.OrderRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
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
    private final OrderRepository orderRepository;
    public OrderHistory storeOrderHistory(Order order, String message) {
        OrderHistory newOrderHistory = OrderHistory.builder()
                .orderId(order.getId())
                .description(message)
                .build();
        return orderHistoryRepository.save(newOrderHistory);
    }

    // get order history by order code
    public List<OrderHistoryDto> getOrderHistoriesByOrderCode(String orderCode) {
        List<Object[]> orderHistories =  orderHistoryRepository.findOrderHistoryInfoByOrderCode(orderCode);
        if (orderHistories.isEmpty()) {
            throw new OrderNotFoundException(MessageConstants.ORDER_NOT_FOUND);
        }
        return orderHistories.stream().map(
                obj -> OrderHistoryDto.builder()
                        .actionDate(((Timestamp) obj[0]).toLocalDateTime())
                        .description((String) obj[1])
                        .build()
        ).collect(Collectors.toList());
    }

    // create order history by order id
    public OrderHistoryDto createOrderHistoryByOrderId(Long orderId, String message) {
        // find order by id
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new OrderNotFoundException(MessageConstants.ORDER_NOT_FOUND)
        );
        OrderHistory newOrderHistory = OrderHistory.builder()
                .orderId(order.getId())
                .actionDate(LocalDateTime.now())
                .description(message)
                .build();
        OrderHistory orderHistory = orderHistoryRepository.save(newOrderHistory);
        // why i got exception :  Resolved [org.springframework.dao.DataIntegrityViolationException: could not execute statement; SQL [n/a]; constraint [fks8ybcps9klk3kn656ler8odm]; nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statemen

        return OrderHistoryDto.builder()
                .actionDate(orderHistory.getActionDate())
                .description(orderHistory.getDescription())
                .build();
    }
}

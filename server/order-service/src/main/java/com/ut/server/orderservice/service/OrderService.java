package com.ut.server.orderservice.service;

import com.ut.server.orderservice.dto.OrderOptionResponse;
import com.ut.server.orderservice.dto.OrderRequest;
import com.ut.server.orderservice.dto.OrderResponse;
import com.ut.server.orderservice.dto.StatusRequest;
import org.springframework.http.ResponseEntity;


import java.util.List;
import java.util.UUID;

public interface OrderService {
    public ResponseEntity<OrderResponse> getOrderById(UUID userId, Long orderId);
    public String updateReceiver(UUID userId, Long orderId, String receiverId);
    public ResponseEntity<String> updateOrderStatus(UUID userId, Long orderId, StatusRequest statusRequest);
    public String deleteOrder(UUID userId, Long orderId);
    public List<OrderOptionResponse> getAllOrderOptions(UUID userId, Long orderId);
    public ResponseEntity<List<OrderResponse>> getAllOrders(UUID userId);
    public ResponseEntity<?> createOrder(UUID userId, OrderRequest orderRequest);
}

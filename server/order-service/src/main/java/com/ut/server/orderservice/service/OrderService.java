package com.ut.server.orderservice.service;

import com.ut.server.orderservice.dto.OrderOptionResponse;
import com.ut.server.orderservice.dto.OrderRequest;
import com.ut.server.orderservice.dto.OrderResponse;
import com.ut.server.orderservice.dto.StatusRequest;


import java.util.List;

public interface OrderService {
    public OrderResponse getOrderById(Long user_id, Long order_id);
    public String updateReceiver(Long user_id, Long order_id, String receiver_id);
    public String updateOrderStatus(Long user_id, Long order_id, StatusRequest statusRequest);
    public String deleteOrder(Long user_id, Long order_id);
    public List<OrderOptionResponse> getAllOrderOptions(Long user_id, Long order_id);
    public List<OrderResponse> getAllOrders(Long user_id);
    public String createOrder(Long user_id, OrderRequest orderRequest);
}

package com.ut.server.orderservice.utils;

import com.ut.server.orderservice.dto.request.OrderResponse;
import com.ut.server.orderservice.model.Order;

public class OrderUtils {

    public static String generateOrderCode(Long orderId) {
        // Logic to generate the order code based on your format
        // You can use timestamps, counters, random strings, etc. to create the code
        // For example:
        String prefix = "ON-" + String.valueOf(orderId);

        return prefix;
    }

    public static OrderResponse mapOrderToOrderResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .code(order.getCode())
                .height(order.getHeight())
                .width(order.getWidth())
                .depth(order.getDepth())
                .storeId(order.getStoreId())
                .receiverId(order.getReceiverId())
                .orderStatus(order.getOrderStatus())
                .price(order.getPrice())
                .discountId(order.getDiscount().getId())
                .shipId(order.getShipId())
                .orderOptions(order.getOrderOptions())
                .build();
    }
}

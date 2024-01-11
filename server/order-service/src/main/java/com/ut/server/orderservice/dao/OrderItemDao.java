package com.ut.server.orderservice.dao;

import com.sun.istack.NotNull;
import com.ut.server.orderservice.dto.OrderItemDto;
import com.ut.server.orderservice.model.Discount;
import com.ut.server.orderservice.model.OrderPrice;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class OrderItemDao {
    private int quantity;
    private double price;
    private Long productId;
}

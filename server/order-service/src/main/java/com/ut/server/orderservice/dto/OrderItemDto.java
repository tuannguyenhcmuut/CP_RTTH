package com.ut.server.orderservice.dto;

import com.ut.server.orderservice.model.Order;
import lombok.Builder;
import lombok.Data;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Builder
@Data
public class OrderItemDto {
    private Long id;

    private int quantity;

    private double price;

    private Order orderId;

    private ProductResponse product;

}

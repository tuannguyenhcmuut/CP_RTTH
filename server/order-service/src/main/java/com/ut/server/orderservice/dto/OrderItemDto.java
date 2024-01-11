package com.ut.server.orderservice.dto;

import com.ut.server.orderservice.model.Order;
import lombok.Builder;
import lombok.Data;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.UUID;

@Builder
@Data
public class OrderItemDto {
    private static final long serialVersionUID = 1L;
    private Long id;

    private int quantity;

    private double price;

    private Long orderId;

    private ProductResponse product;

}

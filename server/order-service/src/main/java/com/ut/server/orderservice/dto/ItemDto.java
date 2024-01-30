package com.ut.server.orderservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemDto {
    private Long id;
    private int quantity;
    private double price;
    private Long productId;
}

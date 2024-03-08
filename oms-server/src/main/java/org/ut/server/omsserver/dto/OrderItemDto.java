package org.ut.server.omsserver.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OrderItemDto {
    private static final long serialVersionUID = 1L;
    private Long id;

    private int quantity;

    private double price;

    private Long orderId;

    private ProductDto product;

}

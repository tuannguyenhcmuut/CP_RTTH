package org.ut.server.userservice.dto;

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

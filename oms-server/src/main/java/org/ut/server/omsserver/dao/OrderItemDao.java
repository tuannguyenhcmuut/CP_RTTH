package org.ut.server.omsserver.dao;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemDao {
    private int quantity;
    private double price;
    private Long productId;
}

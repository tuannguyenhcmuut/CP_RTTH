package com.ut.server.orderservice.dto.request;

import com.ut.server.orderservice.model.OrderPrice;
import lombok.*;
import org.ut.server.common.events.OrderStatus;

@Data
@Builder
public class OrderResponse {
    private Long id;
    private String code;
    private Float height;
    private Float width;
    private Float depth;
    private Long storeId;
    private Long receiverId;
    private OrderStatus orderStatus;
    private OrderPrice price;
    private Long discountId;
    private Long deliveryId;
    //    List<OrderOption> orderOptions;
    private Boolean isBulky;
    private Boolean isFragile;
    private Boolean isValuable;
}

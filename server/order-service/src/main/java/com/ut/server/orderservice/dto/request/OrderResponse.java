package com.ut.server.orderservice.dto.request;

import com.ut.server.orderservice.model.OrderOption;
import com.ut.server.orderservice.model.OrderPrice;
import lombok.*;
import org.ut.server.common.events.OrderStatus;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
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
    private Long shipId;
    List<OrderOption> orderOptions;
}

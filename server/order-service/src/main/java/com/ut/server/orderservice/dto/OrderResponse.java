package com.ut.server.orderservice.dto;

import com.ut.server.orderservice.model.OrderOptions;
import com.ut.server.orderservice.model.OrderPrice;
import com.ut.server.orderservice.model.Status;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

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
    private Status statusId;
    private OrderPrice price;
    private Long discount_id;
    private Long shipId;
    List<OrderOptions> orderOptions;
}

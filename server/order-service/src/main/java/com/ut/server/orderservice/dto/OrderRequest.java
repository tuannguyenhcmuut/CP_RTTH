package com.ut.server.orderservice.dto;

import com.ut.server.orderservice.model.OrderOptions;
import com.ut.server.orderservice.model.OrderPrice;
import com.ut.server.orderservice.model.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    // private String code;
    private Float height;
    private Float width;
    private Float depth;
    private Long userId;
    private Long storeId;
    private Long receiverId;
    private Status statusId;
    private OrderPrice price;
    private Long discount_id;
    List<OrderOptions> orderOptions;
}

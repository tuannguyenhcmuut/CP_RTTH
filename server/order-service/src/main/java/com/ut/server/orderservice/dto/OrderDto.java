package com.ut.server.orderservice.dto;

import com.sun.istack.NotNull;
import com.ut.server.orderservice.model.Discount;
import com.ut.server.orderservice.model.OrderItem;
import com.ut.server.orderservice.model.OrderPrice;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {
    // private String code;
    private Long id;
    private String code;
    private Float height;
    private Float width;
    private Float depth;
    private List<OrderItemDto> items;
    @NotNull
    private UUID userId;
    private Long storeId;
    private Long receiverId;
    private String orderStatus;
    private OrderPrice price;
    private Discount discount;
    private Long shipId;
    //    List<OrderOptionDto> orderOptions;
    private Boolean isBulky;
    private Boolean isFragile;
    private Boolean isValuable;

}

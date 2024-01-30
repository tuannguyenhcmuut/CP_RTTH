package com.ut.server.orderservice.dto;

import com.sun.istack.NotNull;
import com.ut.server.orderservice.model.Discount;
import com.ut.server.orderservice.model.OrderPrice;
import lombok.*;
import org.ut.server.common.events.OrderStatus;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class OrderDto {
    // private String code;
    private Long id;
    private String code;
    private Float height;
    private Float width;
    private Float depth;
    private List<OrderItemDto> items;
    private UUID userId;
    private StoreDto store;
    @NotNull
    private ReceiverDto receiver;
    private OrderStatus orderStatus;
    private OrderPrice price;
    private Discount discount;
    private Boolean isDocument; // tai lieu
    private Boolean isBulky; // cong kenh
    private Boolean isFragile; // de vo
    private Boolean isValuable; // gia tri
    private Long deliveryId;
}

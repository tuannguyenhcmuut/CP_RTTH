package org.ut.server.userservice.dto.request;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Data;
import org.ut.server.userservice.dto.OrderItemDto;
import org.ut.server.userservice.dto.ReceiverDto;
import org.ut.server.userservice.dto.StoreDto;
import org.ut.server.userservice.model.Discount;
import org.ut.server.userservice.model.OrderPrice;
import org.ut.server.userservice.model.enums.OrderStatus;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class OrderRequest {
    private Long id;
    private String code;
    private Float height;
    private Float width;
    private Float depth;
    private List<OrderItemDto> items;
    private UUID userId;
    @NotNull
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

    private DeliveryRequest delivery;
}

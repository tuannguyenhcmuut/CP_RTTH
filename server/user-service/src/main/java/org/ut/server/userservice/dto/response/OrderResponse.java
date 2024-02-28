package org.ut.server.userservice.dto.response;

import lombok.Builder;
import lombok.Data;
import org.ut.server.userservice.model.enums.OrderStatus;
import org.ut.server.userservice.model.OrderPrice;

@Data
@Builder
public class OrderResponse {
    private Long id;
    private String code;
    private Float height;
    private Float width;
    private Float length;
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

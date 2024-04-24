package org.ut.server.omsserver.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Data;
import org.ut.server.omsserver.model.Discount;
import org.ut.server.omsserver.model.OrderPrice;
import org.ut.server.omsserver.model.enums.OrderStatus;

import java.time.LocalDateTime;
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
    private Float length;
    private List<OrderItemDto> orderItemDtos;
    private UUID userId;
    private StoreDto storeDto;
    @NotNull
    private ReceiverDto receiverDto;
    private OrderStatus orderStatus;
    private OrderPrice price;
    private Discount discount;
    private Boolean isDocument; // tai lieu
    private Boolean isBulky; // cong kenh
    private Boolean isFragile; // de vo
    private Boolean isValuable; // gia tri
    private Long deliveryId;
    private String createdBy;

    @JsonInclude(Include.NON_NULL)
    private LocalDateTime createdDate;

    private String lastUpdatedBy;

    @JsonInclude(Include.NON_NULL)
    private LocalDateTime lastUpdatedDate;

    @JsonInclude(Include.NON_NULL)
    private UUID ownerId;

    @JsonInclude(Include.NON_NULL)
    private String ownerName;
}

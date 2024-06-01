package org.ut.server.omsserver.dto;

import lombok.Builder;
import lombok.Data;
import org.ut.server.omsserver.model.enums.*;

import java.time.LocalDateTime;

@Data
@Builder
public class DeliveryDto {
    private Long id;
    private ShipperDto shipper;
    private DeliveryStatus status;
    private StoreDto store;
    private ReceiverDto receiver;
    private Payer payer;
    private boolean hasLostInsurance;
    private boolean isCollected;
    private DeliveryMethod deliveryMethod;
    private LuuKho luuKho;
    private LayHang layHang;
    private GiaoHang giaoHang;
    private Long shippingFee;
    private Float collectionFee;
    private Boolean isDraft;
    private String note;
    private LocalDateTime deliveryDate;
    private LocalDateTime receivedDate;
    private LocalDateTime lastUpdated;

    private OrderDto order;
}

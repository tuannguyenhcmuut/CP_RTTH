package org.ut.server.deliveryservice.dto;

import lombok.Builder;
import lombok.Data;
import org.ut.server.common.events.*;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
public class DeliveryDto {
    private Long id;
    private Long orderId;
    private UUID shipperId;
    private DeliveryStatus status;
    private String shipperName;
    private String shipperPhone;
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
    private Date deliveryDate;
    private Date receivedDate;
}

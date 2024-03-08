package org.ut.server.omsserver.dto;

import lombok.Builder;
import lombok.Data;
import org.ut.server.omsserver.model.enums.*;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
public class DeliveryDto {
    private Long id;
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
    private OrderDto orderDto;
}

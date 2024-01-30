package org.ut.server.deliveryservice.dto;

import lombok.Builder;
import lombok.Data;
import org.ut.server.common.events.*;

@Data
@Builder
public class DeliveryRequest {
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
}

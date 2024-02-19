package org.ut.server.userservice.dto.request;

import lombok.Builder;
import lombok.Data;
import org.ut.server.userservice.model.enums.*;

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
    private Long orderId;
}

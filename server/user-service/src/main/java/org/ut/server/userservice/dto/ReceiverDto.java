package org.ut.server.common.server.dto;

import lombok.Builder;
import lombok.Data;
import org.ut.server.common.server.enums.DeliveryTime;
import org.ut.server.common.server.enums.ReceivedPlace;

@Data
@Builder
public class ReceiverDto {
    private Long id;
    private String name;
    private String phoneNumber;
    private String address;
    private String detailedAddress;
    private String note;
    private ReceivedPlace receivedPlace;
    private DeliveryTime deliveryTimeFrame;
    private Boolean callBeforeSend;
    private Boolean receiveAtPost;
}

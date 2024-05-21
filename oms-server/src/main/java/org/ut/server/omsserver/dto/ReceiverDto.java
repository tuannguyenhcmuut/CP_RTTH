package org.ut.server.omsserver.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Builder;
import lombok.Data;
import org.ut.server.omsserver.model.enums.DeliveryTime;
import org.ut.server.omsserver.model.enums.ReceivedPlace;

import java.util.UUID;

@Data
@Builder
public class ReceiverDto {
    private Long receiverId;
    private String name;
    private String phoneNumber;
    private String address;
    private String detailedAddress;
    private String note;
    private ReceivedPlace receivedPlace;
    private DeliveryTime deliveryTimeFrame;
    private Boolean callBeforeSend;
    private Boolean receiveAtPost;
    @JsonInclude(Include.NON_NULL)
    private UUID ownerId;
    @JsonInclude(Include.NON_NULL)
    private String ownerName;
}

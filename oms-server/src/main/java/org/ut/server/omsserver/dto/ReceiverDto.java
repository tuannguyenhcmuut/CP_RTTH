package org.ut.server.omsserver.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

import org.ut.server.omsserver.model.enums.DeliveryTime;
import org.ut.server.omsserver.model.enums.ReceivedPlace;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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
    @JsonInclude(Include.NON_NULL)
    private UUID ownerId;
    @JsonInclude(Include.NON_NULL)
    private String ownerName;
}

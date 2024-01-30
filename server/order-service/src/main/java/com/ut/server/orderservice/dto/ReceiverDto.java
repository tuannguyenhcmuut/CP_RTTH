package com.ut.server.orderservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReceiverDto {
    private Long id;
    private String name;
    private String phoneNumber;
    private String address;
    private String detailedAddress;
    private Boolean receiveAtPost; // nhan hang tai buu cuc
}

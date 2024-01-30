package com.ut.server.orderservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StoreDto {
    private Long id;
    private String name;
//    phone
    private String phoneNumber;
//    address
    private String address;
    private String detailedAddress;
    private Boolean sendAtPost; // gui hang tai buu cuc
}

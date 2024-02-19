package org.ut.server.userservice.dto;

import lombok.Builder;
import lombok.Data;
import org.ut.server.userservice.model.enums.StorePickUpTime;

@Builder
@Data
public class StoreDto {
    private Long id;
    private String name;
    private String phoneNumber;
    private String address;
    private String detailedAddress;
    private String description;
    private StorePickUpTime storePickUpTime;
    private Boolean isDefault;
    private Boolean sendAtPost;
}

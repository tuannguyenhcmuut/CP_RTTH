package org.ut.server.common.server.dto;

import lombok.Builder;
import lombok.Data;
import org.ut.server.common.server.enums.DeliveryTime;
import org.ut.server.common.server.enums.ReceivedPlace;
import org.ut.server.common.server.enums.StorePickUpTime;

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

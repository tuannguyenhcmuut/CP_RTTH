package org.ut.server.omsserver.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Builder;
import lombok.Data;
import org.ut.server.omsserver.model.enums.StorePickUpTime;

import java.util.UUID;

@Builder
@Data
public class StoreDto {
    private Long storeId;
    private String name;
    private String phoneNumber;
    private String address;
    private String detailedAddress;
    private String description;
    private StorePickUpTime storePickUpTime;
    private Boolean isDefault;
    private Boolean sendAtPost;
    @JsonInclude(Include.NON_NULL)
    private UUID ownerId;
    @JsonInclude(Include.NON_NULL)
    private String ownerName;
}

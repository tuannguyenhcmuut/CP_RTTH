package org.ut.server.omsserver.dto;

import lombok.Builder;
import lombok.Data;
import org.ut.server.omsserver.model.enums.Gender;

import java.util.UUID;

@Data
@Builder
public class ShipperDto {
    private UUID id;
    private String name;
    private String phoneNumber;
    private String address;
    private Gender gender;
    private String avatarUrl;
    private Float rating;
}

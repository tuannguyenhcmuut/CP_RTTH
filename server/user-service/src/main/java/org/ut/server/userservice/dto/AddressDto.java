package org.ut.server.userservice.dto;

import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class AddressDto {
    private Long id;
    private String homeNumber;
    private String country;
    private String city;
    private String district;
    private String ward;
    private String street;
    private String description;
}

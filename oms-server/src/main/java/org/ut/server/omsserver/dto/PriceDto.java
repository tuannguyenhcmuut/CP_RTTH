package org.ut.server.omsserver.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PriceDto {
    private String serviceCode;
    private String serviceName;
    private Float price;

    @JsonInclude(Include.NON_NULL)
    private Integer exchangeWeight;


}

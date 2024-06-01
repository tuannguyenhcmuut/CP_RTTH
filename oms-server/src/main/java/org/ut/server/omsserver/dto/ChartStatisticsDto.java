package org.ut.server.omsserver.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChartStatisticsDto {
    private Double totalAmount;
    private Integer totalOrder;
    private String monthYear;
}

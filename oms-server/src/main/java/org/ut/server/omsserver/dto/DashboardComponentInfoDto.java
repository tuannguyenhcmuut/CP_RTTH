package org.ut.server.omsserver.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardComponentInfoDto {
    private Long totalTodayOrders;
    private Long totalTodayDeliveries;
    private Double totalRevenue;
}

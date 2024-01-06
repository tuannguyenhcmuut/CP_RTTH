package com.ut.server.orderservice.dto;

import com.ut.server.orderservice.model.OrderOptionPK;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderOptionDto {
    private OrderOptionPK ids;
    private String description;
    private Boolean isChecked;
}

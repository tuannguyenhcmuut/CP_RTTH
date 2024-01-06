package com.ut.server.orderservice.dto.response;


import com.ut.server.orderservice.model.OrderOptionType;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderOptionResponse {
    private OrderOptionType orderOptionType;
    private Boolean value;
}
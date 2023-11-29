package com.ut.server.orderservice.dto;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderOptionsResponse {
    private Long order_id;
    private List<OrderOptionResponse> orderOptions;
}

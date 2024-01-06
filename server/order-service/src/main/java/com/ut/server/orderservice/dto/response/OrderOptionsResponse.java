package com.ut.server.orderservice.dto.response;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderOptionsResponse {
    private Long orderId;
    private List<OrderOptionResponse> orderOptions;
}

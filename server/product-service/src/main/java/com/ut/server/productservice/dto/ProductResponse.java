package com.ut.server.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private Long id;
    private String code;
    private String name;
    private String description;
    private BigDecimal price;
    private Float height;
    private Float width;
    private Float depth;
    private String photo;
    private List<CategoryResponse> categories;
}

package com.ut.server.productservice.dto;

import com.ut.server.productservice.model.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private Long id;
    private String code;
    private String name;
    private String description;
    private BigDecimal price;
    private Float weight;
    private Float height;
    private Float width;
    private Float length;
    private String photo;
    private ProductStatus status;
    private List<CategoryDto> categories;
    private UUID userId;
}

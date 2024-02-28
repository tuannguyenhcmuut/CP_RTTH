package org.ut.server.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ut.server.userservice.model.enums.ProductStatus;

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
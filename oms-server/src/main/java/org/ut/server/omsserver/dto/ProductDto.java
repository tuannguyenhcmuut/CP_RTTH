package org.ut.server.omsserver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ut.server.omsserver.model.enums.ProductStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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
    private String photoUrl;
    private ProductStatus status;
    private List<CategoryDto> categories;
    @JsonInclude(Include.NON_NULL)
    private UUID userId;
    @JsonInclude(Include.NON_NULL)
    private UUID ownerId;
    @JsonInclude(Include.NON_NULL)
    private String ownerName;
}

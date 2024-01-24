package com.ut.server.productservice.dto;

import com.ut.server.productservice.enums.Status;
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
    private Status status;
    private BigDecimal price;
    private Integer weight;
    private Float length;
    private Float height;
    private Float width;
    private String photo;
    private List<CategoryResponse> categories;
}

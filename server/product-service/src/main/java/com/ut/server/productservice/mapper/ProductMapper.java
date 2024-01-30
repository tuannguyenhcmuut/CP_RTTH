package com.ut.server.productservice.mapper;

import com.ut.server.productservice.dto.ProductDto;
import com.ut.server.productservice.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductMapper {
    @Autowired
    private final CategoryMapper categoryMapper;

    public List<ProductDto> mapEntitiesToDtos(List<Product> products) {
        if (products == null) {
            return null;
        }
        return products.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public List<Product> mapDtosToEntities(List<ProductDto> productDtos) {
        if (productDtos == null) {
            return null;
        }
        return productDtos.stream().map(this::mapDtoToEntity).collect(Collectors.toList());
    }

    public Product mapDtoToEntity(ProductDto productDto) {
        if (productDto == null) {
            return null;
        }
        return Product.builder()
                .id(productDto.getId())
                .code(productDto.getCode())
                .name(productDto.getName())
                .description(productDto.getDescription())
                .status(productDto.getStatus())
                .price(productDto.getPrice())
                .weight(productDto.getWeight())
                .height(productDto.getHeight())
                .width(productDto.getWidth())
                .length(productDto.getLength())
                .categories(
                        categoryMapper.mapToEntities(productDto.getCategories(), productDto.getUserId())
                )
                .userId(productDto.getUserId())
                .build();

    }

    public ProductDto mapToDto(Product product) {
        if (product == null) {
            return null;
        }
        return ProductDto.builder()
                .id(product.getId())
                .code(product.getCode())
                .name(product.getName())
                .description(product.getDescription())
                .status(product.getStatus())
                .price(product.getPrice())
                .weight(product.getWeight())
                .height(product.getHeight())
                .width(product.getWidth())
                .length(product.getLength())
                .categories(
                        categoryMapper.mapToDtos(product.getCategories())
                )
                .userId(product.getUserId())
                .build();
    }
}

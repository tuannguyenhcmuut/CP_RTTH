package com.ut.server.productservice.mapper;

import com.ut.server.productservice.dto.CategoryResponse;
import com.ut.server.productservice.dto.ProductRequest;
import com.ut.server.productservice.dto.ProductResponse;
import com.ut.server.productservice.model.Product;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ProductMapper {
    public ProductResponse mapToProductResponse(Product product) {
        if (product == null) {
            return null;
        }
        return ProductResponse.builder()
                .id(product.getId())
                .code(product.getCode())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .height(product.getHeight())
                .width(product.getWidth())
                .length(product.getLength())
                .categories(product.getCategories() != null ? product.getCategories().stream().map( category ->
                                CategoryResponse.builder()
                                        .categoryName(category.getCategoryName())
                                        .description(category.getDescription())
                                        .build()
                        ).collect(Collectors.toList())
                                : null
                )
                .build();
    }

    public Product mapProductRequestToProduct(ProductRequest productRequest, UUID userId) {
        if (productRequest == null) {
            return null;
        }
        return Product.builder()
                .code(productRequest.getCode())
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .height(productRequest.getHeight())
                .width(productRequest.getWidth())
                .length(productRequest.getLength())
                .userId(userId)
                .categories(productRequest.getCategories())
                .build();
    }

//    public List<Product> mapRequestsToEntities(List<ProductRequest> productRequests, UUID userId) {
//        if (productRequests == null) {
//            return null;
//        }
//        return productRequests.stream().map(productRequest -> {
//            return mapProductRequestToProduct(productRequest, userId);
//        }).collect(Collectors.toList());
//    }

    public List<ProductResponse> mapEntitiesToResponses(List<Product> products) {
        if (products == null) {
            return null;
        }
        return products.stream().map(product -> {
            return mapToProductResponse(product);
        }).collect(Collectors.toList());
    }
}

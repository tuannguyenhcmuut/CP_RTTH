package com.ut.server.productservice.service;

import com.ut.server.productservice.config.UserFeign;
import com.ut.server.productservice.dto.*;
import com.ut.server.productservice.exception.ApiRequestException;
import com.ut.server.productservice.model.Product;
import com.ut.server.productservice.repo.CategoryRepository;
import com.ut.server.productservice.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserFeign userFeign;
    public String createProduct(ProductRequest productRequest, UUID userId) {

        Product product = mapProductRequestToProduct(productRequest, userId);
        productRepository.save(product);
        log.info("Product {} is saved", product.getId());
        return "OK";
    }

//    getAllProducts
    public List<ProductResponse> getAllProducts(UUID userId) {
//        return productRepository.findProductsByUserId(userId);
        List<Product> products = productRepository.findProductsByUserId(userId);
        List<UserDTO> users = (List<UserDTO>) userFeign.getUser();
        log.info("Users: {}", users);
        return products.stream().map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }

    public ProductResponse updateProduct(UUID userId, Long productId, Product product) {
        Product productToUpdate = productRepository.findProductByUserIdAndId(userId, productId);
        if (productToUpdate == null) {
            throw new ApiRequestException("Product not found!" + productId);
        }
        productToUpdate.setCode(product.getCode());
        productToUpdate.setName(product.getName());
        productToUpdate.setDescription(product.getDescription());
        productToUpdate.setPrice(product.getPrice());
        productToUpdate.setHeight(product.getHeight());
        productToUpdate.setWidth(product.getWidth());
        productToUpdate.setDepth(product.getDepth());
        productToUpdate.setCategories(product.getCategories());
        productRepository.save(productToUpdate);
        return mapToProductResponse(productToUpdate);
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .code(product.getCode())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .height(product.getHeight())
                .width(product.getWidth())
                .depth(product.getDepth())
                .categories(product.getCategories().stream().map( category ->
                                CategoryResponse.builder()
                                        .categoryName(category.getCategoryName())
                                        .description(category.getDescription())
                                        .build()
                        ).collect(Collectors.toList()))
                .build();
    }

    private Product mapProductRequestToProduct(ProductRequest productRequest, UUID userId) {
        return Product.builder()
                .code(productRequest.getCode())
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .height(productRequest.getHeight())
                .width(productRequest.getWidth())
                .depth(productRequest.getDepth())
                .userId(userId)
                .categories(productRequest.getCategories())
                .build();
    }


    public String deleteProduct(Long productId) {
        if (productRepository.findById(productId) != null) {
            productRepository.deleteProductById(productId);
            return "OK";
        }
        else {
            return "Product not found.";
        }
    }
}

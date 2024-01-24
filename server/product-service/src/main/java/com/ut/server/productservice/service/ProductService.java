package com.ut.server.productservice.service;

import com.ut.server.productservice.config.UserFeign;
import com.ut.server.productservice.dto.*;
import com.ut.server.productservice.exception.ApiRequestException;
import com.ut.server.productservice.mapper.ProductMapper;
import com.ut.server.productservice.model.Category;
import com.ut.server.productservice.model.Product;
import com.ut.server.productservice.repo.CategoryRepository;
import com.ut.server.productservice.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserFeign userFeign;
    private final ProductMapper productMapper;
    public ProductResponse createProduct(ProductRequest productRequest, UUID userId) {
        // check user is existed

        // TODO: fix request is authenticated when request to user-service
//        if (userFeign.getUserById(userId).getBody() == null) {
//            throw new ApiRequestException("User not found!");
//        }

        Product product = productMapper.mapProductRequestToProduct(productRequest, userId);
        productRepository.save(product);
        log.info("Product {} is saved", product.getId());
        ProductResponse productResponse = productMapper.mapToProductResponse(product);
        return productResponse;
    }

//    getAllProducts
    public List<ProductResponse> getAllProducts(UUID userId) {
        // TODO: fix request is authenticated when request to user-service
//        if (userFeign.getUserById(userId).getBody()  == null) {
//            throw new ApiRequestException("User not found!");
//        }
        List<Product> products = productRepository.findProductsByUserId(userId);
//        List<UserDTO> users = (List<UserDTO>) userFeign.getUser();
        log.info("Products: {}", products);
        return productMapper.mapEntitiesToResponses(products);
    }


    public ProductResponse getProductById( Long productId, UUID userId) {
        Optional<Product> product = productRepository.findProductByIdAndUserId(productId, userId);
        if (product.isPresent()) {
            if (product.get().getUserId().equals(userId)) {
                return productMapper.mapToProductResponse(product.get());
            }
            else {
                throw new ApiRequestException("Product not found!");
            }
        }
        else {
                throw new ApiRequestException("Product not found!");
        }

    }

    public ProductResponse updateProduct(UUID userId, Long productId, Product product) {
        // verify user with product

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
        productToUpdate.setLength(product.getLength());
        productToUpdate.setCategories(product.getCategories());
        productRepository.save(productToUpdate);
        return productMapper.mapToProductResponse(productToUpdate);
    }

//    private ProductResponse mapToProductResponse(Product product) {
//        return ProductResponse.builder()
//                .id(product.getId())
//                .code(product.getCode())
//                .name(product.getName())
//                .description(product.getDescription())
//                .price(product.getPrice())
//                .height(product.getHeight())
//                .width(product.getWidth())
//                .depth(product.getDepth())
//                .categories(product.getCategories() != null ? product.getCategories().stream().map( category ->
//                                CategoryResponse.builder()
//                                        .categoryName(category.getCategoryName())
//                                        .description(category.getDescription())
//                                        .build()
//                        ).collect(Collectors.toList())
//                        : null
//                )
//                .build();
//    }
//
//    private Product mapProductRequestToProduct(ProductRequest productRequest, UUID userId) {
//        return Product.builder()
//                .code(productRequest.getCode())
//                .name(productRequest.getName())
//                .description(productRequest.getDescription())
//                .price(productRequest.getPrice())
//                .height(productRequest.getHeight())
//                .width(productRequest.getWidth())
//                .depth(productRequest.getDepth())
//                .userId(userId)
//                .categories(productRequest.getCategories())
//                .build();
//    }


    public void deleteProduct(Long productId) {
        if (productRepository.findById(productId) != null) {
            productRepository.deleteProductById(productId);
        }
        else {
            throw new ApiRequestException("Product not found!");
        }
    }

}

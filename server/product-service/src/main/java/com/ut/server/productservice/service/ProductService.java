package com.ut.server.productservice.service;

import com.ut.server.productservice.config.UserFeign;
import com.ut.server.productservice.dto.*;
import com.ut.server.productservice.exception.ApiRequestException;
import com.ut.server.productservice.mapper.ProductMapper;
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

@Service
@RequiredArgsConstructor
@Slf4j
//@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserFeign userFeign;
    private final ProductMapper productMapper;

    public ProductDto createProduct(ProductDto productDto, UUID userId) {
        Product product = productMapper.mapDtoToEntity(productDto);
        productRepository.save(product);
        log.info("Product {} is saved", product.getId());
        return productMapper.mapToDto(product);
    }

    //    getAllProducts
    public List<ProductDto> getAllProducts(UUID userId) {
        List<Product> products = productRepository.findProductsByUserId(userId);
        log.info("Products: {}", products);
        return productMapper.mapEntitiesToDtos(products);
    }


    public ProductDto getProductById(Long productId, UUID userId) {
        Optional<Product> product = productRepository.findProductByIdAndUserId(productId, userId);
        if (product.isPresent()) {
            if (product.get().getUserId().equals(userId)) {
                return productMapper.mapToDto(product.get());
            }
        }
        throw new ApiRequestException("Product not found!");

    }

    public ProductDto updateProduct(UUID userId, Long productId, Product product) {
        Product productToUpdate = productRepository.findProductByUserIdAndId(userId, productId);
        if (productToUpdate == null) {
            throw new ApiRequestException("Product not found!" + productId);
        }
        productToUpdate.setCode(product.getCode());
        productToUpdate.setName(product.getName());
        productToUpdate.setDescription(product.getDescription());
        productToUpdate.setPrice(product.getPrice());
        productToUpdate.setWeight(product.getWeight());
        productToUpdate.setHeight(product.getHeight());
        productToUpdate.setWidth(product.getWidth());
        productToUpdate.setLength(product.getLength());
        productToUpdate.setCategories(product.getCategories());
        productRepository.save(productToUpdate);
        return productMapper.mapToDto(productToUpdate);
    }


    public void deleteProduct(Long productId) {
        if (productRepository.findById(productId) != null) {
            productRepository.deleteProductById(productId);
            ;
        } else {
            throw new ApiRequestException("Product not found!");
        }
    }
}

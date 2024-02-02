package com.ut.server.productservice.service;

import com.ut.server.productservice.dto.*;
import com.ut.server.productservice.exception.ApiRequestException;
import com.ut.server.productservice.exception.FileUploadException;
import com.ut.server.productservice.mapper.ProductMapper;
import com.ut.server.productservice.model.Product;
import com.ut.server.productservice.repo.ProductRepository;
import com.ut.server.productservice.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
//@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final FileUtils fileUtils;

    public ProductDto createProduct(ProductDto productDto) {
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

    public FileDto uploadImage(byte[] imageBytes) {
        // convert image to base64
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
        log.info("Base64 image: {}", base64Image);
            return FileDto.builder()
                .base64(base64Image)
                .sizeKB(FileUtils.getFileSizeKB(base64Image)) // get file size in kb
                .build();
    }

    @Transactional
    public ProductDto uploadImageToProduct(Long productId, byte[] imageBytes) {
        // find product by id
        Optional<Product> product = productRepository.findById(productId);
        if (product.isEmpty()) {
            throw new ApiRequestException("Product not found!");
        }

        // convert image to base64
        String base64Image = uploadImage(imageBytes).getBase64();

        try {
            product.get().setPhoto(FileUtils.base64ToBlob(base64Image));
        } catch (SQLException e) {
            throw new FileUploadException("Error converting photo to Blob. " + e.getMessage());
        }
        // save image to product
        productRepository.save(product.get());
        return productMapper.mapToDto(product.get());
    }
}

package org.ut.server.userservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.ut.server.userservice.dto.FileDto;
import org.ut.server.userservice.dto.ProductDto;
import org.ut.server.userservice.exception.FileUploadException;
import org.ut.server.userservice.exception.ProductInUsedException;
import org.ut.server.userservice.exception.ProductNotFoundException;
import org.ut.server.userservice.mapper.ProductMapper;
import org.ut.server.userservice.model.Product;
import org.ut.server.userservice.repo.OrderItemRepository;
import org.ut.server.userservice.repo.ProductRepository;
import org.ut.server.userservice.utils.FileUtils;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
//@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final OrderItemRepository orderItemRepository;

    public ProductDto createProduct(ProductDto productDto) {
        Product product = productMapper.mapDtoToEntity(productDto);
        Product newProduct = productRepository.save(product);
        log.info("Product {} is saved", newProduct.getId());
        return productMapper.mapToDto(newProduct);
    }

    //    getAllProducts
    public List<ProductDto> getAllProducts(UUID userId) {
        List<Product> products = productRepository.findProductsByUserId(userId);
        log.info("Products: {}", products);
        return productMapper.mapEntitiesToDtos(products);
    }


    public ProductDto getProductById(Long productId, UUID userId) {
        Product product = productRepository.findProductByIdAndUserId(productId, userId)
                .orElseThrow(
                        () -> new ProductNotFoundException("Product not found by id: " + productId.toString())
                );

        if (product.getUser().getId().equals(userId)) {
            return productMapper.mapToDto(product);
        }
        throw new ProductNotFoundException("Product not found by id: " + productId.toString());
    }

    public ProductDto updateProduct(UUID userId, Long productId, Product product) {
        Product productToUpdate = productRepository.findProductByIdAndUserId(productId, userId)
                .orElseThrow(
                        () -> new ProductNotFoundException("Product not found by id: " + productId.toString())
                );
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


    public void deleteProduct(Long productId, UUID userId) {
        // check if product belongs to user
        Product product = productRepository.findProductByIdAndUserId(productId, userId)
                .orElseThrow(
                        () -> new ProductNotFoundException("Product not found by id: " + productId.toString())
                );

        if (orderItemRepository.existsOrderItemByProduct_Id(product.getId())) {
            throw new ProductInUsedException("Product is used in order items");
        }
        productRepository.deleteProductById(product.getId());

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
    public ProductDto uploadImageToProduct(Long productId, byte[] imageBytes, UUID userId) {
        // find product by id
       Product product = productRepository.findProductByIdAndUserId(productId, userId)
                .orElseThrow(
                        () -> new ProductNotFoundException("Product not found by id: " + productId.toString())
                );

        // convert image to base64
        String base64Image = uploadImage(imageBytes).getBase64();

        try {
            product.setPhoto(FileUtils.base64ToBlob(base64Image));
        } catch (SQLException e) {
            throw new FileUploadException("Error converting photo to Blob. " + e.getMessage());
        }
        // save image to product
        productRepository.save(product);
        return productMapper.mapToDto(product);
    }
}

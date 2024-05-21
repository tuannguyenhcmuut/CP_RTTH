package org.ut.server.omsserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.ut.server.omsserver.common.MessageConstants;
import org.ut.server.omsserver.dto.FileDto;
import org.ut.server.omsserver.dto.ProductDto;
import org.ut.server.omsserver.exception.EmployeeManagementException;
import org.ut.server.omsserver.exception.ProductInUsedException;
import org.ut.server.omsserver.exception.ProductNotFoundException;
import org.ut.server.omsserver.mapper.ProductMapper;
import org.ut.server.omsserver.model.EmployeeManagement;
import org.ut.server.omsserver.model.Product;
import org.ut.server.omsserver.model.ShopOwner;
import org.ut.server.omsserver.model.enums.EmployeeRequestStatus;
import org.ut.server.omsserver.repo.EmployeeManagementRepository;
import org.ut.server.omsserver.repo.OrderItemRepository;
import org.ut.server.omsserver.repo.ProductRepository;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final OrderItemRepository orderItemRepository;
    private final IImageService imageService;
    private final EmployeeManagementRepository employeeManagementRepository;


    public ProductDto createProduct(ProductDto productDto) {
        Product product = productMapper.mapDtoToEntity(productDto);
        Product newProduct = productRepository.save(product);
        log.info("Product {} is saved", newProduct.getId());
        return productMapper.mapToDto(newProduct,null);
    }

    //    getAllProducts
    public List<ProductDto> getAllProducts(UUID userId, Pageable pageable) {

        List<Product> products;
        if (pageable != null) {
            products = productRepository.findProductsByShopOwner_Id(userId, pageable);
        }
        else {
            products = productRepository.findProductsByShopOwner_Id(userId);
        }
        log.info("Products: {}", products);

        // get product from their owner
        List<ProductDto> productDtos =  productMapper.mapEntitiesToDtos(products, null);

        try {
            List<ProductDto> ownerProductDtos = this.getAllProductsByOwner(userId, pageable);
            productDtos.addAll(ownerProductDtos);
        }
        catch (Exception e) {
        }

        return productDtos;

    }

    private List<ProductDto> getProductsFromOwner(UUID userId) {

        return null;
    }


    public ProductDto getProductById(Long productId, UUID userId) {
        Product product = productRepository.findProductByIdAndShopOwner_Id(productId, userId)
                .orElseThrow(
                        () -> new ProductNotFoundException(String.format(MessageConstants.PRODUCT_NOT_FOUND, productId.toString()))
                );

        if (product.getShopOwner().getId().equals(userId)) {
            return productMapper.mapToDto(product,null);
        }
        throw new ProductNotFoundException(String.format(MessageConstants.PRODUCT_NOT_FOUND, productId.toString()));
    }

    public ProductDto updateProduct(UUID userId, Long productId, Product product) {
        Product productToUpdate = productRepository.findProductByIdAndShopOwner_Id(productId, userId)
                .orElseThrow(
                        () -> new ProductNotFoundException(String.format(MessageConstants.PRODUCT_NOT_FOUND, productId.toString()))
                );
        if (product.getCode() != null) productToUpdate.setCode(product.getCode());
        // check if getName is not null
        if (product.getName() != null) productToUpdate.setName(product.getName());
        if (product.getDescription() != null) productToUpdate.setDescription(product.getDescription());
        if (product.getPrice() != null) productToUpdate.setPrice(product.getPrice());
        if (product.getWeight() != null) productToUpdate.setWeight(product.getWeight());
        if (product.getHeight() != null) productToUpdate.setHeight(product.getHeight());
        if (product.getWidth() != null) productToUpdate.setWidth(product.getWidth());
        if (product.getLength() != null) productToUpdate.setLength(product.getLength());
        if (product.getCategories() != null) productToUpdate.setCategories(product.getCategories());
        if (product.getPhotoUrl() != null) productToUpdate.setPhotoUrl(product.getPhotoUrl());
        if (product.getStatus() != null) productToUpdate.setStatus(product.getStatus());
        productRepository.save(productToUpdate);
        return productMapper.mapToDto(productToUpdate,null);
    }


    public void deleteProduct(Long productId, UUID userId) {
        // check if product belongs to user
        Product product = productRepository.findProductByIdAndShopOwner_Id(productId, userId)
                .orElseThrow(
                        () -> new ProductNotFoundException(String.format(MessageConstants.PRODUCT_NOT_FOUND, productId.toString()))
                );

        if (orderItemRepository.existsOrderItemByProduct_Id(product.getId())) {
            throw new ProductInUsedException(MessageConstants.PRODUCT_IN_USE);
        }
        productRepository.deleteProductById(product.getId());

    }

    public FileDto uploadImage(MultipartFile file) throws IOException {
        // convert image to base64
        String fileName = imageService.save(file);

        String imageUrl = imageService.getImageUrl(fileName);
        return FileDto.builder()
                .base64(imageUrl)
                .sizeKB(null) // get file size in kb
                .build();
    }

    @Transactional
    public ProductDto uploadImageToProduct(Long productId, MultipartFile imageFile, UUID userId) throws IOException {
        // find product by id
        Product product = productRepository.findProductByIdAndShopOwner_Id(productId, userId)
                .orElseThrow(
                        () -> new ProductNotFoundException(String.format(MessageConstants.PRODUCT_NOT_FOUND, productId.toString()))
                );
        String fileName = imageService.save(imageFile);
        String imageUrl = imageService.getImageUrl(fileName);

        product.setPhotoUrl(imageUrl);
        // save image to product
        product = productRepository.save(product);
        return productMapper.mapToDto(product,null);
    }

    public List<ProductDto> getAllProductsByOwner(UUID userId, Pageable pageable) {
        List<EmployeeManagement> emplMgnts= employeeManagementRepository.findEmployeeManagementsByEmployee_IdAndApprovalStatus(userId, EmployeeRequestStatus.ACCEPTED);
        if (emplMgnts.isEmpty()) {
            throw new EmployeeManagementException(MessageConstants.ERROR_USER_NOT_HAS_OWNER);
        }
        EmployeeManagement emplMgnt = emplMgnts.get(0);
        ShopOwner owner = emplMgnt.getManager();
        List<Product> products;
        if (pageable != null) {
            products = productRepository.findProductsByShopOwner_Id(owner.getId(), pageable);
        }
        else {
            products = productRepository.findProductsByShopOwner_Id(owner.getId());
        }
        return productMapper.mapEntitiesToDtos(products, owner);
    }

    // get total product created today
    public Long countTotalProductCreatedToday(UUID userId) {
        // find user
        return productRepository.countTotalProductCreatedToday(userId);
    }
}

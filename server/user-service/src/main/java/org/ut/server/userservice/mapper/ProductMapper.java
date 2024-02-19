package org.ut.server.userservice.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ut.server.userservice.dto.ProductDto;
import org.ut.server.userservice.exception.ApiRequestException;
import org.ut.server.userservice.exception.FileUploadException;
import org.ut.server.userservice.model.Product;
import org.ut.server.userservice.model.User;
import org.ut.server.userservice.repo.UserRepository;
import org.ut.server.userservice.utils.FileUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductMapper {
    @Autowired
    private final CategoryMapper categoryMapper;

    @Autowired
    private final UserRepository userRepository;

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

    public Product mapDtoToEntity(ProductDto productDto){
        // find user by id
        Optional<User> user = userRepository.findById(productDto.getUserId());
        if (user.isEmpty()) {
            throw new ApiRequestException("User not found!");
        }
        if (productDto == null) {
            return null;
        }
        try {
            return Product.builder()
                    .id(productDto.getId())
                    .code(productDto.getCode())
                    .name(productDto.getName())
                    .description(productDto.getDescription())
                    .status(productDto.getStatus())
                    .price(productDto.getPrice())
                    .photo(FileUtils.base64ToBlob(productDto.getPhoto()))
                    .weight(productDto.getWeight())
                    .height(productDto.getHeight())
                    .width(productDto.getWidth())
                    .length(productDto.getLength())
                    .categories(
                            categoryMapper.mapToEntities(productDto.getCategories(), productDto.getUserId())
                    )
                    .user(user.get())
                    .build();
        } catch (SQLException e) {
            throw new FileUploadException("Error converting photo to Blob. " + e.getMessage());
        }

    }

    public ProductDto mapToDto(Product product) {
        if (product == null) {
            return null;
        }
        try {
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
                    .photo(FileUtils.blobToBase64(product.getPhoto()))
                    .length(product.getLength())
                    .categories(
                            categoryMapper.mapToDtos(product.getCategories())
                    )
                    .userId(product.getUser().getId())
                    .build();
        } catch (SQLException e) {
            throw new FileUploadException("Error converting photo to base64. " + e.getMessage());
        }
    }
}

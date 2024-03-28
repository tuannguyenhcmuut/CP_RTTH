package org.ut.server.omsserver.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ut.server.omsserver.dto.ProductDto;
import org.ut.server.omsserver.exception.ApiRequestException;
import org.ut.server.omsserver.model.Product;
import org.ut.server.omsserver.model.ShopOwner;
import org.ut.server.omsserver.repo.ShopOwnerRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductMapper {
    @Autowired
    private final CategoryMapper categoryMapper;

    @Autowired
    private final ShopOwnerRepository shopOwnerRepository;

    public List<ProductDto> mapEntitiesToDtos(List<Product> products, ShopOwner owner) {
        if (products == null) {
            return null;
        }
        return products.stream().map(
                product -> mapToDto(product, owner)
        ).collect(Collectors.toList());
    }

    public List<Product> mapDtosToEntities(List<ProductDto> productDtos) {
        if (productDtos == null) {
            return null;
        }
        return productDtos.stream().map(this::mapDtoToEntity).collect(Collectors.toList());
    }

    public Product mapDtoToEntity(ProductDto productDto) {
        // find user by id
        if (productDto == null) {
            return null;
        }
        ShopOwner user = shopOwnerRepository.findById(productDto.getUserId()).orElseThrow(
                () -> new ApiRequestException("User not found!")
        );
        return Product.builder()
                .id(productDto.getId())
                .code(productDto.getCode())
                .name(productDto.getName())
                .description(productDto.getDescription())
                .status(productDto.getStatus())
                .price(productDto.getPrice())
                .photoUrl(productDto.getPhotoUrl())
                .weight(productDto.getWeight())
                .height(productDto.getHeight())
                .width(productDto.getWidth())
                .length(productDto.getLength())
                .categories(
                        categoryMapper.mapToEntities(productDto.getCategories(), productDto.getUserId())
                )
                .shopOwner(user)
                .build();

    }

    public ProductDto mapToDto(Product product, ShopOwner owner) {
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
                .photoUrl(product.getPhotoUrl())
                .length(product.getLength())
                .categories(
                        categoryMapper.mapToDtos(product.getCategories())
                )
                .userId(product.getShopOwner().getId())
                .ownerId(owner == null ? null : owner.getId())
                .ownerName(owner == null ? null : String.format("%s %s", owner.getFirstName(), owner.getLastName()))
                .build();
    }
}

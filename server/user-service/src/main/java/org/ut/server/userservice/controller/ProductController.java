package org.ut.server.userservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.ut.server.userservice.common.MessageCode;
import org.ut.server.userservice.common.MessageConstants;
import org.ut.server.userservice.config.JwtUtils;
import org.ut.server.userservice.dto.FileDto;
import org.ut.server.userservice.dto.ProductDto;
import org.ut.server.userservice.dto.response.GenericResponseDTO;
import org.ut.server.userservice.exception.FileUploadException;
import org.ut.server.userservice.model.Product;
import org.ut.server.userservice.service.ProductService;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductController {

    private final ProductService productService;
    private final JwtUtils jwtUtils;

    /*
     * http://localhost:8082/api/v1/products?userId=1
     * */
    @PostMapping(consumes = "application/json;charset=UTF-8")
    public GenericResponseDTO<ProductDto> createProduct(
            @RequestBody ProductDto productDto,
            @RequestHeader("Authorization") String token
    ) {
        UUID userId = UUID.fromString(jwtUtils.extractUserIdFromBearerToken(token));
        productDto.setUserId(userId);
        ProductDto product = productService.createProduct(productDto);
        return GenericResponseDTO.<ProductDto>builder()
                .data(product)
                .code(MessageCode.SUCCESS.toString())
                .message(MessageConstants.CREATED_PRODUCT_SUCCESSFULLY)
                .timestamps(new Date())
                .build();

    }

    /*
     * http://localhost:8081/api/v1/products
     * */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public GenericResponseDTO<List<ProductDto>> getAllProduct(
            @RequestHeader("Authorization") String token
    ) {
//        try {
        UUID userId = UUID.fromString(jwtUtils.extractUserIdFromBearerToken(token));
        List<ProductDto> products = productService.getAllProducts(userId);
        return GenericResponseDTO.<List<ProductDto>>builder()
                .data(products)
                .code(MessageCode.SUCCESS.toString())
                .message(MessageConstants.SUCCESS_GET_ALL_PRODUCTS)
                .timestamps(new Date())
                .build();
//        } catch (Exception e) {
////            e.printStackTrace();
//            log.error(e.getMessage());
//            return GenericResponseDTO.<List<ProductDto>>builder()
//                    .code(e.getMessage())
//                    .timestamps(new Date())
//                    .message(MessageConstant.UNSUCCESSFUL_GET_ORDER)
//                    .build();
//        }
    }

    @GetMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public GenericResponseDTO<ProductDto> getProduct(
            @PathVariable Long productId,
            @RequestHeader("Authorization") String token
    ) {
//        try {
        UUID userId = UUID.fromString(jwtUtils.extractUserIdFromBearerToken(token));
        ProductDto product = productService.getProductById(productId, userId);
        return GenericResponseDTO.<ProductDto>builder()
                .data(product)
                .code(MessageCode.SUCCESS.toString())
                .message(MessageConstants.SUCCESS_GET_PRODUCT)
                .timestamps(new Date())
                .build();
//        } catch (Exception e) {
////            e.printStackTrace();
//            log.error(e.getMessage());
//            return GenericResponseDTO.<ProductDto>builder()
//                    .code(HttpStatus.BAD_REQUEST.toString())
//                    .timestamps(new Date())
//                    .message(e.getMessage())
//                    .build();
//        }
    }

    /*
     * http://localhost:8081/api/v1/products/1
     * */
    @PutMapping("/{productId}")
    public GenericResponseDTO<ProductDto> updateProduct(
            @PathVariable Long productId,
            @RequestBody Product product,
            @RequestHeader("Authorization") String token
    ) {
//        try {
        UUID userId = UUID.fromString(jwtUtils.extractUserIdFromBearerToken(token));
            ProductDto productRes = productService.updateProduct(userId, productId, product);
            return GenericResponseDTO.<ProductDto>builder()
                    .data(productRes)
                    .code(MessageCode.SUCCESS.toString())
                    .message(MessageConstants.UPDATED_PRODUCT_SUCCESSFULLY)
                    .timestamps(new Date())
                    .build();
//        } catch (Exception e) {
//            log.error("Update product error: ", e.getMessage());
//            return GenericResponseDTO.<ProductDto>builder()
//                    .code(e.getMessage())
//                    .timestamps(new Date())
//                    .message(MessageConstants.UPDATED_PRODUCT_UNSUCCESSFULLY)
//                    .build();
//        }
    }

    /*
     * http://localhost:8081/api/v1/products/1
     * */
    @DeleteMapping("/{productId}")
    public GenericResponseDTO<String> deleteProduct(
            @PathVariable Long productId,
            @RequestHeader("Authorization") String token

    ) {
//        try {
        UUID userId = UUID.fromString(jwtUtils.extractUserIdFromBearerToken(token));
            productService.deleteProduct(productId, userId);
            return GenericResponseDTO.<String>builder()
                    .code(MessageCode.SUCCESS.toString())
                    .message(MessageConstants.DELETED_PRODUCT_SUCCESSFULLY)
                    .timestamps(new Date())
                    .build();
//        } catch (Exception e) {
//            log.error("Delete product error: ", e.getMessage());
//            return GenericResponseDTO.<String>builder()
//                    .code(e.getMessage())
//                    .timestamps(new Date())
//                    .message(MessageConstants.DELETED_PRODUCT_UNSUCCESSFULLY)
//                    .build();
//        }
    }

    // proceed product avatar to base64 from  multipart file
    @PostMapping("/image")
    public ResponseEntity<FileDto> uploadProductImage(
            @RequestParam("file") MultipartFile imageFile
    ) throws IOException {
//        try {
            FileDto photo = productService.uploadImage(imageFile);
            return ResponseEntity.ok(photo);
//        } catch (Exception e) {
//            log.error("Image upload error: ", e.getMessage());
//            return ResponseEntity.badRequest().build();
//        }
    }

    @PostMapping("{productId}/image")
    public GenericResponseDTO<ProductDto> uploadProductImage(
            @PathVariable Long productId,
            @RequestParam("file") MultipartFile imageFile,
            @RequestHeader("Authorization") String token
    ) {
        try {
            UUID userId = UUID.fromString(jwtUtils.extractUserIdFromBearerToken(token));
            ProductDto productDto = productService.uploadImageToProduct(productId, imageFile, userId);
            return GenericResponseDTO.<ProductDto>builder()
                    .data(productDto)
                    .code(MessageCode.SUCCESS.toString())
                    .message(MessageConstants.SUCCESS_UPLOAD_IMAGE)
                    .timestamps(new Date())
                    .build();
        } catch (Exception e) {
            log.error("Upload product image error: ", e.getMessage());
            throw new FileUploadException(e.getMessage());
        }
    }

}

package com.ut.server.productservice.controller;

import com.ut.server.productservice.common.MessageConstant;
import com.ut.server.productservice.dto.ProductRequest;
import com.ut.server.productservice.dto.ProductResponse;
import com.ut.server.productservice.exception.MessageCode;
import com.ut.server.productservice.model.Product;
import com.ut.server.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.ut.server.authservice.server.common.dtos.GenericResponseDTO;

import javax.transaction.Transactional;
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

    /*
     * http://localhost:8082/api/v1/products?userId=1
     * */
    @PostMapping(consumes = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.CREATED)
    public GenericResponseDTO<ProductResponse> createProduct(@RequestBody ProductRequest productRequest, @RequestParam UUID userId) {

        try {
            ProductResponse product = productService.createProduct(productRequest, userId);
            return GenericResponseDTO.<ProductResponse>builder()
                    .data(product)
                    .code(MessageCode.SUCCESS.toString())
                    .message(MessageConstant.SUCCESS_GET_ORDER)
                    .timestamps(new Date())
                    .build();
        }
        catch (Exception e){
            log.error(e.getMessage());
            return GenericResponseDTO.<ProductResponse>builder()
                    .code(e.getMessage())
                    .timestamps(new Date())
                    .message(MessageConstant.UNSUCCESSFUL_GET_ORDER)
                    .build();
        }
    }

    /*
    * http://localhost:8081/api/v1/products?userId=1
    * */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public GenericResponseDTO<List<ProductResponse>> getAllProduct(@RequestParam UUID userId) {
        try {
            List<ProductResponse> products = productService.getAllProducts(userId);
            return GenericResponseDTO.<List<ProductResponse>>builder()
                    .data(products)
                    .code(MessageCode.SUCCESS.toString())
                    .message(MessageConstant.SUCCESS_GET_ORDER)
                    .timestamps(new Date())
                    .build();
        } catch (Exception e) {
//            e.printStackTrace();
            log.error(e.getMessage());
            return GenericResponseDTO.<List<ProductResponse>>builder()
                    .code(e.getMessage())
                    .timestamps(new Date())
                    .message(MessageConstant.UNSUCCESSFUL_GET_ORDER)
                    .build();
        }
    }



    @GetMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public GenericResponseDTO<ProductResponse> getProduct(@PathVariable Long productId, @RequestParam UUID userId) {
        try {
            ProductResponse product = productService.getProductById(productId, userId);
            return GenericResponseDTO.<ProductResponse>builder()
                    .data(product)
                    .code(MessageCode.SUCCESS.toString())
                    .message(MessageConstant.SUCCESS_GET_ORDER)
                    .timestamps(new Date())
                    .build();
        } catch (Exception e) {
//            e.printStackTrace();
            log.error(e.getMessage());
            return GenericResponseDTO.<ProductResponse>builder()
                    .code(e.getMessage())
                    .timestamps(new Date())
                    .message(MessageConstant.UNSUCCESSFUL_GET_ORDER)
                    .build();
        }
    }

    /*
     * http://localhost:8081/api/v1/products/1?userId=1
     * */
    @PutMapping("/{productId}")
    public GenericResponseDTO<ProductResponse> updateProduct(@RequestParam UUID userId, @PathVariable Long productId, @RequestBody Product product) {
        try {
            ProductResponse productRes = productService.updateProduct(userId, productId, product);
            return GenericResponseDTO.<ProductResponse>builder()
                    .data(productRes)
                    .code(MessageCode.SUCCESS.toString())
                    .message(MessageConstant.SUCCESS_ORDER_UPDATED)
                    .timestamps(new Date())
                    .build();
        }
        catch (Exception e) {
            log.error("Update product error: ",e.getMessage());
            return GenericResponseDTO.<ProductResponse>builder()
                    .code(e.getMessage())
                    .timestamps(new Date())
                    .message(MessageConstant.UNSUCCESSFUL_ORDER_UPDATED)
                    .build();
        }
    }

    /*
     * http://localhost:8081/api/v1/products/1
     * */
    @DeleteMapping("/{id}")
    public GenericResponseDTO<String> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return GenericResponseDTO.<String>builder()
                    .code(MessageCode.SUCCESS.toString())
                    .message(MessageConstant.SUCCESS_ORDER_DELETED)
                    .timestamps(new Date())
                    .build();
        }
        catch (Exception e) {
            log.error("Delete product error: ",e.getMessage());
            return GenericResponseDTO.<String>builder()
                    .code(e.getMessage())
                    .timestamps(new Date())
                    .message(MessageConstant.UNSUCCESSFUL_ORDER_DELETED)
                    .build();
        }
    }
}

package com.ut.server.productservice.controller;

import com.ut.server.productservice.dto.ProductRequest;
import com.ut.server.productservice.dto.ProductResponse;
import com.ut.server.productservice.model.Product;
import com.ut.server.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Transactional
public class ProductController {

    private final ProductService productService;

    /*
     * http://localhost:8082/api/v1/products?userId=1
     * */
    @PostMapping(consumes = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody ProductRequest productRequest, @RequestParam UUID userId) {
        productService.createProduct(productRequest, userId);
    }

    /*
    * http://localhost:8081/api/v1/products?userId=1
    * */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ProductResponse>> getAllProduct(@RequestParam UUID userId) {
        return ResponseEntity.ok(productService.getAllProducts(userId));
    }

    /*
     * http://localhost:8081/api/v1/products/1?userId=1
     * */
    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(@RequestParam UUID userId, @PathVariable Long productId, @RequestBody Product product) {
        return ResponseEntity.ok(productService.updateProduct(userId, productId, product));
    }

    /*
     * http://localhost:8081/api/v1/products/1
     * */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.deleteProduct(id));
    }
}

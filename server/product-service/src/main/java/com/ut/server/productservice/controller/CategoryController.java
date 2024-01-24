package com.ut.server.productservice.controller;

import com.ut.server.productservice.common.MessageConstant;
import com.ut.server.productservice.dto.CategoryRequest;
import com.ut.server.productservice.dto.CategoryResponse;
import com.ut.server.productservice.dto.ProductRequest;
import com.ut.server.productservice.dto.ProductResponse;
import com.ut.server.productservice.exception.MessageCode;
import com.ut.server.productservice.model.Category;
import com.ut.server.productservice.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.ut.server.authservice.server.common.dtos.GenericResponseDTO;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CategoryController {
    private final CategoryService categoryService;

//    public GenericResponseDTO<List<CategoryResponse>> getAllCategories() {
//
//    }

    @PostMapping(consumes = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.CREATED)
    public GenericResponseDTO<CategoryResponse> addCategory(@RequestBody CategoryRequest categoryRequest, @RequestParam UUID userId) {

        try {
            CategoryResponse category = categoryService.createCategory(categoryRequest, userId);
            return GenericResponseDTO.<CategoryResponse>builder()
                    .data(category)
                    .code(MessageCode.SUCCESS.toString())
                    .message(MessageConstant.SUCCESS_GET_ORDER)
                    .timestamps(new Date())
                    .build();
        }
        catch (Exception e){
            log.error(e.getMessage());
            return GenericResponseDTO.<CategoryResponse>builder()
                    .code(e.getMessage())
                    .timestamps(new Date())
                    .message(MessageConstant.UNSUCCESSFUL_GET_ORDER)
                    .build();
        }
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public GenericResponseDTO<List<CategoryResponse>> getAllCategories(@RequestParam UUID userId) {
        try {
            List<CategoryResponse> categories = categoryService.getAllCategories(userId);
            return GenericResponseDTO.<List<CategoryResponse>>builder()
                    .data(categories)
                    .code(MessageCode.SUCCESS.toString())
                    .message(MessageConstant.SUCCESS_GET_ORDER)
                    .timestamps(new Date())
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return GenericResponseDTO.<List<CategoryResponse>>builder()
                    .code(e.getMessage())
                    .timestamps(new Date())
                    .message(MessageConstant.UNSUCCESSFUL_GET_ORDER)
                    .build();
        }
    }

    @GetMapping("/{category}")
    @ResponseStatus(HttpStatus.OK)
    public GenericResponseDTO<List<ProductResponse>> getProductByCategory(@PathVariable Long category) {
        try {
            List<ProductResponse> products = categoryService.getProductByCategory(category);
            return GenericResponseDTO.<List<ProductResponse>>builder()
                    .data(products)
                    .code(MessageCode.SUCCESS.toString())
                    .message(MessageConstant.SUCCESS_GET_ORDER)
                    .timestamps(new Date())
                    .build();
        } catch (Exception e) {
//
            log.error(e.getMessage());
            return GenericResponseDTO.<List<ProductResponse>>builder()
                    .code(e.getMessage())
                    .timestamps(new Date())
                    .message(MessageConstant.UNSUCCESSFUL_GET_ORDER)
                    .build();
        }
    }
}

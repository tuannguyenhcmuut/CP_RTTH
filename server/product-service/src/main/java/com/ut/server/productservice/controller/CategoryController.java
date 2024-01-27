package com.ut.server.productservice.controller;

import com.ut.server.productservice.common.MessageConstant;
import com.ut.server.productservice.dto.CategoryDto;
import com.ut.server.productservice.model.Category;
import com.ut.server.productservice.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.ut.server.common.dtos.GenericResponseDTO;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
@Transactional
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping(consumes = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.CREATED)
    public GenericResponseDTO<CategoryDto> createCategory(@RequestBody CategoryDto category,
                                                          @RequestHeader("userId") UUID userId) {
        try {
//            category.setUserId(userId);
            CategoryDto newCategory = categoryService.createCategory(category, userId);
            return GenericResponseDTO.<CategoryDto>builder()
                    .data(newCategory)
                    .code(MessageConstant.SUCCESS_GET_ORDER)
                    .message(MessageConstant.SUCCESS_GET_ORDER)
                    .timestamps(new Date())
                    .build();

        } catch (Exception e) {
            log.error(e.getMessage());
            return GenericResponseDTO.<CategoryDto>builder()
                    .code(e.getMessage())
                    .timestamps(new Date())
                    .message(MessageConstant.UNSUCCESSFUL_GET_ORDER)
                    .build();
        }
    }

    // get all category by userId
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public GenericResponseDTO<List<CategoryDto>> getAllCategoryByUserId(@RequestHeader("userId") UUID userId) {
        try {
            List<CategoryDto> category = categoryService.getAllCategoryByUserId(userId);
            return GenericResponseDTO.<List<CategoryDto>>builder()
                    .data(category)
                    .code(MessageConstant.SUCCESS_GET_ORDER)
                    .message(MessageConstant.SUCCESS_GET_ORDER)
                    .timestamps(new Date())
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return GenericResponseDTO.<List<CategoryDto>>builder()
                    .code(e.getMessage())
                    .timestamps(new Date())
                    .message(MessageConstant.UNSUCCESSFUL_GET_ORDER)
                    .build();
        }
    }
}
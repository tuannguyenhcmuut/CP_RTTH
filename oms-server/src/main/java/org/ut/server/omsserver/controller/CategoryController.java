package org.ut.server.omsserver.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.ut.server.omsserver.common.MessageCode;
import org.ut.server.omsserver.common.MessageConstants;
import org.ut.server.omsserver.config.JwtUtils;
import org.ut.server.omsserver.dto.CategoryDto;
import org.ut.server.omsserver.dto.response.GenericResponseDTO;
import org.ut.server.omsserver.service.CategoryService;
import org.ut.server.omsserver.utils.RestParamUtils;

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
    private final JwtUtils jwtUtils;

    @PostMapping(consumes = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.CREATED)
    public GenericResponseDTO<CategoryDto> createCategory(@RequestBody CategoryDto category,
                                                          @RequestHeader("Authorization") String token) {
//        try {
//            category.setUserId(userId);
            UUID userId = UUID.fromString(jwtUtils.extractUserIdFromBearerToken(token));
            CategoryDto newCategory = categoryService.createCategory(category, userId);
            return GenericResponseDTO.<CategoryDto>builder()
                    .data(newCategory)
                    .code(MessageCode.SUCCESS.toString())
                    .message(MessageConstants.SUCCESS_GET_CATE)
                    .timestamps(new Date())
                    .build();

//        } catch (Exception e) {
//            log.error(e.getMessage());
//            return GenericResponseDTO.<CategoryDto>builder()
//                    .code(e.getMessage())
//                    .timestamps(new Date())
//                    .message(MessageConstants.UNSUCCESSFUL_GET_CATE)
//                    .build();
//        }
    }

    // get all category by userId
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public GenericResponseDTO<List<CategoryDto>> getAllCategoryByUserId(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "categoryName,asc") String[] sort
    ) {
//        try {
            UUID userId = UUID.fromString(jwtUtils.extractUserIdFromBearerToken(token));
            Pageable pageable = RestParamUtils.getPageable(page, size, sort);
            List<CategoryDto> category = categoryService.getAllCategoryByUserId(userId, pageable);
            return GenericResponseDTO.<List<CategoryDto>>builder()
                    .data(category)
                    .code(MessageCode.SUCCESS.toString())
                    .message(MessageConstants.SUCCESS_GET_CATE)
                    .timestamps(new Date())
                    .build();
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            return GenericResponseDTO.<List<CategoryDto>>builder()
//                    .code(e.getMessage())
//                    .timestamps(new Date())
//                    .message(MessageConstants.UNSUCCESSFUL_GET_CATE)
//                    .build();
//        }
    }
}
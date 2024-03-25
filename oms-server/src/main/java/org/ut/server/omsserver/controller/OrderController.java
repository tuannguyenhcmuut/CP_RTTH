package org.ut.server.omsserver.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.ut.server.omsserver.common.MessageCode;
import org.ut.server.omsserver.common.MessageConstants;
import org.ut.server.omsserver.config.JwtUtils;
import org.ut.server.omsserver.dto.OrderDto;
import org.ut.server.omsserver.dto.PriceDto;
import org.ut.server.omsserver.dto.request.OrderOptionRequest;
import org.ut.server.omsserver.dto.request.OrderRequest;
import org.ut.server.omsserver.dto.response.GenericResponseDTO;
import org.ut.server.omsserver.service.OrderService;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderController {
    //    jwtUtils
    private final JwtUtils jwtUtils;
    private final OrderService orderService;

    // retrieve all orders of a user
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public GenericResponseDTO<List<OrderDto>> getAllOrders(
            @RequestHeader("Authorization") String token
    ) {
//        try {
        UUID userId = UUID.fromString(jwtUtils.extractUserIdFromBearerToken(token));
        List<OrderDto> orders = orderService.getAllOrders(userId);

        return GenericResponseDTO.<List<OrderDto>>builder()
                .data(orders)
                .code(MessageCode.SUCCESS.toString())
                .message(MessageConstants.SUCCESS_GET_ORDER)
                .timestamps(new Date())
                .build();
//        }
//        catch (Exception e){
//            log.error(e.getMessage());
//            return GenericResponseDTO.<List<OrderDto>>builder()
//                            .code(e.getMessage())
//                            .timestamps(new Date())
//                            .message(MessageConstants.UNSUCCESSFUL_GET_ORDER)
//                    .build();
//        }
    }

    // retrieve an order of a user by order id
    // http://localhost:8083/api/v1/order/1/user/1
    @GetMapping("/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public GenericResponseDTO<OrderDto> getOrderById(
            @RequestHeader("Authorization") String token,
            @PathVariable Long orderId
    ) {
//        try {
        UUID userId = UUID.fromString(jwtUtils.extractUserIdFromBearerToken(token));
        OrderDto orderDto = orderService.getOrderById(userId, orderId);
        return GenericResponseDTO.<OrderDto>builder()
                .data(orderDto)
                .code(MessageCode.SUCCESS.toString())
                .message(MessageConstants.SUCCESS_GET_ORDER)
                .timestamps(new Date())
                .build();
//        }
//        catch (Exception e){
//            log.error(MessageCode.CREATED_FAILED.toString());
//            return GenericResponseDTO.<OrderDto>builder()
//                    .code(e.getMessage())
//                    .timestamps(new Date())
//                    .build();
//        }
    }

    // create an order
    // http://localhost:8083/api/v1/order/user/1
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public GenericResponseDTO<OrderDto> createOrder(
            @RequestBody OrderRequest orderRequest,
            @RequestHeader("Authorization") String token
    ) {
//        try {
        UUID userId = UUID.fromString(jwtUtils.extractUserIdFromBearerToken(token));
        orderRequest.setUserId(userId);
        OrderDto newOrderDto = orderService.createOrder(orderRequest);
        return GenericResponseDTO.<OrderDto>builder()
                .data(newOrderDto)
                .code(MessageCode.CREATED_SUCCESS.toString())
                .message(MessageConstants.SUCCESS_ORDER_CREATED)
                .timestamps(new Date())
                .build();
//        }
//        catch (Exception e){
//            log.error(String.format("[CREATED ORDER FAILED: %s]",e.getMessage()));
//            return GenericResponseDTO.<OrderDto>builder()
//                    .code(e.getMessage())
//                    .timestamps(new Date())
//                    .message(MessageConstants.UNSUCCESSFUL_ORDER_CREATED)
//                    .build();
//        }
    }

    // update an order status
//    @PatchMapping("/{order_id}/user/{userId}/status/{status_value}")
//    public ResponseEntity<String> updateOrderStatus(@PathVariable UUID userId, @PathVariable Long order_id, @PathVariable StatusRequest statusRequest) {
//        return orderService.updateOrderStatus(userId, order_id, statusRequest);
//    }


    // delete an order
    @DeleteMapping("/{orderId}")
    public GenericResponseDTO<String> deleteOrder(
            @PathVariable Long orderId,
            @RequestHeader("Authorization") String token
    ) {
//        try {
        UUID userId = UUID.fromString(jwtUtils.extractUserIdFromBearerToken(token));
        orderService.deleteOrder(userId, orderId);
        return GenericResponseDTO.<String>builder()
                .code(MessageCode.SUCCESS.toString())
                .message(MessageConstants.SUCCESS_ORDER_DELETED)
                .timestamps(new Date())
                .build();
//        }
//        catch (Exception e) {
//            log.error(MessageCode.CREATED_FAILED.toString());
//            return GenericResponseDTO.<String>builder()
//                    .code(e.getMessage())
//                    .timestamps(new Date())
//                    .message(MessageConstants.UNSUCCESSFUL_ORDER_DELETED)
//                    .build();
//        }
    }

    // retrieve order form information for order: orderOptionType, receipt by userId, products by userId, store by userId


    // http://localhost:8083/api/v1/order/2/user/1/receiver/1
    @PatchMapping("/{orderId}/receiver/{receiverId}")
    public GenericResponseDTO<OrderDto> updateReceiver(
            @PathVariable Long orderId,
            @PathVariable Long receiverId,
            @RequestHeader("Authorization") String token
    ) {
//        try {
        UUID userId = UUID.fromString(jwtUtils.extractUserIdFromBearerToken(token));
        OrderDto orderDto = orderService.updateReceiver(userId, orderId, receiverId);
        return GenericResponseDTO.<OrderDto>builder()
                .data(orderDto)
                .code(MessageCode.SUCCESS.toString())
                .message(MessageConstants.SUCCESS_ORDER_UPDATED)
                .timestamps(new Date())
                .build();
//        }
//        catch (Exception e){
//            log.error(MessageCode.CREATED_FAILED.toString());
//            return GenericResponseDTO.<OrderDto>builder()
//                    .code(e.getMessage())
//                    .timestamps(new Date())
//                    .message(MessageConstants.UNSUCCESSFUL_ORDER_UPDATED)
//                    .build();
//        }
    }

    // calculate the total price and option's prices based on order options 
    @PostMapping("/price/calculate")
    public List<PriceDto> postMethodName(
        @RequestBody List<OrderOptionRequest> options,
        @RequestHeader("Authorization") String token
    ) {
        //TODO: process POST request
        // build sample priceDto
        List<PriceDto> priceDtos = new ArrayList<>();
        priceDtos.add(PriceDto.builder()
                .serviceCode("STD")
                .serviceName("Standard Shipping")
                .price(5000)
                .build());
        priceDtos.add(PriceDto.builder()
                .serviceCode("EXP")
                .serviceName("Express Shipping")
                .price(10000)
                .build());
        priceDtos.add(PriceDto.builder()
                .serviceCode("INT")
                .serviceName("ALL")
                .price(20000)
                .exchangeWeight(1000)
                .build());
        return priceDtos;
        
        
    }
    



    // update receiver


}

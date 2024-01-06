package com.ut.server.orderservice.controller;

import com.ut.server.orderservice.common.MessageConstant;
import com.ut.server.orderservice.dto.response.OrderOptionResponse;
import com.ut.server.orderservice.dto.OrderDto;
import com.ut.server.orderservice.exception.MessageCode;
import com.ut.server.orderservice.service.OrderService;
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
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderController {

    private final OrderService orderService;
    // retrieve all orders of a user
    @GetMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public GenericResponseDTO<List<OrderDto>> getAllOrders(@PathVariable UUID userId) {
        try {

            List<OrderDto> orders = orderService.getAllOrders(userId);
            return GenericResponseDTO.<List<OrderDto>>builder()
                    .data(orders)
                    .code(MessageCode.SUCCESS.toString())
                    .message(MessageConstant.SUCCESS_GET_ORDER)
                    .timestamps(new Date())
                    .build();
        }
        catch (Exception e){
            log.error(e.getMessage());
            return GenericResponseDTO.<List<OrderDto>>builder()
                    .code(e.getMessage())
                    .timestamps(new Date())
                    .message(MessageConstant.UNSUCCESSFUL_GET_ORDER)
                    .build();
        }
    }

    // retrieve an order of a user by order id
    // http://localhost:8083/api/v1/order/1/user/1
    @GetMapping("/{orderId}/user/{userId}")
    public GenericResponseDTO<OrderDto> getOrderById(@PathVariable UUID userId, @PathVariable Long orderId) {
        try {
            OrderDto orderDto = orderService.getOrderById(userId, orderId);
            return GenericResponseDTO.<OrderDto>builder()
                    .data(orderDto)
                    .code(MessageCode.SUCCESS.toString())
                    .message("OK")
                    .timestamps(new Date())
                    .build();
        }
        catch (Exception e){
            log.error(MessageCode.CREATED_FAILED.toString());
            return GenericResponseDTO.<OrderDto>builder()
                    .code(e.getMessage())
                    .timestamps(new Date())
                    .build();
        }
    }

    // create an order
    // http://localhost:8083/api/v1/order/user/1
    @PostMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public GenericResponseDTO<OrderDto> createOrder(@PathVariable UUID userId, @RequestBody OrderDto orderDto) {
        try {
            OrderDto newOrderDto = orderService.createOrder(userId, orderDto);
            return GenericResponseDTO.<OrderDto>builder()
                    .data(newOrderDto)
                    .code(MessageCode.CREATED_SUCCESS.toString())
                    .message(MessageConstant.SUCCESS_ORDER_CREATED)
                    .timestamps(new Date())
                    .build();
        }
        catch (Exception e){
            log.error(String.format("[CREATED ORDER FAILED: %s]",e.getMessage()));
            return GenericResponseDTO.<OrderDto>builder()
                    .code(e.getMessage())
                    .timestamps(new Date())
                    .message(MessageConstant.UNSUCCESSFUL_ORDER_CREATED)
                    .build();
        }
    }
    // retrieve order form information for order: orderOptionType, receipt by userId, products by userId, store by userId


    // http://localhost:8083/api/v1/order/2/user/1/receiver/1
    @PatchMapping("/{orderId}/user/{userId}/receiver/{receiverId}")
    public GenericResponseDTO<OrderDto> updateReceiver(@PathVariable UUID userId, @PathVariable Long orderId, @PathVariable Long receiverId) {
        try {
            OrderDto orderDto = orderService.updateReceiver(userId, orderId, receiverId);
            return GenericResponseDTO.<OrderDto>builder()
                    .data(orderDto)
                    .code(MessageCode.SUCCESS.toString())
                    .message(MessageConstant.SUCCESS_ORDER_UPDATED)
                    .timestamps(new Date())
                    .build();
        }
        catch (Exception e){
            log.error(MessageCode.CREATED_FAILED.toString());
            return GenericResponseDTO.<OrderDto>builder()
                    .code(e.getMessage())
                    .timestamps(new Date())
                    .message(MessageConstant.UNSUCCESSFUL_ORDER_UPDATED)
                    .build();
        }
    }
    // update receiver

    // update an order status
//    @PatchMapping("/{order_id}/user/{userId}/status/{status_value}")
//    public ResponseEntity<String> updateOrderStatus(@PathVariable UUID userId, @PathVariable Long order_id, @PathVariable StatusRequest statusRequest) {
//        return orderService.updateOrderStatus(userId, order_id, statusRequest);
//    }


    // delete an order
    @DeleteMapping("/{orderId}/user/{userId}")
    public GenericResponseDTO<String> deleteOrder(@PathVariable UUID userId, @PathVariable Long orderId) {
        try {
            orderService.deleteOrder(userId, orderId);
            return GenericResponseDTO.<String>builder()
                    .code(MessageCode.SUCCESS.toString())
                    .message(MessageConstant.SUCCESS_ORDER_DELETED)
                    .timestamps(new Date())
                    .build();
        }
        catch (Exception e) {
            log.error(MessageCode.CREATED_FAILED.toString());
            return GenericResponseDTO.<String>builder()
                    .code(e.getMessage())
                    .timestamps(new Date())
                    .message(MessageConstant.UNSUCCESSFUL_ORDER_DELETED)
                    .build();
        }
    }

    // retrieve all order options of an order
    @GetMapping("/{orderId}/user/{userId}/orderOptions")
    public List<OrderOptionResponse> getAllOrderOptions(@PathVariable UUID userId, @PathVariable Long orderId) {
        return orderService.getAllOrderOptions(userId, orderId);
    }
}

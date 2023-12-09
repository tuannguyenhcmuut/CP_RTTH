package com.ut.server.orderservice.controller;

import com.ut.server.orderservice.dto.OrderOptionResponse;
import com.ut.server.orderservice.dto.OrderRequest;
import com.ut.server.orderservice.dto.OrderResponse;
import com.ut.server.orderservice.dto.StatusRequest;
import com.ut.server.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
@Transactional
public class OrderController {

    private final OrderService orderService;
    // retrieve all orders of a user
    @GetMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<OrderResponse>> getAllOrders(@PathVariable UUID userId) {

        return orderService.getAllOrders(userId);  // List<OrderResponse>
    }

    // retrieve an order of a user by order id
    // http://localhost:8083/api/v1/order/1/user/1
    @GetMapping("/{order_id}/user/{userId}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable UUID userId, @PathVariable Long order_id) {
        return  orderService.getOrderById(userId, order_id);
    }

    // create an order
    // http://localhost:8083/api/v1/order/user/1
    @PostMapping("/user/{userId}")
    public ResponseEntity<?> createOrder(@PathVariable UUID userId, @RequestBody OrderRequest orderRequest) {
        return orderService.createOrder(userId, orderRequest);
    }
    // retrieve order form information for order: orderOptionType, receipt by userId, products by userId, store by userId


    // http://localhost:8083/api/v1/order/2/user/1/receiver/1
    @PatchMapping("/{order_id}/user/{userId}/receiver/{receiver_id}")
    public String updateReceiver(@PathVariable UUID userId, @PathVariable Long order_id, @PathVariable String receiver_id) {
        return orderService.updateReceiver(userId, order_id, receiver_id);
    }
    // update receiver

    // update an order status
    @PatchMapping("/{order_id}/user/{userId}/status/{status_value}")
    public ResponseEntity<String> updateOrderStatus(@PathVariable UUID userId, @PathVariable Long order_id, @PathVariable StatusRequest statusRequest) {
        return orderService.updateOrderStatus(userId, order_id, statusRequest);
    }


    // delete an order
    @DeleteMapping("/{order_id}/user/{userId}")
    public String deleteOrder(@PathVariable UUID userId, @PathVariable Long order_id) {
        return orderService.deleteOrder(userId, order_id);
    }

    // retrieve all order options of an order
    @GetMapping("/{order_id}/user/{userId}/orderOptions")
    public List<OrderOptionResponse> getAllOrderOptions(@PathVariable UUID userId, @PathVariable Long order_id) {
        return orderService.getAllOrderOptions(userId, order_id);
    }




}

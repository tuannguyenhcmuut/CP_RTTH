package com.ut.server.orderservice.controller;

import com.ut.server.orderservice.dto.OrderOptionResponse;
import com.ut.server.orderservice.dto.OrderRequest;
import com.ut.server.orderservice.dto.OrderResponse;
import com.ut.server.orderservice.dto.StatusRequest;
import com.ut.server.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user/{user_id}/orders")
@RequiredArgsConstructor
@Transactional
public class OrderController {

    private final OrderService orderService;
    // retrieve all orders of a user
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OrderResponse> getAllOrders(@PathVariable Long user_id) {

        return orderService.getAllOrders(user_id);
    }

    // retrieve an order of a user by order id
    @GetMapping("/{order_id}")
    public OrderResponse getOrderById(@PathVariable Long user_id, @PathVariable Long order_id) {
        return  orderService.getOrderById(user_id, order_id);
    }

    // create an order
    @PostMapping
    public String createOrder(@PathVariable Long user_id, @RequestBody OrderRequest orderRequest) {
        return orderService.createOrder(user_id, orderRequest);
    }
    // retrieve order form information for order: orderOptionType, receipt by userId, products by userId, store by userId



    @PatchMapping("/{order_id}/receiver/{receiver_id}")
    public String updateReceiver(@PathVariable Long user_id, @PathVariable Long order_id, @PathVariable String receiver_id) {
        return orderService.updateReceiver(user_id, order_id, receiver_id);
    }
    // update receiver

    // update an order status
    @PatchMapping("/{order_id}/status/{status_value}")
    public String updateOrderStatus(@PathVariable Long user_id, @PathVariable Long order_id, @PathVariable StatusRequest statusRequest) {
        return orderService.updateOrderStatus(user_id, order_id, statusRequest);
    }


    // delete an order
    @DeleteMapping("/{order_id}")
    public String deleteOrder(@PathVariable Long user_id, @PathVariable Long order_id) {
        return orderService.deleteOrder(user_id, order_id);
    }

    // retrieve all order options of an order
    @GetMapping("/{order_id}/orderOptions")
    public List<OrderOptionResponse> getAllOrderOptions(@PathVariable Long user_id, @PathVariable Long order_id) {
        return orderService.getAllOrderOptions(user_id, order_id);
    }




}

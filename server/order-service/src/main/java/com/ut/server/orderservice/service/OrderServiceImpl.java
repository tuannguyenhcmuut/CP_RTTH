package com.ut.server.orderservice.service;

import com.ut.server.orderservice.dto.OrderOptionResponse;
import com.ut.server.orderservice.dto.OrderRequest;
import com.ut.server.orderservice.dto.OrderResponse;
import com.ut.server.orderservice.dto.StatusRequest;
import com.ut.server.orderservice.model.Order;
import com.ut.server.orderservice.model.Status;
import com.ut.server.orderservice.repo.OrderRepository;
import com.ut.server.orderservice.repo.StatusRepository;
import com.ut.server.orderservice.utils.OrderUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.function.EntityResponse;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final StatusRepository statusRepository;

    @Override
    public ResponseEntity<OrderResponse> getOrderById(UUID userId, Long order_id) {
        Order order = orderRepository.findOrderByIdAndUserId(order_id, userId);


        // ok, found!
        OrderResponse orderResponse =  OrderResponse.builder()
                            .id(order.getId())
                            .code(order.getCode())
                            .height(order.getHeight())
                            .width(order.getWidth())
                            .depth(order.getDepth())
                            .storeId(order.getStoreId())
                            .receiverId(order.getReceiverId())
                            .statusId(order.getStatusId())
                            .price(order.getPrice())
                            .discountId(order.getDiscountId())
                            .shipId(order.getShipId())
                            .orderOptions(order.getOrderOptions())
                            .build();
        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }


    public String updateReceiver(UUID userId, Long order_id, String receiver_id) {
        return null;
    }

    public ResponseEntity<String> updateOrderStatus(UUID userId, Long order_id, StatusRequest statusRequest){
        Order order = orderRepository.findOrderByIdAndUserId(order_id, userId);
        Status status = statusRepository.findById(statusRequest.getStatusId()).orElse(null);
        if (order == null) {
            return new ResponseEntity<>("Order not found", HttpStatus.BAD_REQUEST);
        }
        if (status == null) {
            return new ResponseEntity<>("Status not found", HttpStatus.BAD_REQUEST);
        }
        // update status
        order.setStatusId(status);
        // order.setLastStatusUpdate(LocalDateTime.now());
        orderRepository.save(order);
        return new ResponseEntity<>("Updated order status successfully", HttpStatus.OK);
    }

    public String deleteOrder(UUID userId, Long order_id){
        Order order = orderRepository.findOrderByIdAndUserId(order_id, userId);
        if (order == null) {
            return "Order not found";
        }
        orderRepository.delete(order);
        return "Deleted order successfully";
    }

    public List<OrderOptionResponse> getAllOrderOptions(UUID userId, Long order_id){
        Order order = orderRepository.findOrderByIdAndUserId(order_id, userId);
        // mapping o
        //

        return null;
    }

    public ResponseEntity<List<OrderResponse>> getAllOrders(UUID userId){
        List<Order> orders = orderRepository.findOrdersByUserId(userId);
        // mapping
        if (orders == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<OrderResponse> orderResponse = orders.stream().map(order -> OrderUtils.mapOrderToOrderResponse(order))
                .collect(Collectors.toList());

        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }

    public ResponseEntity<?> createOrder(UUID userId, OrderRequest orderRequest){
        // tam thoi la chua validate
        Order newOrder = Order.builder()
                .height(orderRequest.getHeight())
                .width(orderRequest.getWidth())
                .depth(orderRequest.getDepth())
                .userId(orderRequest.getUserId())
                .storeId(orderRequest.getStoreId())
                .receiverId(orderRequest.getReceiverId())
                .statusId(orderRequest.getStatusId())
                .price(orderRequest.getPrice())
                .discountId(orderRequest.getDiscountId())
                .orderOptions(orderRequest.getOrderOptions())
                .build();


        // tu dong tao 1 ship id moi, gui request toi shipping service voi param la orderid
        // tao code moi
        newOrder.setCode(OrderUtils.generateOrderCode(newOrder.getId()));
        orderRepository.save(newOrder);
        return new ResponseEntity<>("Created order successfully",HttpStatus.CREATED);
    }
}

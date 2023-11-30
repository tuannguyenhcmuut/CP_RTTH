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
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final StatusRepository statusRepository;

    @Override
    public OrderResponse getOrderById(Long user_id, Long order_id) {
        Order order = orderRepository.findOrderByIdAndUserId(order_id, user_id);


        // ok, found!
        return OrderResponse.builder()
                            .id(order.getId())
                            .code(order.getCode())
                            .height(order.getHeight())
                            .width(order.getWidth())
                            .depth(order.getDepth())
                            .storeId(order.getStoreId())
                            .receiverId(order.getReceiverId())
                            .statusId(order.getStatusId())
                            .price(order.getPrice())
                            .discount_id(order.getDiscount_id())
                            .shipId(order.getShipId())
                            .orderOptions(order.getOrderOptions())
                            .build();
    }


    public String updateReceiver(Long user_id, Long order_id, String receiver_id) {
        return null;
    }

    public String updateOrderStatus(Long user_id, Long order_id, StatusRequest statusRequest){
        Order order = orderRepository.findOrderByIdAndUserId(order_id, user_id);
        Status status = statusRepository.findById(statusRequest.getStatusId()).orElse(null);
        if (order == null) {
            return "Order not found";
        }
        if (status == null) {
            return "Status not found";
        }
        // update status
        order.setStatusId(status);
        // order.setLastStatusUpdate(LocalDateTime.now());
        orderRepository.save(order);
        return "Updated order status successfully";
    }

    public String deleteOrder(Long user_id, Long order_id){
        Order order = orderRepository.findOrderByIdAndUserId(order_id, user_id);
        if (order == null) {
            return "Order not found";
        }
        orderRepository.delete(order);
        return "Deleted order successfully";
    }

    public List<OrderOptionResponse> getAllOrderOptions(Long user_id, Long order_id){
        Order order = orderRepository.findOrderByIdAndUserId(order_id, user_id);
        // mapping o
        //

        return null;
    }

    public List<OrderResponse> getAllOrders(Long user_id){
        return null;
    }

    public String createOrder(Long user_id, OrderRequest orderRequest){
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
                .discount_id(orderRequest.getDiscount_id())
                .orderOptions(orderRequest.getOrderOptions())
                .build();


        // tu dong tao 1 ship id moi, gui request toi shipping service voi param la orderid
        // tao code moi
        newOrder.setCode(OrderUtils.generateOrderCode(newOrder.getId()));
        orderRepository.save(newOrder);
        return "Created order successfully";
    }
}

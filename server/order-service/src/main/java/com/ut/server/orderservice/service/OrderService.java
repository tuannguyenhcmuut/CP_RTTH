package com.ut.server.orderservice.service;

import com.ut.server.orderservice.common.MessageConstant;
import com.ut.server.orderservice.config.UserFeign;
import com.ut.server.orderservice.dto.response.OrderOptionResponse;
import com.ut.server.orderservice.dto.OrderDto;
import com.ut.server.orderservice.mapper.OrderMapper;
import com.ut.server.orderservice.mapper.OrderOptionMapper;
import com.ut.server.orderservice.model.Order;
import com.ut.server.orderservice.repo.OrderRepository;
import com.ut.server.orderservice.utils.OrderUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.ut.server.common.events.OrderStatus;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderOptionMapper orderOptionMapper;
    private final UserFeign userFeign;


    public OrderDto getOrderById(UUID userId, Long order_id) {
        Order order = orderRepository.findOrderByIdAndUserId(order_id, userId);

        if (order == null) {
            throw new RuntimeException("Order not found");
        }
        // ok, found!
        return OrderDto.builder()
                            .id(order.getId())
                            .code(order.getCode())
                            .height(order.getHeight())
                            .width(order.getWidth())
                            .depth(order.getDepth())
                            .storeId(order.getStoreId())
                            .receiverId(order.getReceiverId())
                            .orderStatus(order.getOrderStatus().toString())
                            .price(order.getPrice())
                            .shipId(order.getShipId())
                            .orderOptions(orderOptionMapper.mapToDtos(order.getOrderOptions()))
                            .build();
    }


    public OrderDto createOrder(UUID userId, OrderDto orderDto){
        // tam thoi la chua validate

//        if (userFeign.getUserById(userId).getBody() == null) {
//            throw new RuntimeException("User not found");
//        }


        // TODO: auto generate the ship id
        // request to delivery service and get back shipId
        Order newOrder = Order.builder()
                .height(orderDto.getHeight())
                .width(orderDto.getWidth())
                .depth(orderDto.getDepth())
                .userId(userId)
                .storeId(orderDto.getStoreId())
                .receiverId(orderDto.getReceiverId())
                .orderStatus(orderDto.getOrderStatus() != null ? OrderStatus.valueOf(orderDto.getOrderStatus()) : null)
                .price(orderDto.getPrice())
                .discount(orderDto.getDiscount())
                .shipId(null) // TODO:
                .orderOptions(
                        orderDto.getOrderOptions() != null ? orderOptionMapper.mapDtosToEntities(orderDto.getOrderOptions()): null) // convert Dto to orderOptions
                .build();

        // tu dong tao 1 ship id moi, gui request toi shipping service voi param la orderid
        // tao code moi
        newOrder.setCode(OrderUtils.generateOrderCode(newOrder.getId()));
        orderRepository.save(newOrder);
        return orderMapper.mapToDto(newOrder);
    }

    public List<OrderOptionResponse> getAllOrderOptions(UUID userId, Long order_id){
        Order order = orderRepository.findOrderByIdAndUserId(order_id, userId);
        // mapping o
        //

        return null;
    }

    public List<OrderDto> getAllOrders(UUID userId){
        List<Order> orders = orderRepository.findOrdersByUserId(userId);
        if (orders == null) {
            return null;
//            throw new RuntimeException("Order not found");
        }
        // mapping

        return orderMapper.mapToDtos(orders);
    }

    public OrderDto updateReceiver(UUID userId, Long orderId, Long receiverId) {
        Order order = orderRepository.findOrderByIdAndUserId(orderId, userId);
        if (order == null) {
            throw new RuntimeException("Order not found");
        }
        order.setReceiverId(receiverId);
        orderRepository.save(order);
        return orderMapper.mapToDto(order);
    }

    public void updateStore(UUID userId, Long orderId, Long storeId) {
        Order order = orderRepository.findOrderByIdAndUserId(orderId, userId);
        if (order == null) {
            throw new RuntimeException("Order not found");
        }
        order.setStoreId(storeId);
        orderRepository.save(order);
    }

    public void updateOrderStatus(UUID userId, Long orderId, String status) {
        Order order = orderRepository.findOrderByIdAndUserId(orderId, userId);
        if (order == null) {
            throw new RuntimeException("Order not found");
        }
        order.setOrderStatus(OrderStatus.valueOf(status));
        orderRepository.save(order);
    }

    public void updateOrder(UUID userId, Long orderId, OrderDto orderDto) {
        Order order = orderRepository.findOrderByIdAndUserId(orderId, userId);
        if (order == null) {
            throw new RuntimeException("Order not found");
        }
        order.setReceiverId(orderDto.getReceiverId());
        order.setStoreId(orderDto.getStoreId());
        order.setOrderStatus(OrderStatus.valueOf(orderDto.getOrderStatus()));
        order.setPrice(orderDto.getPrice());
        order.setDiscount(orderDto.getDiscount());
        order.setShipId(orderDto.getShipId());
        order.setOrderOptions(orderOptionMapper.mapDtosToEntities(orderDto.getOrderOptions()));
        orderRepository.save(order);

    }


    // TODO:
//    public ResponseEntity<String> updateOrderStatus(UUID userId, Long order_id, StatusRequest statusRequest){
//        Order order = orderRepository.findOrderByIdAndUserId(order_id, userId);
//        Status status = statusRepository.findById(statusRequest.getStatusId()).orElse(null);
//        if (order == null) {
//            return new ResponseEntity<>("Order not found", HttpStatus.BAD_REQUEST);
//        }
//        if (status == null) {
//            return new ResponseEntity<>("Status not found", HttpStatus.BAD_REQUEST);
//        }
//        // update status
//        order.setOrderStatus(status);
//        // order.setLastStatusUpdate(LocalDateTime.now());
//        orderRepository.save(order);
//        return new ResponseEntity<>("Updated order status successfully", HttpStatus.OK);
//    }

    public String deleteOrder(UUID userId, Long order_id){
        Order order = orderRepository.findOrderByIdAndUserId(order_id, userId);
        if (order == null) {
            throw new RuntimeException("Order not found");
        }
        orderRepository.delete(order);
        return MessageConstant.SUCCESS_ORDER_DELETED;
    }


}

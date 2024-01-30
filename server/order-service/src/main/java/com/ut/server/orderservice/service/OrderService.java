package com.ut.server.orderservice.service;

import com.ut.server.orderservice.config.*;
import com.ut.server.orderservice.dto.*;
import com.ut.server.orderservice.dto.request.OrderRequest;
import com.ut.server.orderservice.exception.OrderNotFoundException;
import com.ut.server.orderservice.mapper.OrderItemMapper;
import com.ut.server.orderservice.mapper.OrderMapper;
import com.ut.server.orderservice.model.Order;
import com.ut.server.orderservice.model.OrderItem;
import com.ut.server.orderservice.repo.OrderItemRepository;
import com.ut.server.orderservice.repo.OrderRepository;
import com.ut.server.orderservice.utils.RandomGenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.ut.server.common.events.OrderStatus;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final StoreFeign storeFeign;
    private final ReceiverFeign receiverFeign;
    private final DeliveryFeign deliveryFeign;
    private final ProductFeign productFeign;
    private final OrderItemService orderItemService;
    @PersistenceContext
    private EntityManager entityManager;
    private final OrderItemRepository orderItemRepository;

    public List<OrderItem> saveItems(OrderDto orderDto, Order order) {
        // save order items
        List<OrderItem> res = new ArrayList<>();
        List<OrderItemDto> orderItemDtos = orderDto.getItems();
        List<OrderItem> orderItems = orderItemMapper.mapDtosToEntities(orderItemDtos);
        orderItems.forEach(orderItem -> {
            orderItem.setOrderId(order);
            orderItemRepository.save(orderItem);
            res.add(orderItem);
        });
        return res;
    }

    public OrderDto getOrderById(UUID userId, Long orderId) {
        Order order = orderRepository.findOrderByIdAndUserId(orderId, userId);

        if (order == null) {
            throw new OrderNotFoundException("Order not found!");
        }
        // ok, found!
        // find list of product


        return orderMapper.mapToDto(order);
    }


    public OrderDto createOrder(OrderRequest orderRequest) {
        // tam thoi la chua validate

        // store
        if (orderRequest.getStore().getId() == null) {
            StoreDto storeDto = storeFeign.addNewStore(
                    orderRequest.getStore(),
                    orderRequest.getUserId()
            ).getData();
            orderRequest.setStore(storeDto);
        }

        // receiver
        if (orderRequest.getReceiver().getId() == null) {
            ReceiverDto receiverDto = receiverFeign.addReceiver(
                    orderRequest.getReceiver(),
                    orderRequest.getUserId()
            ).getData();
            orderRequest.setReceiver(receiverDto);
        }

        Order newOrder = orderMapper.mapRequestToEntity(orderRequest);
        newOrder.setItems(newOrder.getItems());  // mapping relationship
        newOrder = orderRepository.save(newOrder);

        // delivery
        DeliveryDto deliveryDto = deliveryFeign.createDelivery(orderRequest.getDelivery(), newOrder.getId()).getData();
        newOrder.setDeliveryId(deliveryDto.getId());
        newOrder.setOrderStatus(OrderStatus.CREATED);
        newOrder.setCode("ORDER-" + RandomGenUtils.getRandomInt(1, 1000000));

        // 2nd save
        newOrder = orderRepository.save(newOrder);

        log.error("ORDER-SERVICE: DEBUG MODE AT createOrder at 2nd save: {}", newOrder.toString());
        return orderMapper.mapToDto(newOrder);
    }


    public List<OrderDto> getAllOrders(UUID userId) {
        List<Order> orders = orderRepository.findOrdersByUserId(userId);
        if (orders == null) {
            return null;
//            throw new RuntimeException("Order not found");
        }
        // mapping
        // test: user feign
//        ResponseEntity<?> user = userFeign.getUserById(userId);
        log.debug("ORDER-SERVICE: DEBUG MODE AT getAllOrders");

        // Test feign client to product service
//        GenericResponseDTO<?> products = productFeign.getAllProduct(userId);
//        log.error(String.valueOf(products.getData()));
        return orderMapper.mapToDtos(orders);
    }

    public OrderDto updateReceiver(UUID userId, Long orderId, Long receiverId) {
        Order order = orderRepository.findOrderByIdAndUserId(orderId, userId);
        if (order == null) {
            throw new OrderNotFoundException("Order not found");
        }
        order.setReceiverId(receiverId);
        orderRepository.save(order);
        return orderMapper.mapToDto(order);
    }

    public OrderDto updateStore(UUID userId, Long orderId, Long storeId) {
        Order order = orderRepository.findOrderByIdAndUserId(orderId, userId);
        if (order == null) {
            throw new OrderNotFoundException("Order not found");
        }
        order.setStoreId(storeId);
        orderRepository.save(order);
        return orderMapper.mapToDto(order);
    }

    public OrderDto updateOrderStatus(UUID userId, Long orderId, String status) {
        Order order = orderRepository.findOrderByIdAndUserId(orderId, userId);
        if (order == null) {
            throw new OrderNotFoundException("Order not found");
        }
        order.setOrderStatus(OrderStatus.valueOf(status));
        orderRepository.save(order);
        return orderMapper.mapToDto(order);
    }

    public OrderDto updateOrder(UUID userId, Long orderId, OrderDto orderDto) {
        Order order = orderRepository.findOrderByIdAndUserId(orderId, userId);
        if (order == null) {
            throw new OrderNotFoundException("Order not found");
        }
        order.setReceiverId(orderDto.getReceiver().getId());
        order.setStoreId(orderDto.getStore().getId());
        order.setOrderStatus(orderDto.getOrderStatus());
        order.setPrice(orderDto.getPrice());
        order.setDiscount(orderDto.getDiscount());
        order.setDeliveryId(orderDto.getDeliveryId());
        order.setIsBulky(orderDto.getIsBulky());
        order.setIsFragile(orderDto.getIsFragile());
        order.setIsValuable(orderDto.getIsValuable());

        orderRepository.save(order);
        return orderMapper.mapToDto(order);
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

    public void deleteOrder(UUID userId, Long orderId) {
        Order order = orderRepository.findOrderByIdAndUserId(orderId, userId);
        if (order == null) {
            throw new OrderNotFoundException("Order not found");
        }
        orderRepository.delete(order);
    }


}

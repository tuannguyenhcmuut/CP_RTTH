package com.ut.server.orderservice.service;

import com.ut.server.orderservice.common.MessageConstant;
import com.ut.server.orderservice.config.ProductFeign;
import com.ut.server.orderservice.config.UserFeign;
import com.ut.server.orderservice.dto.OrderDto;
import com.ut.server.orderservice.mapper.OrderItemMapper;
import com.ut.server.orderservice.mapper.OrderMapper;
import com.ut.server.orderservice.model.Order;
import com.ut.server.orderservice.model.OrderItem;
import com.ut.server.orderservice.repo.OrderRepository;
import com.ut.server.orderservice.utils.OrderUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.ut.server.common.dtos.GenericResponseDTO;
import org.ut.server.common.events.OrderStatus;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final UserFeign userFeign;
    private final ProductFeign productFeign;
    private final OrderItemService orderItemService;
    @PersistenceContext
    private EntityManager entityManager;


    public OrderDto getOrderById(UUID userId, Long orderId) {
        Order order = orderRepository.findOrderByIdAndUserId(orderId, userId);

        if (order == null) {
            throw new RuntimeException("Order not found");
        }
        // ok, found!
        // find list of product


        return OrderDto.builder()
                .id(order.getId())
                .code(order.getCode())
                .height(order.getHeight())
                .width(order.getWidth())
                .depth(order.getDepth())
                .userId(order.getUserId())
                .items(
                        orderItemMapper.mapToDtos(order.getItems())
                 )
                .storeId(order.getStoreId())
                .receiverId(order.getReceiverId())
                .orderStatus(order.getOrderStatus().toString())
                .price(order.getPrice())
                .shipId(order.getShipId())
                .isBulky(order.getIsBulky())
                .isFragile(order.getIsFragile())
                .isValuable(order.getIsValuable())
                .build();
    }


    public OrderDto createOrder(UUID userId, OrderDto orderDto) {
        // tam thoi la chua validate

//        if (userFeign.getUserById(userId).getBody() == null) {
//            throw new RuntimeException("User not found");
//        }
        // add order items


        // TODO: auto generate the ship id
        // request to delivery service and get back shipId
        Order newOrder = Order.builder()
                .height(orderDto.getHeight())
                .width(orderDto.getWidth())
                .depth(orderDto.getDepth())
                .items(orderDto.getItems() != null ?
                        orderDto.getItems().stream().map(orderItemDto -> {
                    return orderItemMapper.mapDtoToEntity(orderItemDto);
                }).collect(Collectors.toList()) : null
                )
                .userId(userId)
                .storeId(orderDto.getStoreId())
                .receiverId(orderDto.getReceiverId())
                .orderStatus(orderDto.getOrderStatus() != null ? OrderStatus.valueOf(orderDto.getOrderStatus()) : null)
                .price(orderDto.getPrice())
                .discount(orderDto.getDiscount())
                .shipId(null) // TODO:
                .isBulky(orderDto.getIsBulky())
                .isFragile(orderDto.getIsFragile())
                .isValuable(orderDto.getIsValuable())
                .build();






        // tu dong tao 1 ship id moi, gui request toi shipping service voi param la orderid
        // tao code moi
        orderRepository.save(newOrder);
        Order orderNew = orderRepository.findOrderByIdAndUserId(newOrder.getId(), userId);
        orderNew.setCode(OrderUtils.generateOrderCode(orderNew.getId()));
        orderRepository.save(orderNew);
//        entityManager.getTransaction().commit();
        return orderMapper.mapToDto(orderNew);
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
        GenericResponseDTO<?> products = productFeign.getAllProduct(userId);
        log.error(String.valueOf(products.getData()));
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

    public OrderDto updateStore(UUID userId, Long orderId, Long storeId) {
        Order order = orderRepository.findOrderByIdAndUserId(orderId, userId);
        if (order == null) {
            throw new RuntimeException("Order not found");
        }
        order.setStoreId(storeId);
        orderRepository.save(order);
        return orderMapper.mapToDto(order);
    }

    public OrderDto updateOrderStatus(UUID userId, Long orderId, String status) {
        Order order = orderRepository.findOrderByIdAndUserId(orderId, userId);
        if (order == null) {
            throw new RuntimeException("Order not found");
        }
        order.setOrderStatus(OrderStatus.valueOf(status));
        orderRepository.save(order);
        return orderMapper.mapToDto(order);
    }

    public OrderDto updateOrder(UUID userId, Long orderId, OrderDto orderDto) {
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
            throw new RuntimeException("Order not found");
        }
        orderRepository.delete(order);
    }


}

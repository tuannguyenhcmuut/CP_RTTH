package org.ut.server.userservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.ut.server.userservice.dto.OrderDto;
import org.ut.server.userservice.dto.request.OrderRequest;
import org.ut.server.userservice.exception.OrderNotFoundException;
import org.ut.server.userservice.exception.ReceiverNotFoundException;
import org.ut.server.userservice.exception.StoreNotFoundException;
import org.ut.server.userservice.mapper.*;
import org.ut.server.userservice.model.Delivery;
import org.ut.server.userservice.model.Order;
import org.ut.server.userservice.model.Receiver;
import org.ut.server.userservice.model.Store;
import org.ut.server.userservice.model.enums.OrderStatus;
import org.ut.server.userservice.repo.*;
import org.ut.server.userservice.utils.RandomGenUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
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
    private final ReceiverRepository receiverRepository;
    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;
    private final OrderItemService orderItemService;
//    storeMapper
    private final StoreMapper storeMapper;
    private final ReceiverMapper receiverMapper;
    @PersistenceContext
    private EntityManager entityManager;
    private final OrderItemRepository orderItemRepository;
//    private final DeliveryService deliveryService;
    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper deliveryMapper;

//    public List<OrderItem> saveItems(OrderDto orderDto, Order order) {
//        // save order items
//        List<OrderItem> res = new ArrayList<>();
//        List<OrderItemDto> orderItemDtos = orderDto.getOrderItemDtos();
//        List<OrderItem> orderItems = orderItemMapper.mapDtosToEntities(orderItemDtos);
//        orderItems.forEach(orderItem -> {
//            orderItem.setOrderId(order);
//            orderItemRepository.save(orderItem);
//            res.add(orderItem);
//        });
//        return res;
//    }

    public OrderDto getOrderById(UUID userId, Long orderId) {
        Order order = orderRepository.findOrderByIdAndUser_Id(orderId, userId)
                .orElseThrow(
                        () -> new OrderNotFoundException("Order not found!")
                );
        // ok, found!
        // find list of product


        return orderMapper.mapToDto(order);
    }


    public OrderDto createOrder(OrderRequest orderRequest) {
        // tam thoi la chua validate
        Store store;
        Receiver receiver;

        // store
        if (orderRequest.getStore().getId() == null) {
            store = storeRepository.save(
                    storeMapper.mapToEntity(orderRequest.getStore(), orderRequest.getUserId())
            );
            orderRequest.setStore(storeMapper.mapToDto(store));
        }
        else {
            store = storeRepository.findStoreByIdAndUser_Id(orderRequest.getStore().getId(), orderRequest.getUserId())
                    .orElseThrow(
                            () -> new StoreNotFoundException("Store not found by id: " + orderRequest.getStore().getId().toString())
                    );
//            orderRequest.setSt/ore(storeMapper.mapToDto(store));
        }

        // receiver
        if (orderRequest.getReceiver().getId() == null) {
            receiver = receiverRepository.save(
                    receiverMapper.mapDtoToEntity(orderRequest.getReceiver(), orderRequest.getUserId())
            );
            orderRequest.setReceiver(receiverMapper.mapToDto(receiver));
        }
        else {
            receiver = receiverRepository.findReceiverByIdAndUser_Id(orderRequest.getReceiver().getId(), orderRequest.getUserId())
                    .orElseThrow(
                            () -> new ReceiverNotFoundException("Receiver not found by id: " + orderRequest.getReceiver().getId().toString())
                    );
//            orderRequest.setReceiver(receiverMapper.mapToDto(receiver));
        }

        Order newOrder = orderMapper.mapRequestToEntity(orderRequest);
        newOrder.setItems(newOrder.getItems());  // mapping relationship
        newOrder.setStore(store);
        newOrder.setReceiver(receiver);
        newOrder = orderRepository.save(newOrder);


        // delivery
        Delivery newDelivery = deliveryMapper.mapRequestToEntity(orderRequest.getDelivery(), newOrder.getId());
        Delivery savedDelivery = deliveryRepository.save(newDelivery);
        newOrder.setDelivery(savedDelivery);
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
        Order order = orderRepository.findOrderByIdAndUser_Id(orderId, userId)
                .orElseThrow(
                        () -> new OrderNotFoundException("Order not found!")
                );
        // find receiver
        Receiver receiver = receiverRepository.findReceiverByIdAndUser_Id(receiverId, userId)
                .orElseThrow(
                        () -> new ReceiverNotFoundException("Receiver not found by id: " + receiverId.toString())
                );
        if (order == null) {
            throw new OrderNotFoundException("Order not found");
        }
        order.setReceiver(receiver);
        orderRepository.save(order);
        return orderMapper.mapToDto(order);
    }

    public OrderDto updateStore(UUID userId, Long orderId, Long storeId) {
        Order order = orderRepository.findOrderByIdAndUser_Id(orderId, userId)
                .orElseThrow(
                        () -> new OrderNotFoundException("Order not found!")
                );
        // find store
        Store store = storeRepository.findStoreByIdAndUser_Id(storeId, userId)
                .orElseThrow(
                        () -> new StoreNotFoundException("Store not found by id: " + storeId.toString())
                );
        if (order == null) {
            throw new OrderNotFoundException("Order not found");
        }
        order.setStore(store);
        orderRepository.save(order);
        return orderMapper.mapToDto(order);
    }

    public OrderDto updateOrderStatus(UUID userId, Long orderId, String status) {
        Order order = orderRepository.findOrderByIdAndUser_Id(orderId, userId)
                .orElseThrow(
                        () -> new OrderNotFoundException("Order not found!")
                );
        order.setOrderStatus(OrderStatus.valueOf(status));
        orderRepository.save(order);
        return orderMapper.mapToDto(order);
    }

    public OrderDto updateOrder(UUID userId, Long orderId, OrderDto orderDto) {
        Order order = orderRepository.findOrderByIdAndUser_Id(orderId, userId)
                .orElseThrow(
                        () -> new OrderNotFoundException("Order not found!")
                );
        if (order == null) {
            throw new OrderNotFoundException("Order not found");
        }

        Receiver receiver = receiverRepository.findReceiverByIdAndUser_Id(order.getReceiver().getId(), order.getUser().getId())
                .orElseThrow(
                        () -> new ReceiverNotFoundException("Receiver not found by id: " + order.getReceiver().getId().toString())
                );

        Store store = storeRepository.findStoreByIdAndUser_Id(order.getStore().getId(), order.getUser().getId())
                .orElseThrow(
                        () -> new StoreNotFoundException("Store not found by id: " + order.getStore().getId().toString())
                );

        // find delivery
        Delivery delivery = deliveryRepository.findDeliveryByIdAndOrderId(orderDto.getId(),orderId)
                .orElseThrow(
                        () -> new OrderNotFoundException("Delivery not found by order id: " + orderId.toString())
                );
        order.setReceiver(receiver);
        order.setStore(store);
        order.setOrderStatus(orderDto.getOrderStatus());
        order.setPrice(orderDto.getPrice());
        order.setDiscount(orderDto.getDiscount());
        order.setDelivery(delivery);
        order.setIsBulky(orderDto.getIsBulky());
        order.setIsFragile(orderDto.getIsFragile());
        order.setIsValuable(orderDto.getIsValuable());

        orderRepository.save(order);
        return orderMapper.mapToDto(order);
    }


    // TODO:
//    public ResponseEntity<String> updateOrderStatus(UUID userId, Long order_id, StatusRequest statusRequest){
//        Order order = orderRepository.findOrderByIdAndUser_Id(order_id, userId);
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
        Order order = orderRepository.findOrderByIdAndUser_Id(orderId, userId)
                .orElseThrow(
                        () -> new OrderNotFoundException("Order not found!")
                );
//        orderRepository.delete(order);

        // delete order items
        order.getItems().forEach(orderItem -> {
            orderItemService.deleteOrderItem(orderItem);
        });
        // delete delivery
        deliveryRepository.deleteById(order.getDelivery().getId());
        // delete order price
        // delete order
        orderRepository.deleteById(order.getId());
    }


}

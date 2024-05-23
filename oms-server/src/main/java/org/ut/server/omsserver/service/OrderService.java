package org.ut.server.omsserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.ut.server.omsserver.common.MessageConstants;
import org.ut.server.omsserver.dto.ChartStatisticsDto;
import org.ut.server.omsserver.dto.DashboardComponentInfoDto;
import org.ut.server.omsserver.dto.OrderDto;
import org.ut.server.omsserver.dto.TopReceiverDto;
import org.ut.server.omsserver.dto.request.OrderRequest;
import org.ut.server.omsserver.exception.*;
import org.ut.server.omsserver.mapper.DeliveryMapper;
import org.ut.server.omsserver.mapper.OrderMapper;
import org.ut.server.omsserver.mapper.ReceiverMapper;
import org.ut.server.omsserver.mapper.StoreMapper;
import org.ut.server.omsserver.model.*;
import org.ut.server.omsserver.model.enums.EmployeeRequestStatus;
import org.ut.server.omsserver.model.enums.LegitLevel;
import org.ut.server.omsserver.model.enums.OrderStatus;
import org.ut.server.omsserver.repo.*;
import org.ut.server.omsserver.service.impl.NotificationService;
import org.ut.server.omsserver.utils.RandomGenUtils;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final ShopOwnerRepository shopOwnerRepository;

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ReceiverRepository receiverRepository;
    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;
    private final OrderItemService orderItemService;
//    storeMapper
    private final StoreMapper storeMapper;
    private final ReceiverMapper receiverMapper;
//    @PersistenceContext
//    private EntityManager entityManager;
//    private final DeliveryService deliveryService;
    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper deliveryMapper;
    private final DeliveryService deliverService;
    private final EmployeeManagementRepository employeeManagementRepository;
    private final NotificationService notificationService;
    private final OrderHistoryService orderHistoryService;
    private final OrderHistoryRepository orderHistoryRepository;


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
        Order order = orderRepository.findByIdAndShopOwner_Id(orderId, userId)
                .orElseThrow(
                        () -> new OrderNotFoundException(MessageConstants.ORDER_NOT_FOUND)
                );
        // ok, found!
        // find list of product


        return orderMapper.mapToDto(order, null);
    }


    public OrderDto createOrder(OrderRequest orderRequest) {
        // tam thoi la chua validate
        Store store;
        Receiver receiver;

        // store
        if (orderRequest.getStore().getStoreId() == null) {
            store = storeRepository.save(
                    storeMapper.mapToEntity(orderRequest.getStore(), orderRequest.getUserId())
            );
            orderRequest.setStore(storeMapper.mapToDto(store, null));
        }
        else {
            store = storeRepository.findByIdAndShopOwner_Id(orderRequest.getStore().getStoreId(), orderRequest.getUserId())
                    .orElseThrow(
                            () -> new StoreNotFoundException(String.format(MessageConstants.STORE_NOT_FOUND_BY_ID, orderRequest.getStore().getStoreId().toString()))
                    );
//            orderRequest.setSt/ore(storeMapper.mapToDto(store));
        }

        // receiver
        if (orderRequest.getReceiver().getReceiverId() == null) {
            receiver = receiverRepository.save(
                    receiverMapper.mapDtoToEntity(orderRequest.getReceiver(), orderRequest.getUserId())
            );
            orderRequest.setReceiver(receiverMapper.mapToDto(receiver, null));
        }
        else {
            receiver = receiverRepository.findByIdAndShopOwner_Id(orderRequest.getReceiver().getReceiverId(), orderRequest.getUserId())
                    .orElseThrow(
                            () -> new ReceiverNotFoundException(MessageConstants.RECEIVER_NOT_FOUND_BY_ID + orderRequest.getReceiver().getReceiverId().toString())
                    );
//            orderRequest.setReceiver(receiverMapper.mapToDto(receiver));
        }
        // find user
        ShopOwner user = shopOwnerRepository.findById(orderRequest.getUserId())
                .orElseThrow(() -> new RuntimeException(MessageConstants.USER_NOT_FOUND_MESSAGE) );
        orderRequest.setCreatedBy(user.getEmail());

        Order newOrder = orderMapper.mapRequestToEntity(orderRequest);
        newOrder.setItems(newOrder.getItems());  // mapping relationship
//        newOrder.setStore(store);
        newOrder.setStoreId(store.getId());
        newOrder.setStoreName(store.getName());
        newOrder.setStorePhoneNumber(store.getPhoneNumber());
        newOrder .setStoreAddress(store.getAddress());
        newOrder.setStoreDetailedAddress(store.getDetailedAddress());
        newOrder.setStoreDescription(store.getDescription());
        newOrder.setStorePickUpTime(store.getStorePickUpTime());
        newOrder.setIsDefault(store.getIsDefault());
        newOrder.setSendAtPost(store.getSendAtPost());
        newOrder.setSendAtPost(store.getSendAtPost());
//        newOrder.setReceiver(receiver);
        newOrder.setReceiverId(receiver.getId());
        newOrder.setReceiverName(receiver.getName());
        newOrder.setReceiverPhoneNumber(receiver.getPhoneNumber());
        newOrder .setReceiverAddress(receiver.getAddress());
        newOrder.setReceiverDetailedAddress(receiver.getDetailedAddress());
        newOrder.setNote(receiver.getNote());
        newOrder.setReceivedPlace(receiver.getReceivedPlace());
        newOrder.setDeliveryTimeFrame(receiver.getDeliveryTimeFrame());
        newOrder.setCallBeforeSend(receiver.getCallBeforeSend());
        newOrder.setReceiveAtPost(receiver.getReceiveAtPost());

        newOrder.setCreatedBy(user.getEmail()) ;
        newOrder.setLastUpdatedBy(user.getEmail());
        newOrder.setLastUpdatedDate(LocalDateTime.now());

        newOrder = orderRepository.save(newOrder);


        // delivery
//        Delivery newDelivery = deliveryMapper.mapRequestToEntity(orderRequest.getDelivery(), newOrder.getId());
        Delivery delivery = deliverService.createDelivery(orderRequest.getDelivery(), newOrder.getId());
//        Delivery savedDelivery = deliveryRepository.save(newDelivery);
        newOrder.setDelivery(delivery);
        newOrder.setOrderStatus(OrderStatus.CREATED);
        newOrder.setCode("ORDER-" + RandomGenUtils.getRandomInt(1, 1000000));
//        private String shipperName;
//        private String shipperPhone;
//        private String storeAddress;
//        private String storePhone;
//        private String receiverPhone;
//        private String receiverAddress;
//        private String receiverName;


        // 2nd save
        newOrder = orderRepository.saveAndFlush(newOrder);

        OrderHistory newOrderHistory = OrderHistory.builder()
                .orderId(newOrder.getId())
                .description("Đã tạo đơn hàng.")
                .build();
        orderHistoryRepository.save(newOrderHistory);

//        orderHistoryRepository.save(newOrderHistory);
////        Đã tạo đơn hàng
//        orderHistoryService.storeOrderHistory(newOrder, "Đã tạo đơn hàng.");

        log.debug("ORDER-SERVICE: DEBUG MODE AT createOrder at 2nd save: {}", newOrder.toString());
        return orderMapper.mapToDto(newOrder, null);
    }


    public List<OrderDto> getAllOrders(UUID userId, Pageable pageable) {
        if (pageable != null) {
            return orderMapper.mapToDtos(orderRepository.findOrdersByShopOwner_Id(userId, pageable), null);
        }
        List<Order> orders = orderRepository.findOrdersByShopOwner_Id(userId);
        if (orders == null) {
            return List.of();
//            throw new RuntimeException(MessageConstants.ORDER_NOT_FOUND);
        }
        // mapping
        // test: user feign
//        ResponseEntity<?> user = userFeign.getUserById(userId);
        log.debug("ORDER-SERVICE: DEBUG MODE AT getAllOrders");

        // Test feign client to product service
//        GenericResponseDTO<?> products = productFeign.getAllProduct(userId);
//        log.error(String.valueOf(products.getData()));
        return orderMapper.mapToDtos(orders, null);
    }

    public OrderDto updateReceiver(UUID userId, Long orderId, Long receiverId) {
        Order order = orderRepository.findByIdAndShopOwner_Id(orderId, userId)
                .orElseThrow(
                        () -> new OrderNotFoundException(MessageConstants.ORDER_NOT_FOUND)
                );
        // find receiver
        Receiver receiver = receiverRepository.findByIdAndShopOwner_Id(receiverId, userId)
                .orElseThrow(
                        () -> new ReceiverNotFoundException(String.format(MessageConstants.RECEIVER_NOT_FOUND_BY_ID, receiverId.toString()))
                );
        if (order == null) {
            throw new OrderNotFoundException(MessageConstants.ORDER_NOT_FOUND);
        }
//        order.setReceiver(receiver);
//        newOrder.setReceiver(receiver);
        order.setReceiverId(receiver.getId());
        order.setReceiverName(receiver.getName());
        order.setReceiverPhoneNumber(receiver.getPhoneNumber());
        order.setReceiverAddress(receiver.getAddress());
        order.setReceiverDetailedAddress(receiver.getDetailedAddress());
        order.setNote(receiver.getNote());
        order.setReceivedPlace(receiver.getReceivedPlace());
        order.setDeliveryTimeFrame(receiver.getDeliveryTimeFrame());
        order.setCallBeforeSend(receiver.getCallBeforeSend());
        order.setReceiveAtPost(receiver.getReceiveAtPost());
        order.setLastUpdatedBy(order.getShopOwner().getEmail());
        order.setLastUpdatedDate(LocalDateTime.now());

        orderRepository.save(order);
        return orderMapper.mapToDto(order, null);
    }

//    update owner receiver
    public OrderDto updateOwnerReceiver(UUID userId, Long orderId, Long receiverId) {
        List<EmployeeManagement> emplMgnts = employeeManagementRepository.findEmployeeManagementsByEmployee_IdAndApprovalStatus(userId, EmployeeRequestStatus.ACCEPTED);
        if (emplMgnts.isEmpty()) {
            throw new EmployeeManagementException(MessageConstants.ERROR_USER_NOT_HAS_OWNER);
        }
        EmployeeManagement emplMgnt = emplMgnts.get(0);
        ShopOwner owner = emplMgnt.getManager();
        ShopOwner user = emplMgnt.getEmployee();

        Order order = orderRepository.findByIdAndShopOwner_Id(orderId, owner.getId())
                .orElseThrow(
                        () -> new OrderNotFoundException(MessageConstants.ORDER_NOT_FOUND)
                );
        // find receiver
        Receiver receiver = receiverRepository.findByIdAndShopOwner_Id(receiverId, owner.getId())
                .orElseThrow(
                        () -> new ReceiverNotFoundException(String.format(MessageConstants.RECEIVER_NOT_FOUND_BY_ID, receiverId.toString()))
                );
        if (order == null) {
            throw new OrderNotFoundException(MessageConstants.ORDER_NOT_FOUND);
        }
        order.setReceiverId(receiver.getId());
        order.setReceiverName(receiver.getName());
        order.setReceiverPhoneNumber(receiver.getPhoneNumber());
        order.setReceiverAddress(receiver.getAddress());
        order.setReceiverDetailedAddress(receiver.getDetailedAddress());
        order.setReceiverName(receiver.getName());
        order.setReceiverPhoneNumber(receiver.getPhoneNumber());
        order.setReceiverAddress(receiver.getAddress());
        order.setReceiverDetailedAddress(receiver.getDetailedAddress());
        order.setNote(receiver.getNote());
        order.setReceivedPlace(receiver.getReceivedPlace());
        order.setDeliveryTimeFrame(receiver.getDeliveryTimeFrame());
        order.setCallBeforeSend(receiver.getCallBeforeSend());
        order.setReceiveAtPost(receiver.getReceiveAtPost());
        order.setLastUpdatedBy(user.getEmail());
        order.setLastUpdatedDate(LocalDateTime.now());

        orderRepository.save(order);
        return orderMapper.mapToDto(order, owner);
    }

    public OrderDto updateStore(UUID userId, Long orderId, Long storeId) {
        Order order = orderRepository.findByIdAndShopOwner_Id(orderId, userId)
                .orElseThrow(
                        () -> new OrderNotFoundException(MessageConstants.ORDER_NOT_FOUND)
                );
        // find store
        Store store = storeRepository.findByIdAndShopOwner_Id(storeId, userId)
                .orElseThrow(
                        () -> new StoreNotFoundException(String.format(MessageConstants.STORE_NOT_FOUND_BY_ID, storeId.toString()))
                );
        if (order == null) {
            throw new OrderNotFoundException(MessageConstants.ORDER_NOT_FOUND);
        }
//        order.setStore(store);
        //        order.setStore(store);
        order.setStoreId(store.getId());
        order.setStoreName(store.getName());
        order.setStorePhoneNumber(store.getPhoneNumber());
        order .setStoreAddress(store.getAddress());
        order.setStoreDetailedAddress(store.getDetailedAddress());
        order.setStoreDescription(store.getDescription());
        order.setStorePickUpTime(store.getStorePickUpTime());
        order.setIsDefault(store.getIsDefault());
        order.setSendAtPost(store.getSendAtPost());
        order.setSendAtPost(store.getSendAtPost());
        order.setLastUpdatedBy(store.getShopOwner().getEmail());
        order.setLastUpdatedDate(LocalDateTime.now());

        orderRepository.save(order);
        return orderMapper.mapToDto(order, null);
    }


//    update owner order, store
    public OrderDto updateOwnerStore(UUID userId, Long orderId, Long storeId) {
        List<EmployeeManagement> emplMgnts = employeeManagementRepository.findEmployeeManagementsByEmployee_IdAndApprovalStatus(userId, EmployeeRequestStatus.ACCEPTED);
        if (emplMgnts.isEmpty()) {
            throw new EmployeeManagementException(MessageConstants.ERROR_USER_NOT_HAS_OWNER);
        }
        EmployeeManagement emplMgnt = emplMgnts.get(0);
        ShopOwner owner = emplMgnt.getManager();
        ShopOwner user = emplMgnt.getEmployee();

        Order order = orderRepository.findByIdAndShopOwner_Id(orderId, owner.getId())
                .orElseThrow(
                        () -> new OrderNotFoundException(MessageConstants.ORDER_NOT_FOUND)
                );
        // find store
        Store store = storeRepository.findByIdAndShopOwner_Id(storeId, owner.getId())
                .orElseThrow(
                        () -> new StoreNotFoundException(String.format(MessageConstants.STORE_NOT_FOUND_BY_ID, storeId.toString()))
                );
        if (order == null) {
            throw new OrderNotFoundException(MessageConstants.ORDER_NOT_FOUND);
        }
//        order.setStore(store);
        //        order.setStore(store);
        order.setStoreId(store.getId());
        order.setStoreName(store.getName());
        order.setStorePhoneNumber(store.getPhoneNumber());
        order .setStoreAddress(store.getAddress());
        order.setStoreDetailedAddress(store.getDetailedAddress());
        order.setStoreDescription(store.getDescription());
        order.setStorePickUpTime(store.getStorePickUpTime());
        order.setIsDefault(store.getIsDefault());
        order.setSendAtPost(store.getSendAtPost());
        order.setSendAtPost(store.getSendAtPost());
        order.setLastUpdatedBy(user.getEmail());
        order.setLastUpdatedDate(LocalDateTime.now());

        orderRepository.save(order);
        return orderMapper.mapToDto(order, owner);
    }

    public OrderDto updateOrderStatus(UUID userId, Long orderId, String status) {
        Order order = orderRepository.findByIdAndShopOwner_Id(orderId, userId)
                .orElseThrow(
                        () -> new OrderNotFoundException(MessageConstants.ORDER_NOT_FOUND)
                );

//        check the status is valid
        validateOrderStatus(status, order);
        // check receiver if status is cancelled
        checkReceiverIfStatusIsCancelled(status, order);
        order.setOrderStatus(OrderStatus.valueOf(status));
        OrderHistory newOrderHistory = OrderHistory.builder()
                .orderId(order.getId())
                .build();
        // set order history
        if (status.equals("CANCELLED")) {
            newOrderHistory.setDescription("Đơn hàng đã bị huỷ.");
        }
        else if (status.equals("PROCESSING")) {
            newOrderHistory.setDescription("Đơn hàng đang được xử lý.");
        }
        else if (status.equals("SHIPPED")) {
            orderHistoryService.storeOrderHistory(order, "Đơn hàng đã được gửi đi.");

            newOrderHistory.setDescription("Đơn hàng đã được gửi đi.");
        }
        else if (status.equals("DELIVERED")) {
            newOrderHistory.setDescription("Đã giao hàng.");
        }
        orderHistoryRepository.save(newOrderHistory);
        ShopOwner user = shopOwnerRepository.findShopOwnerById(userId)
                .orElseThrow(() -> new RuntimeException(MessageConstants.USER_NOT_FOUND_MESSAGE) );
        order.setLastUpdatedBy(user.getEmail());
        order.setLastUpdatedDate(LocalDateTime.now());
        orderRepository.save(order);
        return orderMapper.mapToDto(order, null);
    }

    private  void checkReceiverIfStatusIsCancelled(String status, Order order) {
        if (status.equals("CANCELLED") || status.equals("DELIVERED")) {

           Receiver receiver = receiverRepository.findById(order.getReceiverId())
                   .orElseThrow(() -> new ReceiverNotFoundException(MessageConstants.RECEIVER_NOT_FOUND_BY_ID + order.getReceiverId().toString()));
           if (status.equals("CANCELLED")) {
               receiver.setLegitPoint(receiver.getLegitPoint() - 1);
           }
           else if (status.equals("DELIVERED")) {
               receiver.setLegitPoint(receiver.getLegitPoint() + 1);
           }
           // set all of the legit level of receiver with the range of legit point
            if (receiver.getLegitPoint() < -2) {
                receiver.setLegitLevel(LegitLevel.VERY_LOW);
            }
            else  if (receiver.getLegitPoint() >= -2 && receiver.getLegitPoint() < 0 ) {
                receiver.setLegitLevel(LegitLevel.BAD);
            }
            else if (receiver.getLegitPoint() >= 0 && receiver.getLegitPoint() < 10) {
                receiver.setLegitLevel(LegitLevel.NORMAL);
            }
            else if (receiver.getLegitPoint() >= 10 && receiver.getLegitPoint() < 30) {
                receiver.setLegitLevel(LegitLevel.HIGH);
            }
            else if (receiver.getLegitPoint() >= 30) {
                receiver.setLegitLevel(LegitLevel.VERY_HIGH);
            }
        }


    }

    private void setOrderHistoryFromStatus(Order order, String status) {
        if (status.equals("CANCELLED")) {
            orderHistoryService.storeOrderHistory(order, "Đơn hàng đã bị huỷ.");
        }
        else if (status.equals("PROCESSING")) {
            orderHistoryService.storeOrderHistory(order, "Đơn hàng đang được xử lý.");
        }
        else if (status.equals("SHIPPED")) {
            orderHistoryService.storeOrderHistory(order, "Đơn hàng đã được gửi đi.");
        }
        else if (status.equals("DELIVERED")) {
            orderHistoryService.storeOrderHistory(order, "Đã giao hàng.");
        }
    }

    public Order updateOrderStatusForShipper(UUID shipperId, Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(MessageConstants.ORDER_NOT_FOUND));
        if (!order.getDelivery().getShipper().getId().equals(shipperId)) {
            throw new RuntimeException(MessageConstants.NOT_SHIPPER_OF_DELIVERY);
        }
//        validateOrderStatus(status, order);
        order.setOrderStatus(OrderStatus.valueOf(status));
        order.setLastUpdatedBy(order.getDelivery().getShipper().getEmail());
        order.setLastUpdatedDate(LocalDateTime.now());
        orderRepository.save(order);
        return order;
    }

    private static void validateOrderStatus(String status, Order order) {
        if (!OrderStatus.contains(status)) {
            throw new OrderUpdateException(MessageConstants.INVALID_ORDER_STATUS);
        }
//        CREATED, -> chi dc update thanh cancelled hoac processing
        if (order.getOrderStatus().equals(OrderStatus.CREATED)) {
            if (!status.equals("CANCELLED") && !status.equals("PROCESSING")) {
                throw new OrderUpdateException(String.format(MessageConstants.CANNOT_UPDATE_STATUS_FROM_CREATED, status));
            }
        }
//        PROCESSING -> chi dc update thanh shipped hoac cancelled
        if (order.getOrderStatus().equals(OrderStatus.PROCESSING)) {
            if (!status.equals("SHIPPED") && !status.equals("CANCELLED")) {
                throw new OrderUpdateException(String.format(MessageConstants.CANNOT_UPDATE_STATUS_FROM_PROCESSING, status));
            }
        }

//        SHIPPED -> chi dc update thanh delivered hoac cancelled
        if (order.getOrderStatus().equals(OrderStatus.SHIPPED)) {
            if (!status.equals("DELIVERED") && !status.equals("CANCELLED")) {
                throw new OrderUpdateException(String.format(MessageConstants.CANNOT_UPDATE_STATUS_FROM_SHIPPED, status));
            }
        }
//        DELIVERED -> chi dc update thanh cancelled
        if (order.getOrderStatus().equals(OrderStatus.DELIVERED)) {
            if (!status.equals("CANCELLED")) {
                throw new OrderUpdateException(String.format(MessageConstants.CANNOT_UPDATE_STATUS_FROM_DELIVERED, status));
            }
        }
//        CANCELLED -> khong dc update
        if (order.getOrderStatus().equals(OrderStatus.CANCELLED)) {
            throw new OrderUpdateException(MessageConstants.CANNOT_UPDATE_FROM_CANCELLED);
        }
    }

    public OrderDto updateOrder(UUID userId, Long orderId, OrderDto orderDto) {
//        find shop owner email
        ShopOwner user = shopOwnerRepository.findShopOwnerById(userId)
                .orElseThrow(() -> new RuntimeException(MessageConstants.USER_NOT_FOUND_MESSAGE) );

        Order order = orderRepository.findByIdAndShopOwner_Id(orderId, userId)
                .orElseThrow(
                        () -> new OrderNotFoundException(MessageConstants.ORDER_NOT_FOUND)
                );
        if (order == null) {
            throw new OrderNotFoundException(MessageConstants.ORDER_NOT_FOUND);
        }

        Receiver receiver = receiverRepository.findByIdAndShopOwner_Id(order.getReceiverId(), order.getShopOwner().getId())
                .orElseThrow(
                        () -> new ReceiverNotFoundException(MessageConstants.RECEIVER_NOT_FOUND_BY_ID + order.getReceiverId().toString())
                );

        Store store = storeRepository.findByIdAndShopOwner_Id(order.getStoreId(), order.getShopOwner().getId())
                .orElseThrow(
                        () -> new StoreNotFoundException(String.format(MessageConstants.STORE_NOT_FOUND_BY_ID, order.getStoreId().toString()))
                );

        // find delivery
        Delivery delivery = deliveryRepository.findByIdAndOrderId(order.getDelivery().getId(), orderId)
                .orElseThrow(
                        () -> new OrderNotFoundException(String.format(MessageConstants.DELIVERY_NOT_FOUND_BY_ORDER_ID, orderId.toString()))
                );

//        order.setReceiver(receiver);
//        order.setStore(store);
        //        newOrder.setStore(store);
        order.setStoreId(store.getId());
        order.setStoreName(store.getName());
        order.setStorePhoneNumber(store.getPhoneNumber());
        order .setStoreAddress(store.getAddress());
        order.setStoreDetailedAddress(store.getDetailedAddress());
        order.setStoreDescription(store.getDescription());
        order.setStorePickUpTime(store.getStorePickUpTime());
        order.setIsDefault(store.getIsDefault());
        order.setSendAtPost(store.getSendAtPost());
        order.setSendAtPost(store.getSendAtPost());
//        order.setReceiver(receiver);
        order.setReceiverId(receiver.getId());
        order.setReceiverName(receiver.getName());
        order.setReceiverPhoneNumber(receiver.getPhoneNumber());
        order.setReceiverAddress(receiver.getAddress());
        order.setReceiverDetailedAddress(receiver.getDetailedAddress());
        order.setNote(receiver.getNote());
        order.setReceivedPlace(receiver.getReceivedPlace());
        order.setDeliveryTimeFrame(receiver.getDeliveryTimeFrame());
        order.setCallBeforeSend(receiver.getCallBeforeSend());
        order.setReceiveAtPost(receiver.getReceiveAtPost());
        this.validateOrderStatus(orderDto.getOrderStatus().toString(), order);
        this.checkReceiverIfStatusIsCancelled(orderDto.getOrderStatus().toString(), order);
        order.setOrderStatus(orderDto.getOrderStatus());
        order.setCode(orderDto.getCode());
        order.setHeight(orderDto.getHeight());
        order.setWidth(orderDto.getWidth());
        order.setLength(orderDto.getLength());
        order.setIsDraft(orderDto.getIsDraft());
        order.setPrice(orderDto.getPrice());
        order.setDelivery(delivery);
        order.setDiscount(orderDto.getDiscount());
        order.setIsDocument(orderDto.getIsDocument());
        order.setIsBulky(orderDto.getIsBulky());
        order.setIsFragile(orderDto.getIsFragile());
        order.setIsValuable(orderDto.getIsValuable());
        order.setLastUpdatedBy(user.getEmail());
        order.setLastUpdatedDate(LocalDateTime.now());

        orderRepository.save(order);
        return orderMapper.mapToDto(order, null);
    }

//
//    // TODO:
//    public OrderDto updateOrderStatus(UUID userId, Long orderId, String status){
//
//    }

    public void deleteOrder(UUID userId, Long orderId) {
        Order order = orderRepository.findByIdAndShopOwner_Id(orderId, userId)
                .orElseThrow(
                        () -> new OrderNotFoundException(MessageConstants.ORDER_NOT_FOUND)
                );
//        orderRepository.delete(order);

        // delete order items
        order.getItems().forEach(orderItemService::deleteOrderItem);
        // delete order price
        // delete delivery
        deliveryRepository.deleteById(order.getDelivery().getId());

        // delete order price

        // delete order
        orderRepository.deleteById(order.getId());
    }

    public List<OrderDto> getOwnerOrders(UUID userId, Pageable pageable) {
        List<EmployeeManagement> emplMgnts= employeeManagementRepository.findEmployeeManagementsByEmployee_IdAndApprovalStatus(userId, EmployeeRequestStatus.ACCEPTED);
        if (emplMgnts.isEmpty()) {
            throw new EmployeeManagementException(MessageConstants.ERROR_USER_NOT_HAS_OWNER);
        }
        EmployeeManagement emplMgnt = emplMgnts.get(0);
        // TODO: check employee permission that has get or not
        ShopOwner owner = emplMgnt.getManager();
        List<Order> orders;
        if (pageable != null) {
            orders = orderRepository.findOrdersByShopOwner_Id(owner.getId(), pageable);
        }
        else {
            orders = orderRepository.findOrdersByShopOwner_Id(owner.getId());
        }
        return orderMapper.mapToDtos(orders, owner);
    }

    public OrderDto createOwnerOrder(OrderRequest orderRequest) {
        List<EmployeeManagement> emplMgnts= employeeManagementRepository.findEmployeeManagementsByEmployee_IdAndApprovalStatus(orderRequest.getUserId(), EmployeeRequestStatus.ACCEPTED);
        if (emplMgnts.isEmpty()) {
            throw new EmployeeManagementException(MessageConstants.ERROR_USER_NOT_HAS_OWNER);
        }
        EmployeeManagement emplMgnt = emplMgnts.get(0);
        // TODO: check employee permission that has get or not
        ShopOwner owner = emplMgnt.getManager();
        ShopOwner user = emplMgnt.getEmployee();

        Store store;
        Receiver receiver;

        // store
        if (orderRequest.getStore().getStoreId() == null) {
            throw new StoreNotFoundException(MessageConstants.STORE_IS_NOT_EXISTED);
        }
        else {
//            store = storeRepository.findByIdAndShopOwner_Id(orderRequest.getStoreId(), owner.getId())
//                    .orElseThrow(
//                            () -> new StoreNotFoundException("Store not found by id: " + orderRequest.getStoreId().toString())
//                    );
//            orderRequest.setStore(storeMapper.mapToDto(store));
            store = storeRepository.findById(orderRequest.getStore().getStoreId()).orElseThrow(() -> new RuntimeException(MessageConstants.STORE_NOT_FOUND_MESSAGE));
            if (!store.getShopOwner().getId().equals(owner.getId())) {
                throw new RuntimeException(MessageConstants.STORE_AND_OWNER_NOT_MATCHED);
            }
        }

        // receiver
        if (orderRequest.getReceiver().getReceiverId() == null) {
            throw new ReceiverNotFoundException(MessageConstants.RECEIVER_IS_NOT_EXISTED);
        }
        else {
//            receiver = receiverRepository.findReceiverByIdAndShopOwner_Id(orderRequest.getReceiverId(), owner.getId())
//                    .orElseThrow(
//                            () -> new ReceiverNotFoundException("Receiver of owner not found by id: " + orderRequest.getReceiverId().toString())
//                    );
            receiver = receiverRepository.findById(orderRequest.getReceiver().getReceiverId()).orElseThrow(() -> new RuntimeException(MessageConstants.RECEIVER_NOT_FOUND));
            if (!receiver.getShopOwner().getId().equals(owner.getId())) {
                throw new RuntimeException(MessageConstants.RECEIVER_AND_OWNER_NOT_MATCHED);
            }
//            orderRequest.setReceiver(receiverMapper.mapToDto(receiver));
        }

        // check if the product is from owner
        orderRequest.getItems().forEach(
                orderItemDto -> {
                    Optional<Product> product = productRepository.findProductByIdAndShopOwner_Id(
                            orderItemDto.getProduct().getId(), owner.getId()
                    );
                    // check if product is not found
                    if (product.isEmpty()) {
                        throw new ProductNotFoundException(MessageConstants.PRODUCT_OF_OWNER_NOT_FOUND);
                    }
                }
        );

        // check product, receiver of owner
        orderRequest.setUserId(owner.getId());
        Order newOrder = orderMapper.mapRequestToEntity(orderRequest);
        newOrder.setItems(newOrder.getItems());  // mapping relationship
        //        newOrder.setStore(store);
        newOrder.setStoreId(store.getId());
        newOrder.setStoreName(store.getName());
        newOrder.setStorePhoneNumber(store.getPhoneNumber());
        newOrder .setStoreAddress(store.getAddress());
        newOrder.setStoreDetailedAddress(store.getDetailedAddress());
        newOrder.setStoreDescription(store.getDescription());
        newOrder.setStorePickUpTime(store.getStorePickUpTime());
        newOrder.setIsDefault(store.getIsDefault());
        newOrder.setSendAtPost(store.getSendAtPost());
        newOrder.setSendAtPost(store.getSendAtPost());
//        newOrder.setReceiver(receiver);
        newOrder.setReceiverId(receiver.getId());
        newOrder.setReceiverName(receiver.getName());
        newOrder.setReceiverPhoneNumber(receiver.getPhoneNumber());
        newOrder .setReceiverAddress(receiver.getAddress());
        newOrder.setReceiverDetailedAddress(receiver.getDetailedAddress());
        newOrder.setNote(receiver.getNote());
        newOrder.setReceivedPlace(receiver.getReceivedPlace());
        newOrder.setDeliveryTimeFrame(receiver.getDeliveryTimeFrame());
        newOrder.setCallBeforeSend(receiver.getCallBeforeSend());
        newOrder.setReceiveAtPost(receiver.getReceiveAtPost());
        newOrder.setCreatedBy(user.getEmail());
        newOrder.setLastUpdatedBy(user.getEmail());
        newOrder.setLastUpdatedDate(LocalDateTime.now());
        newOrder = orderRepository.save(newOrder);


        // delivery
        Delivery newDelivery = deliveryMapper.mapRequestToEntity(orderRequest.getDelivery(), newOrder.getId());
        // TODO: find suitable shipper
        newDelivery.setShipper(deliverService.findBestSuitShipper(newDelivery));
        Delivery savedDelivery = deliveryRepository.save(newDelivery);
        newOrder.setDelivery(savedDelivery);
        newOrder.setOrderStatus(OrderStatus.CREATED);
        newOrder.setCode("ORDER-" + RandomGenUtils.getRandomInt(1, 1000000));

        // 2nd save
        newOrder = orderRepository.saveAndFlush(newOrder);

        OrderHistory newOrderHistory = OrderHistory.builder()
                .orderId(newOrder.getId())
                .description("Đã tạo đơn hàng.")
                .build();
        orderHistoryRepository.save(newOrderHistory);

        log.error("ORDER-SERVICE: DEBUG MODE AT createOrder at 2nd save: {}", newOrder.toString());

        // notify to owner
        notificationService.notifyOrderInfoToOwner(
                owner, user, newOrder,
                String.format(MessageConstants.EMPLOYEE_ORDER_CREATED_MESSAGE, user.getEmail(), newOrder.getCode())
        );
        return orderMapper.mapToDto(newOrder, owner);
    }

    public OrderDto updateOwnerOrder(UUID userId, Long orderId, OrderDto orderDto) {

        List<EmployeeManagement> emplMgnts= employeeManagementRepository.findEmployeeManagementsByEmployee_IdAndApprovalStatus(userId, EmployeeRequestStatus.ACCEPTED);
        if (emplMgnts.isEmpty()) {
            throw new EmployeeManagementException(MessageConstants.ERROR_USER_NOT_HAS_OWNER);
        }
        EmployeeManagement emplMgnt = emplMgnts.get(0);
        // TODO: check employee permission that has get or not
        ShopOwner owner = emplMgnt.getManager();
        ShopOwner user = emplMgnt.getEmployee();

        Order order = orderRepository.findByIdAndShopOwner_Id(orderId, owner.getId())
                .orElseThrow(
                        () -> new OrderNotFoundException(MessageConstants.ORDER_NOT_FOUND)
                );

        Receiver receiver = receiverRepository.findById(order.getReceiverId()).orElseThrow(() -> new RuntimeException(MessageConstants.RECEIVER_NOT_FOUND));
        if (!receiver.getShopOwner().getId().equals(owner.getId())) {
            throw new RuntimeException(MessageConstants.RECEIVER_AND_OWNER_NOT_MATCHED);
        }
//        Store store = storeRepository.findByIdAndShopOwner_Id(order.getStoreId(), order.getShopOwner().getId())
//                .orElseThrow(
//                        () -> new StoreNotFoundException("Store not found by id: " + order.getStoreId().toString())
//                );
        Store store = storeRepository.findById(order.getStoreId()).orElseThrow(() -> new RuntimeException(MessageConstants.STORE_NOT_FOUND_MESSAGE));
        if (!store.getShopOwner().getId().equals(owner.getId())) {
            throw new RuntimeException(MessageConstants.STORE_AND_OWNER_NOT_MATCHED);
        }

        // find delivery
        Delivery delivery = deliveryRepository.findByIdAndOrderId(order.getDelivery().getId(), orderId)
                .orElseThrow(
                        () -> new OrderNotFoundException(String.format(MessageConstants.DELIVERY_NOT_FOUND_BY_ORDER_ID, orderId.toString()))
                );
        //        newOrder.setStore(store);
        order.setStoreId(store.getId());
        order.setStoreName(store.getName());
        order.setStorePhoneNumber(store.getPhoneNumber());
        order .setStoreAddress(store.getAddress());
        order.setStoreDetailedAddress(store.getDetailedAddress());
        order.setStoreDescription(store.getDescription());
        order.setStorePickUpTime(store.getStorePickUpTime());
        order.setIsDefault(store.getIsDefault());
        order.setSendAtPost(store.getSendAtPost());
        order.setSendAtPost(store.getSendAtPost());
//        order.setReceiver(receiver);
        order.setReceiverId(receiver.getId());
        order.setReceiverName(receiver.getName());
        order.setReceiverPhoneNumber(receiver.getPhoneNumber());
        order.setReceiverAddress(receiver.getAddress());
        order.setReceiverDetailedAddress(receiver.getDetailedAddress());
        order.setNote(receiver.getNote());
        order.setReceivedPlace(receiver.getReceivedPlace());
        order.setDeliveryTimeFrame(receiver.getDeliveryTimeFrame());
        order.setCallBeforeSend(receiver.getCallBeforeSend());
        order.setReceiveAtPost(receiver.getReceiveAtPost());

        this.validateOrderStatus(orderDto.getOrderStatus().toString(), order);
        this.checkReceiverIfStatusIsCancelled(orderDto.getOrderStatus().toString(), order);
        order.setOrderStatus(orderDto.getOrderStatus());
        order.setCode(orderDto.getCode());
        order.setHeight(orderDto.getHeight());
        order.setWidth(orderDto.getWidth());
        order.setLength(orderDto.getLength());
        order.setIsDraft(orderDto.getIsDraft());
        order.setPrice(orderDto.getPrice());
        order.setDelivery(delivery);
        order.setDiscount(orderDto.getDiscount());
        order.setIsDocument(orderDto.getIsDocument());
        order.setIsBulky(orderDto.getIsBulky());
        order.setIsFragile(orderDto.getIsFragile());
        order.setIsValuable(orderDto.getIsValuable());
        order.setLastUpdatedBy(user.getEmail());
        order.setLastUpdatedDate(LocalDateTime.now());

        orderRepository.save(order);
        notificationService.notifyOrderInfoToOwner(
                owner,
                user,
                order,
                String.format(MessageConstants.EMPLOYEE_UPDATE_ORDER_STATUS_MESSAGE, user.getEmail(), order.getCode())
        );
        return orderMapper.mapToDto(order, owner);
    }

    public OrderDto updateOwnerOrderStatus(UUID userId, Long orderId, String status) {
        List<EmployeeManagement> emplMgnts= employeeManagementRepository.findEmployeeManagementsByEmployee_IdAndApprovalStatus(userId, EmployeeRequestStatus.ACCEPTED);
        if (emplMgnts.isEmpty()) {
            throw new EmployeeManagementException(MessageConstants.ERROR_USER_NOT_HAS_OWNER);
        }
        EmployeeManagement emplMgnt = emplMgnts.get(0);

        ShopOwner owner = emplMgnt.getManager();
        ShopOwner employee = emplMgnt.getEmployee();
        Order order = orderRepository.findByIdAndShopOwner_Id(orderId, owner.getId())
                .orElseThrow(
                        () -> new OrderNotFoundException(MessageConstants.ORDER_NOT_FOUND)
                );

        validateOrderStatus(status, order);
        checkReceiverIfStatusIsCancelled(status, order);
        order.setOrderStatus(OrderStatus.valueOf(status));
        order.setLastUpdatedBy(employee.getEmail());
        order.setLastUpdatedDate(LocalDateTime.now());

        orderRepository.save(order);
        notificationService.notifyOrderInfoToOwner(
                owner,
                employee,
                order,
                String.format(MessageConstants.EMPLOYEE_ORDER_UPDATED_MESSAGE, employee.getEmail(), order.getCode(), order.getOrderStatus())
        );
        return orderMapper.mapToDto(order, owner);
    }

    public List<TopReceiverDto> getTopReceiver(UUID userId) {
        List<Object[]> topReceivers = orderRepository.findTopReceivers(userId);

        return receiverMapper.mapToTopReceiverDtos(topReceivers);

    }

    public List<ChartStatisticsDto> getStatistic(UUID userId) {
        List<Object[]> statistics = orderRepository.findStatistics(userId);
        return orderMapper.mapToChartStatisticsDtos(statistics);
    }

    public OrderDto getOwnerOrderById(UUID userId, Long orderId) {
        List<EmployeeManagement> emplMgnts= employeeManagementRepository.findEmployeeManagementsByEmployee_IdAndApprovalStatus(userId, EmployeeRequestStatus.ACCEPTED);
        if (emplMgnts.isEmpty()) {
            throw new EmployeeManagementException(MessageConstants.ERROR_USER_NOT_HAS_OWNER);
        }
        EmployeeManagement emplMgnt = emplMgnts.get(0);
        ShopOwner owner = emplMgnt.getManager();
        ShopOwner employee = emplMgnt.getEmployee();
        Order order = orderRepository.findByIdAndShopOwner_Id(orderId, owner.getId())
                .orElseThrow(
                        () -> new OrderNotFoundException(MessageConstants.ORDER_NOT_FOUND)
                );
        return orderMapper.mapToDto(order, owner);
    }


    // count total number of order that is created today
    public DashboardComponentInfoDto getDashboardComponentValues(UUID userId) {
        Long totalTodayOrders = orderRepository.countTotalOrderCreatedToday(userId);
        Long totalTodayDeliveredOrders = orderRepository.countTotalOrderDeliveredToday(userId);
        Double totalRevenue = orderRepository.calculateTotalRevenue(userId);

        return DashboardComponentInfoDto.builder()
                .totalTodayOrders(totalTodayOrders)
                .totalTodayDeliveries(totalTodayDeliveredOrders)
                .totalRevenue(totalRevenue)
                .build();

    }

    // get all order of shipper

    // phân shipper vào

    // update status order

}

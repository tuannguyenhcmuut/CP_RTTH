package org.ut.server.userservice.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ut.server.userservice.dto.OrderDto;
import org.ut.server.userservice.dto.request.OrderRequest;
import org.ut.server.userservice.exception.DeliveryNotFoundException;
import org.ut.server.userservice.exception.ReceiverNotFoundException;
import org.ut.server.userservice.exception.StoreNotFoundException;
import org.ut.server.userservice.exception.UserNotFoundException;
import org.ut.server.userservice.model.*;
import org.ut.server.userservice.model.enums.OrderStatus;
import org.ut.server.userservice.repo.DeliveryRepository;
import org.ut.server.userservice.repo.ReceiverRepository;
import org.ut.server.userservice.repo.StoreRepository;
import org.ut.server.userservice.repo.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class OrderMapper {

    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private ReceiverRepository receiverRepository;
    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private StoreMapper storeMapper;

    @Autowired
    private ReceiverMapper receiverMapper;


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeliveryRepository deliveryRepository;


//    @Autowired
//    private DiscountRepository discountRepository;
    public Order mapRequestToEntity(OrderRequest orderRequest) {
        // find user
        if (orderRequest == null) return null;
        User user = userRepository.findById(orderRequest.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return Order.builder()
                .code(orderRequest.getCode())
                .height(orderRequest.getHeight())
                .width(orderRequest.getWidth())
                .length(orderRequest.getLength())
                .items(orderItemMapper.mapDtosToEntities(orderRequest.getItems()))
                .user(user)
                .store(storeMapper.mapToEntity(orderRequest.getStore(), orderRequest.getUserId()))
                .receiver(receiverMapper.mapDtoToEntity(orderRequest.getReceiver(), orderRequest.getUserId()))
                .orderStatus(orderRequest.getOrderStatus())
                .price(orderRequest.getPrice())
                .delivery(null)
                .discount(orderRequest.getDiscount())
                .isDocument(orderRequest.getIsDocument())
                .isBulky(orderRequest.getIsBulky())
                .isFragile(orderRequest.getIsFragile())
                .isValuable(orderRequest.getIsValuable())
                .build();
    }

    public Order mapDtoToEntity(OrderDto orderDto) {
        // recheck db
        // find discount
//        Optional<Discount> discount = discountRepository.findById(orderDto.getDiscountId().)

        if (orderDto == null) return null;
        Delivery delivery = null;
        if (orderDto.getDeliveryId() != null) {
            delivery = deliveryRepository.findById(orderDto.getDeliveryId())
                    .orElseThrow(() -> new DeliveryNotFoundException("Delivery not found"));
        }
        User user = userRepository.findById(orderDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return Order.builder()
                .code(orderDto.getCode())
                .height(orderDto.getHeight())
                .width(orderDto.getWidth())
                .length(orderDto.getLength())
                .items(orderItemMapper.mapDtosToEntities(orderDto.getOrderItemDtos()))
                .user(user)
                .store(storeMapper.mapToEntity(orderDto.getStoreDto(), orderDto.getUserId()))
                .receiver(receiverMapper.mapDtoToEntity(orderDto.getReceiverDto(), orderDto.getUserId()))
                .orderStatus(orderDto.getOrderStatus())
                .price(orderDto.getPrice())
                .delivery(delivery)
                .discount(orderDto.getDiscount())
                .isDocument(orderDto.getIsDocument())
                .isBulky(orderDto.getIsBulky())
                .isFragile(orderDto.getIsFragile())
                .isValuable(orderDto.getIsValuable())
                .build();
    }

    public List<Order> mapDtosToEntities(List<OrderDto> orderDtos) {
        return orderDtos != null ? orderDtos.stream().map(
                orderDto -> mapDtoToEntity(orderDto)
        ).collect(Collectors.toList()) : null;
    }

    public OrderDto mapToDto(Order order) {
        Receiver receiver = receiverRepository.findReceiverByIdAndUser_Id(order.getReceiver().getId(), order.getUser().getId())
                .orElseThrow(
                        () -> new ReceiverNotFoundException("Receiver not found by id: " + order.getReceiver().getId().toString())
                );

        Store store = storeRepository.findStoreByIdAndUser_Id(order.getStore().getId(), order.getUser().getId())
                .orElseThrow(
                        () -> new StoreNotFoundException("Store not found by id: " + order.getStore().getId().toString())
                );

        if (order != null) {
            return OrderDto.builder()
                    .id(order.getId())
                    .code(order.getCode())
                    .height(order.getHeight())
                    .width(order.getWidth())
                    .length(order.getLength())
                    .orderItemDtos(
                            order.getItems().size() > 0 ? orderItemMapper.mapToDtos(order.getItems(), order.getUser().getId()) : null
                    )
                    .userId(order.getUser().getId())
                    .storeDto(storeMapper.mapToDto(store))
                    .receiverDto(receiverMapper.mapToDto(receiver))
                    .orderStatus(order.getOrderStatus() != null ? order.getOrderStatus() : OrderStatus.CREATED)
                    .price(order.getPrice())
                    .discount(order.getDiscount())
                    .isDocument(order.getIsDocument())
                    .isBulky(order.getIsBulky())
                    .isFragile(order.getIsFragile())
                    .isValuable(order.getIsValuable())
                    .deliveryId(order.getDelivery().getId())
                    .build();
        }
        return null;
    }

    public List<OrderDto> mapToDtos(List<Order> orders) {
        return orders != null ? orders.stream().map(
                order -> mapToDto(order)
        ).collect(Collectors.toList()) : null;
    }

//    public List<Order>

}

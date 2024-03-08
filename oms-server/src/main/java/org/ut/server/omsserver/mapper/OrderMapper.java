package org.ut.server.omsserver.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ut.server.omsserver.dto.OrderDto;
import org.ut.server.omsserver.dto.request.OrderRequest;
import org.ut.server.omsserver.exception.DeliveryNotFoundException;
import org.ut.server.omsserver.exception.ReceiverNotFoundException;
import org.ut.server.omsserver.exception.StoreNotFoundException;
import org.ut.server.omsserver.exception.UserNotFoundException;
import org.ut.server.omsserver.model.*;
import org.ut.server.omsserver.model.enums.OrderStatus;
import org.ut.server.omsserver.repo.*;

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
    private ShopOwnerRepository shopOwnerRepository;

    @Autowired
    private DeliveryRepository deliveryRepository;


//    @Autowired
//    private DiscountRepository discountRepository;
    public Order mapRequestToEntity(OrderRequest orderRequest) {
        // find user
        if (orderRequest == null) return null;
        ShopOwner user = shopOwnerRepository.findById(orderRequest.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return Order.builder()
                .code(orderRequest.getCode())
                .height(orderRequest.getHeight())
                .width(orderRequest.getWidth())
                .length(orderRequest.getLength())
                .items(orderItemMapper.mapDtosToEntities(orderRequest.getItems()))
                .shopOwner(user)
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
        ShopOwner user = shopOwnerRepository.findById(orderDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return Order.builder()
                .code(orderDto.getCode())
                .height(orderDto.getHeight())
                .width(orderDto.getWidth())
                .length(orderDto.getLength())
                .items(orderItemMapper.mapDtosToEntities(orderDto.getOrderItemDtos()))
                .shopOwner(user)
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
        Receiver receiver = receiverRepository.findReceiverByIdAndShopOwner_Id(order.getReceiver().getId(), order.getShopOwner().getId())
                .orElseThrow(
                        () -> new ReceiverNotFoundException("Receiver not found by id: " + order.getReceiver().getId().toString())
                );

        Store store = storeRepository.findStoreByIdAndShopOwner_Id(order.getStore().getId(), order.getShopOwner().getId())
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
                            order.getItems().size() > 0 ? orderItemMapper.mapToDtos(order.getItems(), order.getShopOwner().getId()) : null
                    )
                    .userId(order.getShopOwner().getId())
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

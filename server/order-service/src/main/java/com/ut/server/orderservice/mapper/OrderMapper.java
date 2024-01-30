package com.ut.server.orderservice.mapper;

import com.ut.server.orderservice.config.ReceiverFeign;
import com.ut.server.orderservice.config.StoreFeign;
import com.ut.server.orderservice.dto.OrderDto;
import com.ut.server.orderservice.dto.ReceiverDto;
import com.ut.server.orderservice.dto.StoreDto;
import com.ut.server.orderservice.dto.request.OrderRequest;
import com.ut.server.orderservice.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ut.server.common.events.OrderStatus;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class OrderMapper {

    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private ReceiverFeign receiverFeign;
    @Autowired
    private StoreFeign storeFeign;

//    @Autowired
//    private DiscountRepository discountRepository;
    public Order mapRequestToEntity(OrderRequest orderRequest) {
        if (orderRequest == null) return null;
        return Order.builder()
                .code(orderRequest.getCode())
                .height(orderRequest.getHeight())
                .width(orderRequest.getWidth())
                .depth(orderRequest.getDepth())
                .items(orderItemMapper.mapDtosToEntities(orderRequest.getItems()))
                .userId(orderRequest.getUserId())
                .storeId(orderRequest.getStore().getId())
                .receiverId(orderRequest.getReceiver().getId())
                .orderStatus(orderRequest.getOrderStatus())
                .price(orderRequest.getPrice())
                .deliveryId(null)
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

        return Order.builder()
                .code(orderDto.getCode())
                .height(orderDto.getHeight())
                .width(orderDto.getWidth())
                .depth(orderDto.getDepth())
                .items(orderItemMapper.mapDtosToEntities(orderDto.getItems()))
                .userId(orderDto.getUserId())
                .storeId(orderDto.getStore().getId())
                .receiverId(orderDto.getReceiver().getId())
                .orderStatus(orderDto.getOrderStatus())
                .price(orderDto.getPrice())
                .deliveryId(orderDto.getDeliveryId())
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
        ReceiverDto receiverDto = receiverFeign.getReceiverById(order.getReceiverId(), order.getUserId()).getData();

        StoreDto storeDto = storeFeign.getStoreById(order.getStoreId(), order.getUserId()).getData();

        if (order != null) {
            return OrderDto.builder()
                    .id(order.getId())
                    .code(order.getCode())
                    .height(order.getHeight())
                    .width(order.getWidth())
                    .depth(order.getDepth())
                    .items(
                            order.getItems().size() > 0 ? orderItemMapper.mapToDtos(order.getItems(), order.getUserId()) : null
                    )
                    .userId(order.getUserId())
                    .store(storeDto)
                    .receiver(receiverDto)
                    .orderStatus(order.getOrderStatus() != null ? order.getOrderStatus() : OrderStatus.CREATED)
                    .price(order.getPrice())
                    .discount(order.getDiscount())
                    .isDocument(order.getIsDocument())
                    .isBulky(order.getIsBulky())
                    .isFragile(order.getIsFragile())
                    .isValuable(order.getIsValuable())
                    .deliveryId(order.getDeliveryId())
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

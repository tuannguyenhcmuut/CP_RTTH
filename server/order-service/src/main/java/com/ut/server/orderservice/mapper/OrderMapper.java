package com.ut.server.orderservice.mapper;

import com.ut.server.orderservice.dto.OrderDto;
import com.ut.server.orderservice.model.Order;
import com.ut.server.orderservice.model.OrderItem;
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

//    @Autowired
//    private DiscountRepository discountRepository;

    public Order mapDtoToEntity(OrderDto orderDto) {
        // recheck db
        // find discount
//        Optional<Discount> discount = discountRepository.findById(orderDto.getDiscountId().)

        return Order.builder()
                .id(orderDto.getId())
                .code(orderDto.getCode())
                .height(orderDto.getHeight())
                .width(orderDto.getWidth())
                .depth(orderDto.getDepth())
                .items(
                        orderItemMapper.mapDtosToEntities(orderDto.getItems())
                )
                .userId(orderDto.getUserId())
                .storeId(orderDto.getStoreId())
                .receiverId(orderDto.getReceiverId())
                .orderStatus(orderDto.getOrderStatus() != null ? OrderStatus.valueOf(orderDto.getOrderStatus()) : null)
                .price(orderDto.getPrice())
                .discount(orderDto.getDiscount())
                .shipId(orderDto.getShipId())
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
        if (order != null) {
            return OrderDto.builder()
                    .id(order.getId())
                    .code(order.getCode())
                    .height(order.getHeight())
                    .width(order.getWidth())
                    .depth(order.getDepth())
                    .items(
                            orderItemMapper.mapToDtos(order.getItems())
                    )
                    .userId(order.getUserId()) // Todo: verify userId
                    .storeId(order.getStoreId())
                    .receiverId(order.getReceiverId())
                    .orderStatus(order.getOrderStatus() != null ? order.getOrderStatus().toString() : OrderStatus.CREATED.toString())
                    .price(order.getPrice())
                    .discount(order.getDiscount())
                    .shipId(order.getShipId()) // todo: verify the ship
                    .isBulky(order.getIsBulky())
                    .isFragile(order.getIsFragile())
                    .isValuable(order.getIsValuable())
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

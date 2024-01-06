package com.ut.server.orderservice.mapper;

import com.ut.server.orderservice.dto.OrderDto;
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
    private OrderOptionMapper orderOptionMapper;

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
                .items(orderDto.getItems())
                .userId(orderDto.getUserId())
                .storeId(orderDto.getStoreId())
                .receiverId(orderDto.getReceiverId())
                .orderStatus(OrderStatus.valueOf(orderDto.getOrderStatus()))
                .price(orderDto.getPrice())
                .discount(orderDto.getDiscount())
                .shipId(orderDto.getShipId())
                .orderOptions(
                        orderDto.getOrderOptions() != null ? orderOptionMapper.mapDtosToEntities(orderDto.getOrderOptions()) : null
                )
                .build();
    }

    public List<Order> mapDtosToEntities(List<OrderDto> orderDtos) {
        return orderDtos.stream().map(
                orderDto -> mapDtoToEntity(orderDto)
        ).collect(Collectors.toList());
    }

    public OrderDto mapToDto(Order order) {
        if (order != null) {
            return OrderDto.builder()
                    .id(order.getId())
                    .code(order.getCode())
                    .height(order.getHeight())
                    .width(order.getWidth())
                    .depth(order.getDepth())
                    .items(order.getItems())
                    .userId(order.getUserId()) // Todo: verify userId
                    .storeId(order.getStoreId())
                    .receiverId(order.getReceiverId())
                    .orderStatus(order.getOrderStatus() != null ? order.getOrderStatus().toString() : OrderStatus.CREATED.toString())
                    .price(order.getPrice())
                    .discount(order.getDiscount())
                    .shipId(order.getShipId()) // todo: verify the ship
                    .orderOptions(
                            order.getOrderOptions() != null ? orderOptionMapper.mapToDtos(order.getOrderOptions()) : null
                    )
                    .build();
        }
        return null;
    }

    public List<OrderDto> mapToDtos(List<Order> orders) {
        return orders.stream().map(
                order -> mapToDto(order)
        ).collect(Collectors.toList());
    }

//    public List<Order>

}

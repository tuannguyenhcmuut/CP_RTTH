package com.ut.server.orderservice.mapper;

import com.ut.server.orderservice.config.ProductFeign;
import com.ut.server.orderservice.dto.OrderItemDto;
import com.ut.server.orderservice.dto.ProductResponse;
import com.ut.server.orderservice.model.OrderItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class OrderItemMapper {

    @Autowired
    private ProductFeign productFeign;

    public OrderItem mapDtoToEntity(OrderItemDto orderItemDto) {
        return OrderItem.builder()
                .id(orderItemDto.getId())
                .price(orderItemDto.getPrice())
                .quantity(orderItemDto.getQuantity())
                .orderId(orderItemDto.getOrderId())
                .productId(orderItemDto.getProduct().getId())
                .build();
    }

    public List<OrderItem> mapDtosToEntities(List<OrderItemDto> orderItemDtos) {
        return orderItemDtos != null ? orderItemDtos.stream().map(
                orderItemDto -> mapDtoToEntity(orderItemDto)
        ).collect(Collectors.toList()) : null;
    }

    public OrderItemDto mapToDto(OrderItem orderItem) {
        // find product
        try {
            if (orderItem != null) {
            ProductResponse product = productFeign.getProduct(orderItem.getProductId(), orderItem.getOrderId().getUserId()).getData();
                return OrderItemDto.builder()
                        .id(orderItem.getId())
                        .price(orderItem.getPrice())
                        .quantity(orderItem.getQuantity())
                        .orderId(orderItem.getOrderId())
                        .product(product)
                        .build();
            }
            else return null;
        }
        catch (Exception e) {
            throw new RuntimeException("Product not found at Order item: " + orderItem.getProductId());
        }
    }

    public List<OrderItemDto> mapToDtos(List<OrderItem> orderItems) {
        return orderItems != null ? orderItems.stream().map(
                orderItem -> mapToDto(orderItem)
        ).collect(Collectors.toList()) : null;
    }

}

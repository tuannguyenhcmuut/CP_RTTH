package org.ut.server.omsserver.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ut.server.omsserver.dao.OrderItemDao;
import org.ut.server.omsserver.dto.OrderItemDto;
import org.ut.server.omsserver.model.Order;
import org.ut.server.omsserver.model.OrderItem;
import org.ut.server.omsserver.model.Product;
import org.ut.server.omsserver.repo.OrderRepository;
import org.ut.server.omsserver.repo.ProductRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Slf4j
public class OrderItemMapper {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private OrderRepository orderRepository;

    public OrderItem mapDtoToEntity(OrderItemDto orderItemDto) {
        // find order
        if (orderItemDto == null) return null;
        Order order = null;
        if (orderItemDto.getOrderId() != null) {
            order = orderRepository.findById(orderItemDto.getOrderId())
                    .orElse(null);
        }
        return OrderItem.builder()
                .id(orderItemDto.getId())
                .price(orderItemDto.getPrice())
                .quantity(orderItemDto.getQuantity())
                .orderId(order)
                .product(productMapper.mapDtoToEntity(orderItemDto.getProduct()))
                .build();
    }

    public List<OrderItem> mapDtosToEntities(List<OrderItemDto> orderItemDtos) {
        return orderItemDtos != null ? orderItemDtos.stream().map(
                orderItemDto -> mapDtoToEntity(orderItemDto)
        ).collect(Collectors.toList()) : null;
    }

    @Transactional
    public OrderItemDto mapToDto(OrderItem orderItem, UUID userId) {
        // find product
        try {
            if (orderItem != null) {
            Optional<Product> product = productRepository.findProductByIdAndShopOwner_Id(orderItem.getProduct().getId(), userId);

            return OrderItemDto.builder()
                    .id(orderItem.getId())
                    .price(orderItem.getPrice())
                    .quantity(orderItem.getQuantity())
                    .orderId(orderItem.getOrderId() == null ? null : orderItem.getOrderId().getId())
                    .product(productMapper.mapToDto(product.get(), null))
                    .build();
            }
            else return null;
        }
        catch (Exception e) {
            throw new RuntimeException("Product not found at Order item: " + orderItem.getProduct());
        }
    }

    public List<OrderItemDto> mapToDtos(List<OrderItem> orderItems, UUID userId) {
        return orderItems != null ? orderItems.stream().map(
                orderItem -> mapToDto(orderItem, userId)
        ).collect(Collectors.toList()) : null;
    }

    public OrderItemDao mapDtoToDao(OrderItemDto orderItemDto) {
        return OrderItemDao.builder()
                .price(orderItemDto.getPrice())
                .quantity(orderItemDto.getQuantity())
                .productId(orderItemDto.getProduct().getId())
                .build();
    }
}

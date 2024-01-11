package com.ut.server.orderservice.service;


import com.ut.server.orderservice.config.ProductFeign;
import com.ut.server.orderservice.dto.OrderItemDto;
import com.ut.server.orderservice.dto.ProductResponse;
import com.ut.server.orderservice.mapper.OrderItemMapper;
import com.ut.server.orderservice.model.OrderItem;
import com.ut.server.orderservice.repo.OrderItemRepository;
import com.ut.server.orderservice.repo.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final ProductFeign productFeign;
    private final OrderItemMapper orderItemMapper;
    private final OrderRepository orderRepository;

    public OrderItemDto getOrderItem(Long orderItemId, UUID userId) {
         Optional<OrderItem> orderItem = orderItemRepository.findById(orderItemId);

         if (orderItem.isPresent()) {
             try {
                 ProductResponse product = productFeign.getProduct(orderItem.get().getProductId(), userId).getData();

                 return OrderItemDto.builder()
                         .id(orderItem.get().getId())
                         .price(orderItem.get().getPrice())
                         .quantity(orderItem.get().getQuantity())
//                         .orderId(orderItem.get().getOrderId().getId())
                         .product(product)
                         .build();

                } catch (Exception e) {
                 log.error("Error when get product by id: {}", orderItem.get().getProductId());
                 throw new RuntimeException("Product not found at Order item at product: " + orderItem.get().getProductId());
             }
         }
         else return null;
    }

//    public OrderItemDto createOrderItem(OrderItemDto orderItemDto) {
//        // find order by id
//        OrderItem orderItem = OrderItem.builder()
//                        .price(orderItemDto.getPrice())
//                        .quantity(orderItemDto.getQuantity())
////                        .orderId(orderItemDto.getOrderId())
//                        .price(orderItemDto.getPrice())
//                        .build();
//        orderItemRepository.save(orderItem);
//        return orderItemMapper.mapToOrderItemDto(orderItem);
//    }


}

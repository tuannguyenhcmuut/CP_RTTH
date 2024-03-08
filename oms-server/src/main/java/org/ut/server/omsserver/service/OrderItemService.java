package org.ut.server.omsserver.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.ut.server.omsserver.dto.OrderItemDto;
import org.ut.server.omsserver.exception.ProductNotFoundException;
import org.ut.server.omsserver.mapper.ProductMapper;
import org.ut.server.omsserver.model.OrderItem;
import org.ut.server.omsserver.model.Product;
import org.ut.server.omsserver.repo.OrderItemRepository;
import org.ut.server.omsserver.repo.ProductRepository;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public OrderItemDto getOrderItem(Long orderItemId, UUID userId) {
         Optional<OrderItem> orderItem = orderItemRepository.findById(orderItemId);

         if (orderItem.isPresent()) {
             try {
                 Product product = productRepository.findProductByIdAndShopOwner_Id(orderItem.get().getProduct().getId(), userId)
                         .orElseThrow(
                                 () -> new ProductNotFoundException("Product not found at Order item at item id: " + orderItem.get().getId())
                         );

                 return OrderItemDto.builder()
                         .id(orderItem.get().getId())
                         .price(orderItem.get().getPrice())
                         .quantity(orderItem.get().getQuantity())
                         .product(productMapper.mapToDto(product))
                         .build();

                } catch (Exception e) {
                 log.error("Error when get product by id: {}", orderItem.get().getProduct());
                 throw new RuntimeException("Product not found at Order item at item id: " + orderItem.get().getId());
             }
         }
         else return null;
    }

    public void deleteOrderItem(OrderItem orderItem) {
        orderItemRepository.delete(orderItem);

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

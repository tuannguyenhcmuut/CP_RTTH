package org.ut.server.userservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.ut.server.userservice.dto.DeliveryDto;
import org.ut.server.userservice.dto.request.DeliveryRequest;
import org.ut.server.userservice.exception.DeliveryNotFoundException;
import org.ut.server.userservice.exception.OrderNotFoundException;
import org.ut.server.userservice.mapper.DeliveryMapper;
import org.ut.server.userservice.model.Delivery;
import org.ut.server.userservice.model.Order;
import org.ut.server.userservice.repo.DeliveryRepository;
import org.ut.server.userservice.repo.OrderRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper deliveryMapper;
    private final OrderRepository orderRepository;

    public DeliveryDto createDelivery(
            DeliveryRequest deliveryRequest, Long orderId
    ) {
        // validate input

        Delivery newDelivery = deliveryMapper.mapRequestToEntity(deliveryRequest, orderId);
        Delivery savedDelivery = deliveryRepository.save(newDelivery);

        return deliveryMapper.mapEntityToDto(savedDelivery);
    }

    public DeliveryDto getDeliveryById(Long deliveryId, UUID userId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new DeliveryNotFoundException("Delivery not found"));

        log.debug("Delivery found: {}", delivery);
        // find order
        Order order = orderRepository.findOrderByIdAndUser_Id(delivery.getOrder().getId(), userId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        log.debug("Order found: {}", order);
        return deliveryMapper.mapEntityToDto(delivery);
    }
}

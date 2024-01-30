package org.ut.server.deliveryservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.ut.server.deliveryservice.config.OrderFeign;
import org.ut.server.deliveryservice.dto.DeliveryDto;
import org.ut.server.deliveryservice.dto.DeliveryRequest;
import org.ut.server.deliveryservice.mapper.DeliveryMapper;
import org.ut.server.deliveryservice.model.Delivery;
import org.ut.server.deliveryservice.repository.DeliveryRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper deliveryMapper;
    private final OrderFeign orderFeign;

    public DeliveryDto createDelivery(
            DeliveryRequest deliveryRequest, Long orderId
    ) {
        // validate input

        Delivery newDelivery = deliveryMapper.mapRequestToEntity(deliveryRequest, orderId);
        Delivery savedDelivery = deliveryRepository.save(newDelivery);

        return deliveryMapper.mapEntityToDto(savedDelivery);
    }

    public DeliveryDto getDeliveryById(Long deliveryId, UUID userId) {
        // get userId from orderFeign
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(() -> new RuntimeException("Delivery not found"));

        if (orderFeign.getOrderById(userId, delivery.getOrderId()).getData() == null) {
            throw new RuntimeException("Order not found");
        }
        // now userId is safe to use
        // todo: add order information to delivery
        return deliveryMapper.mapEntityToDto(delivery);
    }
}

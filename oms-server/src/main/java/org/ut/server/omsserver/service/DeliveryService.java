package org.ut.server.omsserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.ut.server.omsserver.dto.DeliveryDto;
import org.ut.server.omsserver.dto.request.DeliveryRequest;
import org.ut.server.omsserver.exception.DeliveryNotFoundException;
import org.ut.server.omsserver.exception.OrderNotFoundException;
import org.ut.server.omsserver.exception.OrderUpdateException;
import org.ut.server.omsserver.mapper.DeliveryMapper;
import org.ut.server.omsserver.model.Delivery;
import org.ut.server.omsserver.model.Order;
import org.ut.server.omsserver.model.Shipper;
import org.ut.server.omsserver.model.enums.DeliveryStatus;
import org.ut.server.omsserver.model.enums.OrderStatus;
import org.ut.server.omsserver.repo.DeliveryRepository;
import org.ut.server.omsserver.repo.OrderRepository;
import org.ut.server.omsserver.repo.ShipperRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryService {
    private final ShipperRepository shipperRepository;
    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper deliveryMapper;
    private final OrderRepository orderRepository;
//    private final OrderService orderService;

    public Delivery createDelivery(
            DeliveryRequest deliveryRequest, Long orderId
    ) {
        // validate input
        // todo: find best suit shipper based on the
        Delivery newDelivery = deliveryMapper.mapRequestToEntity(deliveryRequest, orderId);
        newDelivery.setCreatedDate(LocalDateTime.now());
        newDelivery.setLastUpdated(LocalDateTime.now());
        newDelivery.setStatus(DeliveryStatus.PENDING);
        Shipper shipper = findBestSuitShipper(newDelivery);
        newDelivery.setShipper(shipper);

        return deliveryRepository.save(newDelivery);
    }

    public DeliveryDto getDeliveryById(Long deliveryId, UUID userId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new DeliveryNotFoundException("Delivery not found"));

        log.debug("Delivery found: {}", delivery);
        // find order
        Order order = orderRepository.findOrderByIdAndShopOwner_Id(delivery.getOrder().getId(), userId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        log.debug("Order found: {}", order);
        return deliveryMapper.mapEntityToDto(delivery);
    }

    public Shipper findBestSuitShipper(Delivery newDelivery) {
        // TODO: tạm thời tìm dc thằng shipper đầu
        List<Shipper> shippers = shipperRepository.findAll();
        if (shippers.isEmpty()) {
            return null;
        }
        return shippers.get(0);
    }
    /* SHIPPER */
    // get all order of shipper
    public List<DeliveryDto> getAllDeliveryOfShipper(UUID userId, Pageable pageable) {
        List<Delivery> deliveries = deliveryRepository.findAllByShipper_Id(userId, pageable);
        if (deliveries.isEmpty()) {
            return List.of();
        }
        return deliveries.stream()
                .map(deliveryMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    public DeliveryDto updateStatusDelivery(Long deliveryId, UUID shipperId, DeliveryStatus status) {
        Delivery delivery = deliveryRepository.findByIdAndShipper_Id(deliveryId, shipperId)
                .orElseThrow(() -> new DeliveryNotFoundException("Delivery not found"));
        if (!delivery.getShipper().getId().equals(shipperId)) {
            throw new RuntimeException("You are not the shipper of this delivery");
        }
        validateOrderStatus(status.toString(), delivery);
        delivery.setStatus(status);
        if (status.equals(DeliveryStatus.DELIVERED)) {
            delivery.setDeliveryDate(LocalDateTime.now());
        }
        if (status.equals(DeliveryStatus.SHIPPING)) {
            delivery.setDeliveryDate(LocalDateTime.now());
        }
        delivery.setLastUpdated(LocalDateTime.now());
        Delivery savedDelivery = deliveryRepository.save(delivery);

        if (status.equals(DeliveryStatus.SHIPPING) || status.equals(DeliveryStatus.DELIVERED) || status.equals(DeliveryStatus.CANCELED)) {
            this.updateOrderStatusForShipper(shipperId, savedDelivery.getOrder().getId(), status.toString());
        }
        return deliveryMapper.mapEntityToDto(savedDelivery);
    }

    private static void validateOrderStatus(String status, Delivery delivery) {
        if (!DeliveryStatus.contains(status)) {
            throw new OrderUpdateException("Invalid order status");
        }
//        CREATED, -> chi dc update thanh cancelled hoac processing
        if (delivery.getStatus().equals(DeliveryStatus.PENDING)) {
            if (!status.equals("CANCELLED") && !status.equals("SHIPPING")) {
                throw new OrderUpdateException(String.format("Cannot update status to %s from PENDING status", status));
            }
        }
//        PROCESSING -> chi dc update thanh DELIVERED hoac cancelled
        if (delivery.getStatus().equals(DeliveryStatus.SHIPPING)) {
            if (!status.equals("DELIVERED") && !status.equals("CANCELLED")) {
                throw new OrderUpdateException(String.format("Cannot update status to %s from SHIPPING status", status));
            }
        }

//        DELIVERED -> chi dc update thanh cancelled
        if (delivery.getStatus().equals(DeliveryStatus.DELIVERED)) {
            if (!status.equals("CANCELLED")) {
                throw new OrderUpdateException(String.format("Cannot update status to %s from DELIVERED status", status));
            }
        }
//        CANCELLED -> khong dc update
        if (delivery.getStatus().equals(DeliveryStatus.CANCELED)) {
            throw new OrderUpdateException("Cannot update from CANCELLED status");
        }
    }

    private Order updateOrderStatusForShipper(UUID shipperId, Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        if (!order.getDelivery().getShipper().getId().equals(shipperId)) {
            throw new RuntimeException("You are not the shipper of this delivery");
        }
//        validateOrderStatus(status, order);
        order.setOrderStatus(OrderStatus.valueOf(status));
        order.setLastUpdatedBy(order.getDelivery().getShipper().getEmail());
        order.setLastUpdatedDate(LocalDateTime.now());
        orderRepository.save(order);
        return order;
    }
}

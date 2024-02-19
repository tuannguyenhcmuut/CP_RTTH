package org.ut.server.userservice.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ut.server.userservice.dto.DeliveryDto;
import org.ut.server.userservice.dto.request.DeliveryRequest;
import org.ut.server.userservice.exception.OrderNotFoundException;
import org.ut.server.userservice.model.Delivery;
import org.ut.server.userservice.model.Order;
import org.ut.server.userservice.model.enums.DeliveryStatus;
import org.ut.server.userservice.repo.OrderRepository;

@Component
public class DeliveryMapper {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderMapper orderMapper;

    public Delivery mapRequestToEntity(DeliveryRequest deliveryRequest, Long orderId) {
        // find order
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        return Delivery.builder()
                .payer(deliveryRequest.getPayer())
                .hasLostInsurance(deliveryRequest.isHasLostInsurance())
                .isCollected(deliveryRequest.isCollected())
                .deliveryMethod(deliveryRequest.getDeliveryMethod())
                .luuKho(deliveryRequest.getLuuKho())
                .layHang(deliveryRequest.getLayHang())
                .giaoHang(deliveryRequest.getGiaoHang())
                .shippingFee(deliveryRequest.getShippingFee())
                .collectionFee(deliveryRequest.getCollectionFee())
                .isDraft(deliveryRequest.getIsDraft())
                .note(deliveryRequest.getNote())
                .order(order)
                .status(DeliveryStatus.PENDING)
                .build();
    }

    public DeliveryDto mapEntityToDto(Delivery delivery) {

        return DeliveryDto.builder()
                .id(delivery.getId())
                .orderDto(orderMapper.mapToDto(delivery.getOrder()))
                .shipperId(delivery.getShipperId())
                .status(delivery.getStatus())
                .shipperName(delivery.getShipperName())
                .shipperPhone(delivery.getShipperPhone())
                .payer(delivery.getPayer())
                .hasLostInsurance(delivery.isHasLostInsurance())
                .isCollected(delivery.isCollected())
                .deliveryMethod(delivery.getDeliveryMethod())
                .luuKho(delivery.getLuuKho())
                .layHang(delivery.getLayHang())
                .giaoHang(delivery.getGiaoHang())
                .shippingFee(delivery.getShippingFee())
                .collectionFee(delivery.getCollectionFee())
                .isDraft(delivery.getIsDraft())
                .note(delivery.getNote())
                .build();
    }
}

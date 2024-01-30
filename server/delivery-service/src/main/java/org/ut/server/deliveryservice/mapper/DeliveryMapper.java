package org.ut.server.deliveryservice.mapper;

import org.springframework.stereotype.Component;
import org.ut.server.deliveryservice.dto.DeliveryDto;
import org.ut.server.deliveryservice.dto.DeliveryRequest;
import org.ut.server.deliveryservice.model.Delivery;
import org.ut.server.common.events.DeliveryStatus;

@Component
public class DeliveryMapper {

    public Delivery mapRequestToEntity(DeliveryRequest deliveryRequest, Long orderId) {
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
                .orderId(orderId)
                .status(DeliveryStatus.PENDING)
                .build();
    }

    public DeliveryDto mapEntityToDto(Delivery delivery) {
        return DeliveryDto.builder()
                .id(delivery.getId())
                .orderId(delivery.getOrderId())
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

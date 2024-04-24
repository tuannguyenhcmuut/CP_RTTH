package org.ut.server.omsserver.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ut.server.omsserver.dto.DeliveryDto;
import org.ut.server.omsserver.dto.request.DeliveryRequest;
import org.ut.server.omsserver.exception.OrderNotFoundException;
import org.ut.server.omsserver.model.*;
import org.ut.server.omsserver.model.enums.DeliveryStatus;
import org.ut.server.omsserver.repo.OrderRepository;

@Component
public class DeliveryMapper {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private ShipperMapper shipperMapper;
//    receiverMapper
    @Autowired
    private ReceiverMapper receiverMapper;
//    storeMapper
    @Autowired
    private StoreMapper storeMapper;

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
// find order
        Order order = orderRepository.findById(delivery.getOrder().getId())
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        return DeliveryDto.builder()
                .id(delivery.getId())
                .order(orderMapper.mapToDto(delivery.getOrder(), null)) // TODO:
                .shipper(shipperMapper.mapToDto(delivery.getShipper()))
                .receiver(receiverMapper.mapToDto(order.getReceiver(), null))
                .store(storeMapper.mapToDto(order.getStore(), null))
                .status(delivery.getStatus())
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
                .deliveryDate(delivery.getDeliveryDate())
                .receivedDate(delivery.getReceivedDate())
                .lastUpdated(delivery.getLastUpdated())
                .build();
    }
}

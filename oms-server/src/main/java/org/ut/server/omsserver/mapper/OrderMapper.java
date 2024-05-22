package org.ut.server.omsserver.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ut.server.omsserver.common.MessageConstants;
import org.ut.server.omsserver.dto.ChartStatisticsDto;
import org.ut.server.omsserver.dto.OrderDto;
import org.ut.server.omsserver.dto.request.OrderRequest;
import org.ut.server.omsserver.exception.DeliveryNotFoundException;
import org.ut.server.omsserver.exception.UserNotFoundException;
import org.ut.server.omsserver.model.*;
import org.ut.server.omsserver.model.enums.OrderStatus;
import org.ut.server.omsserver.repo.DeliveryRepository;
import org.ut.server.omsserver.repo.ReceiverRepository;
import org.ut.server.omsserver.repo.ShopOwnerRepository;
import org.ut.server.omsserver.repo.StoreRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class OrderMapper {

    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private ReceiverRepository receiverRepository;
    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private StoreMapper storeMapper;

    @Autowired
    private ReceiverMapper receiverMapper;


    @Autowired
    private ShopOwnerRepository shopOwnerRepository;

    @Autowired
    private DeliveryRepository deliveryRepository;


//    @Autowired
//    private DiscountRepository discountRepository;
    public Order mapRequestToEntity(OrderRequest orderRequest) {
        // find user
        if (orderRequest == null) return null;
        ShopOwner user = shopOwnerRepository.findById(orderRequest.getUserId())
                .orElseThrow(() -> new UserNotFoundException(MessageConstants.USER_NOT_FOUND));

        Store store = storeMapper.mapToEntity(orderRequest.getStore(), orderRequest.getUserId());
        Receiver receiver = receiverMapper.mapDtoToEntity(orderRequest.getReceiver(), orderRequest.getUserId());
        return Order.builder()
                .code(orderRequest.getCode())
                .height(orderRequest.getHeight())
                .width(orderRequest.getWidth())
                .length(orderRequest.getLength())
                .isDraft(orderRequest.getIsDraft())
                .items(orderItemMapper.mapDtosToEntities(orderRequest.getItems()))
                .shopOwner(user)
                .storeId(store.getId())
                .storeName(store.getName())
                .storePhoneNumber(store.getPhoneNumber())
                .storeAddress(store.getAddress())
                .storeDetailedAddress(store.getDetailedAddress())
                .storeDescription(store.getDescription())
                .storePickUpTime(store.getStorePickUpTime())
                .isDefault(store.getIsDefault())
                .sendAtPost(store.getSendAtPost())
                .receiverId(receiver.getId())
                .receiverName(receiver.getName())
                .receiverPhoneNumber(receiver.getPhoneNumber())
                .receiverAddress(receiver.getAddress())
                .receiverDetailedAddress(receiver.getDetailedAddress())
                .note(receiver.getNote())
                .receivedPlace(receiver.getReceivedPlace())
                .deliveryTimeFrame(receiver.getDeliveryTimeFrame())
                .callBeforeSend(receiver.getCallBeforeSend())
                .receiveAtPost(receiver.getReceiveAtPost())
                .orderStatus(orderRequest.getOrderStatus())
                .price(orderRequest.getPrice())
                .delivery(null)
                .discount(orderRequest.getDiscount())
                .isDocument(orderRequest.getIsDocument())
                .isBulky(orderRequest.getIsBulky())
                .isFragile(orderRequest.getIsFragile())
                .isValuable(orderRequest.getIsValuable())
                .createdBy(orderRequest.getCreatedBy())
                .build();
    }

    public Order mapDtoToEntity(OrderDto orderDto) {
        // recheck db
        // find discount
//        Optional<Discount> discount = discountRepository.findById(orderDto.getDiscountId().)

        if (orderDto == null) return null;
        Delivery delivery = null;
        if (orderDto.getDeliveryId() != null) {
            delivery = deliveryRepository.findById(orderDto.getDeliveryId())
                    .orElseThrow(() -> new DeliveryNotFoundException(MessageConstants.DELIVERY_NOT_FOUND));
        }
        ShopOwner user = shopOwnerRepository.findById(orderDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException(MessageConstants.USER_NOT_FOUND_MESSAGE));
        return Order.builder()
                .code(orderDto.getCode())
                .height(orderDto.getHeight())
                .width(orderDto.getWidth())
                .length(orderDto.getLength())
                .isDraft(orderDto.getIsDraft())
                .items(orderItemMapper.mapDtosToEntities(orderDto.getOrderItemDtos()))
                .shopOwner(user)
                .storeId(orderDto.getStoreDto().getStoreId())
                .storeName(orderDto.getStoreDto().getName())
                .storePhoneNumber(orderDto.getStoreDto().getPhoneNumber())
                .storeAddress(orderDto.getStoreDto().getAddress())
                .storeDetailedAddress(orderDto.getStoreDto().getDetailedAddress())
                .storeDescription(orderDto.getStoreDto().getDescription())
                .storePickUpTime(orderDto.getStoreDto().getStorePickUpTime())
                .isDefault(orderDto.getStoreDto().getIsDefault())
                .sendAtPost(orderDto.getStoreDto().getSendAtPost())
                .receiverId(orderDto.getReceiverDto().getReceiverId())
                .receiverName(orderDto.getReceiverDto().getName())
                .receiverPhoneNumber(orderDto.getReceiverDto().getPhoneNumber())
                .receiverAddress(orderDto.getReceiverDto().getAddress())
                .receiverDetailedAddress(orderDto.getReceiverDto().getDetailedAddress())
                .note(orderDto.getReceiverDto().getNote())
                .receivedPlace(orderDto.getReceiverDto().getReceivedPlace())
                .deliveryTimeFrame(orderDto.getReceiverDto().getDeliveryTimeFrame())
                .callBeforeSend(orderDto.getReceiverDto().getCallBeforeSend())
                .receiveAtPost(orderDto.getReceiverDto().getReceiveAtPost())
                .orderStatus(orderDto.getOrderStatus())
                .price(orderDto.getPrice())
                .delivery(delivery)
                .discount(orderDto.getDiscount())
                .isDocument(orderDto.getIsDocument())
                .isBulky(orderDto.getIsBulky())
                .isFragile(orderDto.getIsFragile())
                .isValuable(orderDto.getIsValuable())
                .createdBy(orderDto.getCreatedBy())
                .createdDate(orderDto.getCreatedDate())
                .lastUpdatedBy(orderDto.getLastUpdatedBy())
                .lastUpdatedDate(orderDto.getLastUpdatedDate())
                .build();
    }

    public List<Order> mapDtosToEntities(List<OrderDto> orderDtos) {
        return orderDtos != null ? orderDtos.stream().map(
                orderDto -> mapDtoToEntity(orderDto)
        ).collect(Collectors.toList()) : null;
    }

    public OrderDto mapToDto(Order order, ShopOwner owner) {
//        Receiver receiver = receiverRepository.findReceiverByIdAndShopOwner_Id(order.getReceiver().getId(), order.getShopOwner().getId())
//                .orElseThrow(
//                        () -> new ReceiverNotFoundException("Receiver not found by id: " + order.getReceiver().getId().toString())
//                );

//        Receiver receiver = receiverRepository.findById(order.getReceiverId()).orElseThrow(() -> new RuntimeException("Receiver not found"));
//        if (!receiver.getShopOwner().getId().equals(
//                owner != null ? owner.getId() : order.getShopOwner().getId()
//        )) {
//            throw new RuntimeException(MessageConstants.RECEIVER_AND_OWNER_NOT_MATCHED);
//        }

//        Store store = storeRepository.findStoreByIdAndShopOwner_Id(order.getStore().getId(), order.getShopOwner().getId())
//                .orElseThrow(
//                        () -> new StoreNotFoundException("Store not found by id: " + order.getStore().getId().toString())
//                );

//        Store store = storeRepository.findById(order.getStoreId()).orElseThrow(() -> new RuntimeException("Store not found"));
//        if (!store.getShopOwner().getId().equals(
//                owner != null ? owner.getId() : order.getShopOwner().getId()
//        )) {
//            throw new RuntimeException(MessageConstants.STORE_AND_OWNER_NOT_MATCHED);
//        }

        Receiver receiver = Receiver.builder()
                .id(order.getReceiverId())
                .name(order.getReceiverName())
                .phoneNumber(order.getReceiverPhoneNumber())
                .address(order.getReceiverAddress())
                .detailedAddress(order.getReceiverDetailedAddress())
                .note(order.getNote())
                .receivedPlace(order.getReceivedPlace())
                .deliveryTimeFrame(order.getDeliveryTimeFrame())
                .callBeforeSend(order.getCallBeforeSend())
                .receiveAtPost(order.getReceiveAtPost())
                .shopOwner(order.getShopOwner())
                .build();

        Store store = Store.builder()
                .id(order.getStoreId())
                .name(order.getStoreName())
                .phoneNumber(order.getStorePhoneNumber())
                .address(order.getStoreAddress())
                .detailedAddress(order.getStoreDetailedAddress())
                .description(order.getStoreDescription())
                .storePickUpTime(order.getStorePickUpTime())
                .isDefault(order.getIsDefault())
                .sendAtPost(order.getSendAtPost())
                .build();

        if (order != null) {
            return OrderDto.builder()
                    .id(order.getId())
                    .code(order.getCode())
                    .height(order.getHeight())
                    .width(order.getWidth())
                    .length(order.getLength())
                    .isDraft(order.getIsDraft())
                    .orderItemDtos(
                            order.getItems().size() > 0 ? orderItemMapper.mapToDtos(order.getItems(), order.getShopOwner().getId()) : null
                    )
                    .userId(order.getShopOwner().getId())
                    .storeDto(storeMapper.mapToDto(store, null))
                    .receiverDto(receiverMapper.mapToDto(receiver,null))
                    .orderStatus(order.getOrderStatus() != null ? order.getOrderStatus() : OrderStatus.CREATED)
                    .price(order.getPrice())
                    .discount(order.getDiscount())
                    .isDocument(order.getIsDocument())
                    .isBulky(order.getIsBulky())
                    .isFragile(order.getIsFragile())
                    .isValuable(order.getIsValuable())
                    .deliveryId(order.getDelivery().getId())
                    .createdBy(order.getCreatedBy())
                    .createdDate(order.getCreatedDate())
                    .lastUpdatedBy(order.getLastUpdatedBy())
                    .lastUpdatedDate(order.getLastUpdatedDate())
                    .ownerId(owner == null ? null : owner.getId())
                    .ownerName(owner == null ? null : String.format("%s %s", owner.getFirstName(), owner.getLastName()))
                    .build();
        }
        return null;
    }

    public List<OrderDto> mapToDtos(List<Order> orders, ShopOwner owner) {
        return orders != null ? orders.stream().map(
                order -> mapToDto(order, owner)
        ).collect(Collectors.toList()) : null;
    }

    public ChartStatisticsDto mapToChartStatisticsDto(Object[] statistics) {
        return statistics != null ? ChartStatisticsDto.builder()
                .monthYear(statistics[0].toString())
                .totalOrder(Integer.parseInt(statistics[1].toString()))
                .totalAmount(Double.parseDouble(statistics[2].toString()))
                .build() : null;
    }
    public List<ChartStatisticsDto> mapToChartStatisticsDtos(List<Object[]> statistics) {
        return statistics != null ? statistics.stream().map(
                obj -> mapToChartStatisticsDto(obj)
        ).collect(Collectors.toList()) : null;
    }

//    public List<Order>

}

package org.ut.server.omsserver.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.ut.server.omsserver.dto.ReceiverDto;
import org.ut.server.omsserver.model.Receiver;
import org.ut.server.omsserver.model.ShopOwner;
import org.ut.server.omsserver.repo.ShopOwnerRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ReceiverMapper {
    private final ShopOwnerRepository shopOwnerRepository;
    public ReceiverDto mapToDto(Receiver receiver, ShopOwner owner) {
        return ReceiverDto.builder()
                .id(receiver.getId())
                .name(receiver.getName())
                .phoneNumber(receiver.getPhoneNumber())
                .address(receiver.getAddress())
                .detailedAddress(receiver.getDetailedAddress())
                .note(receiver.getNote())
                .receivedPlace(receiver.getReceivedPlace())
                .deliveryTimeFrame(receiver.getDeliveryTimeFrame())
                .callBeforeSend(receiver.getCallBeforeSend())
                .receivedPlace(receiver.getReceivedPlace())
                .deliveryTimeFrame(receiver.getDeliveryTimeFrame())
                .callBeforeSend(receiver.getCallBeforeSend())
                .receiveAtPost(receiver.getReceiveAtPost())
                .ownerId(owner == null ? null : owner.getId())
                .ownerName(owner == null ? null : String.format("%s %s", owner.getFirstName(), owner.getLastName()))
                .build();
    }

    public List<ReceiverDto> mapToDtos(List<Receiver> receivers, ShopOwner owner) {
        if (receivers != null) {
            return receivers.stream().map(
                    receiver -> mapToDto(receiver, owner)
            ).collect(Collectors.toList());
        }
        else {
            return null;
        }
    }

    public Receiver mapDtoToEntity(ReceiverDto receiverDto, UUID userId) {
        ShopOwner user = shopOwnerRepository.findById(userId).orElseThrow(() -> new RuntimeException("ShopOwner not found"));
        return Receiver.builder()
                .name(receiverDto.getName())
                .phoneNumber(receiverDto.getPhoneNumber())
                .address(receiverDto.getAddress())
                .detailedAddress(receiverDto.getDetailedAddress())
                .shopOwner(user)
                .note(receiverDto.getNote())
                .receivedPlace(receiverDto.getReceivedPlace())
                .deliveryTimeFrame(receiverDto.getDeliveryTimeFrame())
                .callBeforeSend(receiverDto.getCallBeforeSend())
                .receiveAtPost(receiverDto.getReceiveAtPost())
                .build();
    }

    public List<Receiver> mapDtosToEntities(List<ReceiverDto> receiverDtos, UUID userId) {
        if (receiverDtos != null) {
            return receiverDtos.stream().map(
                    receiverDto -> mapDtoToEntity(receiverDto, userId)
            ).collect(Collectors.toList());
        }
        else {
            return null;
        }
    }
}

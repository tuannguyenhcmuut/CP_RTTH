package org.ut.server.common.server.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.ut.server.common.server.dto.ReceiverDto;
import org.ut.server.common.server.model.Receiver;
import org.ut.server.common.server.model.User;
import org.ut.server.common.server.repo.UserRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ReceiverMapper {
    private final UserRepository userRepository;
    public ReceiverDto mapToDto(Receiver receiver) {
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
                .build();
    }

    public List<ReceiverDto> mapToDtos(List<Receiver> receivers) {
        if (receivers != null) {
            return receivers.stream().map(
                    receiver -> mapToDto(receiver)
            ).collect(Collectors.toList());
        }
        else {
            return null;
        }
    }

    public Receiver mapDtoToEntity(ReceiverDto receiverDto, UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return Receiver.builder()
                .name(receiverDto.getName())
                .phoneNumber(receiverDto.getPhoneNumber())
                .address(receiverDto.getAddress())
                .detailedAddress(receiverDto.getDetailedAddress())
                .user(user)
                .note(receiverDto.getNote())
                .receivedPlace(receiverDto.getReceivedPlace())
                .deliveryTimeFrame(receiverDto.getDeliveryTimeFrame())
                .callBeforeSend(receiverDto.getCallBeforeSend())
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

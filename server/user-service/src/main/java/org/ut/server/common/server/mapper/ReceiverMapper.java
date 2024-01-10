package org.ut.server.common.server.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.ut.server.common.server.dto.ReceiverDto;
import org.ut.server.common.server.model.Receiver;
import org.ut.server.common.server.model.User;
import org.ut.server.common.server.repo.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ReceiverMapper {
    private final UserRepository userRepository;
    public ReceiverDto mapToDto(Receiver receiver) {
        return ReceiverDto.builder()
                .id(receiver.getId())
                .username(receiver.getUsername())
                .phoneNumber(receiver.getPhoneNumber())
                .address(receiver.getAddress())
                .userId(receiver.getUser().getId())
                .receivedAtPost(receiver.getReceivedAtPost())
                .postAddress(receiver.getPostAddress())
                .note(receiver.getNote())
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

    public Receiver mapDtoToEntity(ReceiverDto receiverDto) {
        User user = userRepository.findById(receiverDto.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        return Receiver.builder()
                .id(receiverDto.getId())
                .user(user)
                .username(receiverDto.getUsername())
                .phoneNumber(receiverDto.getPhoneNumber())
                .address(receiverDto.getAddress())
                .receivedAtPost(receiverDto.getReceivedAtPost())
                .postAddress(receiverDto.getPostAddress())
                .note(receiverDto.getNote())
                .build();
    }

    public List<Receiver> mapDtosToEntities(List<ReceiverDto> receiverDtos) {
        if (receiverDtos != null) {
            return receiverDtos.stream().map(
                    receiverDto -> mapDtoToEntity(receiverDto)
            ).collect(Collectors.toList());
        }
        else {
            return null;
        }
    }
}

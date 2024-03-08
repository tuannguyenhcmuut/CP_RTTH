package org.ut.server.omsserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.ut.server.omsserver.common.MessageConstants;
import org.ut.server.omsserver.dto.ReceiverDto;
import org.ut.server.omsserver.exception.UserNotFoundException;
import org.ut.server.omsserver.mapper.ReceiverMapper;
import org.ut.server.omsserver.model.Receiver;
import org.ut.server.omsserver.model.ShopOwner;
import org.ut.server.omsserver.repo.ReceiverRepository;
import org.ut.server.omsserver.repo.ShopOwnerRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReceiverService {
    private final ReceiverRepository receiverRepository;
    private final ShopOwnerRepository shopOwnerRepository;
    private final ReceiverMapper receiverMapper;

    public List<ReceiverDto> getAllReceivers(UUID userId) {
        ShopOwner owner = shopOwnerRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(MessageConstants.USER_NOT_FOUND)
        );

        List<Receiver> receivers = receiverRepository.findReceiversByShopOwner(owner);
        return receiverMapper.mapToDtos(receivers);
    }

    public ReceiverDto addNewReceiver(ReceiverDto newReceiver, UUID userId) {
        ShopOwner owner = shopOwnerRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(MessageConstants.USER_NOT_FOUND)
        );
        Receiver receiver = receiverMapper.mapDtoToEntity(newReceiver, userId);
        receiverRepository.save(receiver);
        return receiverMapper.mapToDto(receiver);
    }

    public void deleteReceiverById(Long receiverId, UUID userId) {
        Receiver receiver = receiverRepository.findById(receiverId).orElseThrow(() -> new RuntimeException("Receiver not found"));
        if (receiver.getShopOwner().getId().equals(userId)) {
            receiverRepository.deleteById(receiverId);
        }
        else {
            throw new RuntimeException("Receiver and User are not matched!");
        }
    }

    public ReceiverDto getReceiverById(Long receiverId, UUID userId) {
        Receiver receiver = receiverRepository.findById(receiverId).orElseThrow(() -> new RuntimeException("Receiver not found"));
        if (receiver.getShopOwner().getId().equals(userId)) {
            return receiverMapper.mapToDto(receiver);
        }
        else {
            throw new RuntimeException("Receiver and User are not matched!");
        }
    }

//    public ResponseEntity<String> updateReceiverById(Long id, Receiver updatedReceiver) {
//        Optional<Receiver> receiver = receiverRepository.findById(id);
//        if (receiver.isPresent()) {
//            Receiver tmp = receiver.get();
//            tmp.setUsername(updatedReceiver.getUsername());
//            tmp.setPhone_number(updatedReceiver.getPhone_number());
//            tmp.setAddress(updatedReceiver.getAddress());
//            return new ResponseEntity<>("success", HttpStatus.OK);
//        }
//        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);


//    }
}

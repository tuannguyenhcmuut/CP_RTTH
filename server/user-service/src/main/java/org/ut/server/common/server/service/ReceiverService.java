package org.ut.server.common.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.ut.server.common.server.common.MessageConstants;
import org.ut.server.common.server.model.Receiver;
import org.ut.server.common.server.model.User;
import org.ut.server.common.server.dto.ReceiverDto;
import org.ut.server.common.server.mapper.ReceiverMapper;
import org.ut.server.common.server.repo.ReceiverRepository;
import org.ut.server.common.server.repo.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReceiverService {
    private final ReceiverRepository receiverRepository;
    private final UserRepository userRepository;
    private final ReceiverMapper receiverMapper;

    public List<ReceiverDto> getAllReceivers(UUID userId) {
        Optional<User> owner = userRepository.findById(userId);
        if (owner.isEmpty()) {
            throw new RuntimeException(MessageConstants.USER_NOT_FOUND);
        }

        List<Receiver> receivers = receiverRepository.findReceiversByUser(owner.get());
        return receiverMapper.mapToDtos(receivers);
    }

    public ReceiverDto addNewReceiver(ReceiverDto newReceiver, UUID userId) {
        Optional<User> owner = userRepository.findById(userId);
        if (owner.isEmpty()) {
            throw new RuntimeException(MessageConstants.USER_NOT_FOUND);
        }
        Receiver receiver = receiverMapper.mapDtoToEntity(newReceiver, userId);
        receiverRepository.save(receiver);
        return receiverMapper.mapToDto(receiver);
    }

    public void deleteReceiverById(Long receiverId, UUID userId) {
        Receiver receiver = receiverRepository.findById(receiverId).orElseThrow(() -> new RuntimeException("Receiver not found"));
        if (receiver.getUser().getId().equals(userId)) {
            receiverRepository.deleteById(receiverId);
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

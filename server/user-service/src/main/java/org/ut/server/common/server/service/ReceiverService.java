package org.ut.server.common.server.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.ut.server.common.server.model.Receiver;
import org.ut.server.common.server.repo.ReceiverRepository;

import java.util.List;

@Service
public class ReceiverService {
    private ReceiverRepository receiverRepository;

    public ReceiverService(ReceiverRepository receiverRepository) {
        this.receiverRepository = receiverRepository;
    }

    public ResponseEntity<List<Receiver>> getAllReceiver() {
        return new ResponseEntity<>(receiverRepository.findAll(), HttpStatus.OK) ;
    }

    public ResponseEntity<String> addNewReceiver(Receiver newPerson) {
        receiverRepository.save(newPerson);
        return new ResponseEntity<>("success", HttpStatus.CREATED);
    }

    public ResponseEntity<String> deleteReceiverById(Long id) {
        receiverRepository.deleteById(id);
        return new ResponseEntity<>("success", HttpStatus.OK);
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

package org.ut.server.common.server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.ut.server.common.server.model.Receiver;
import org.ut.server.common.server.service.ReceiverService;

import java.util.List;

@RestController
@RequestMapping("receiver")
public class ReceiverController {
    private final ReceiverService receiverService;

    public ReceiverController(ReceiverService receiverService) {
        this.receiverService = receiverService;
    }
    //Get list receiver of store
    @GetMapping("")
    public ResponseEntity<List<Receiver>> getReceiverByUserId() {
        return receiverService.getAllReceiver();
    }

    //add new receiver
    @PostMapping("/create")
    public ResponseEntity<String> addPerson(@RequestBody Receiver newPerson) {
        return receiverService.addNewReceiver(newPerson);
    }

    //delete receiver by id
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePersonById(@PathVariable Long id) {
        return  receiverService.deleteReceiverById(id);
    }

    //update receiver by id
//    @PutMapping("/{id}")
//    public ResponseEntity<String> updatePersonById(@PathVariable Long id, @RequestBody Receiver updatedReceiver) {
//        return receiverService.updateReceiverById(id, updatedReceiver);
//    }
}

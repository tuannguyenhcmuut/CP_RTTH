package org.ut.server.omsserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.ut.server.omsserver.common.MessageConstants;
import org.ut.server.omsserver.dto.ReceiverDto;
import org.ut.server.omsserver.exception.EmployeeManagementException;
import org.ut.server.omsserver.exception.UserNotFoundException;
import org.ut.server.omsserver.mapper.ReceiverMapper;
import org.ut.server.omsserver.model.EmployeeManagement;
import org.ut.server.omsserver.model.Receiver;
import org.ut.server.omsserver.model.ShopOwner;
import org.ut.server.omsserver.model.enums.EmployeeRequestStatus;
import org.ut.server.omsserver.repo.EmployeeManagementRepository;
import org.ut.server.omsserver.repo.ReceiverRepository;
import org.ut.server.omsserver.repo.ShopOwnerRepository;
import org.ut.server.omsserver.service.impl.NotificationService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReceiverService {
    private final ReceiverRepository receiverRepository;
    private final ShopOwnerRepository shopOwnerRepository;
    private final ReceiverMapper receiverMapper;
    private final EmployeeManagementRepository employeeManagementRepository;
    private final NotificationService notificationService;

    public List<ReceiverDto> getAllReceivers(UUID userId, Pageable pageable) {
        ShopOwner owner = shopOwnerRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(MessageConstants.USER_NOT_FOUND)
        );

        List<Receiver> receivers;
        if (pageable != null) {
            receivers = receiverRepository.findReceiversByShopOwner(owner, pageable);
        }
        else {
            receivers = receiverRepository.findReceiversByShopOwner(owner);
        }

        List<ReceiverDto> receiverDtos = receiverMapper.mapToDtos(receivers, null);
//        try {
//            List<ReceiverDto> ownerReceiverDtos = this.getOwnerReceivers(userId, pageable);
//            receiverDtos.addAll(ownerReceiverDtos);
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            log.error("Error when get owner receivers");
//        }
        return receiverDtos;
    }

    public ReceiverDto addNewReceiver(ReceiverDto newReceiver, UUID userId) {
        ShopOwner owner = shopOwnerRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(MessageConstants.USER_NOT_FOUND)
        );
        Receiver receiver = receiverMapper.mapDtoToEntity(newReceiver, userId);
        receiverRepository.save(receiver);
        return receiverMapper.mapToDto(receiver, null);
    }

    public ReceiverDto addNewReceiverForOwner(ReceiverDto receiverDto, UUID userId) {
        List<EmployeeManagement> emplMgnts = employeeManagementRepository.findEmployeeManagementsByEmployee_IdAndApprovalStatus(
                userId,
                EmployeeRequestStatus.ACCEPTED
        );
        if (emplMgnts.isEmpty()) {
            throw new EmployeeManagementException(MessageConstants.ERROR_USER_NOT_HAS_OWNER);
        }
        EmployeeManagement emplMgnt = emplMgnts.get(0);
        ShopOwner owner = emplMgnt.getManager();
        ShopOwner employee = emplMgnt.getEmployee();
        receiverDto.setOwnerId(owner.getId());
        Receiver receiver = receiverMapper.mapDtoToEntity(receiverDto, owner.getId());
        receiverRepository.save(receiver);
        notificationService.notifyOrderInfoToOwner(
                owner, employee, null,
                String.format(MessageConstants.EMPLOYEE_RECEIVER_CREATED_MESSAGE, employee.getEmail(), receiver.getName())
        );
        return receiverMapper.mapToDto(receiver, owner);
    }

    public void deleteReceiverById(Long receiverId, UUID userId) {
        Receiver receiver = receiverRepository.findById(receiverId).orElseThrow(() -> new RuntimeException(MessageConstants.RECEIVER_NOT_FOUND));
        if (receiver.getShopOwner().getId().equals(userId)) {
            receiverRepository.deleteById(receiverId);
        } else {
            throw new RuntimeException(MessageConstants.RECEIVER_AND_USER_NOT_MATCHED);
        }
    }

    public ReceiverDto getReceiverById(Long receiverId, UUID userId) {
        Receiver receiver = receiverRepository.findById(receiverId).orElseThrow(() -> new RuntimeException(MessageConstants.RECEIVER_NOT_FOUND));
        if (receiver.getShopOwner().getId().equals(userId)) {
            return receiverMapper.mapToDto(receiver, null);
        } else {
            throw new RuntimeException(MessageConstants.RECEIVER_AND_USER_NOT_MATCHED);
        }
    }

    public List<ReceiverDto> getOwnerReceivers(UUID userId, Pageable pageable) {
        List<EmployeeManagement> emplMgnts = employeeManagementRepository.findEmployeeManagementsByEmployee_IdAndApprovalStatus(userId, EmployeeRequestStatus.ACCEPTED);
        if (emplMgnts.isEmpty()) {
            throw new EmployeeManagementException(MessageConstants.ERROR_USER_NOT_HAS_OWNER);
        }

        EmployeeManagement emplMgnt = emplMgnts.get(0);
        ShopOwner owner = emplMgnt.getManager();
        // get all stores of user's owner
        List<Receiver> stores = receiverRepository.findReceiversByShopOwner(owner, pageable);
        return receiverMapper.mapToDtos(stores, owner);
    }

    public ReceiverDto updateReceiverById(Long receiverId, ReceiverDto updatedReceiver, UUID userId) {
        Receiver receiver = receiverRepository.findById(receiverId).orElseThrow(() -> new RuntimeException(MessageConstants.RECEIVER_NOT_FOUND));
        if (receiver.getShopOwner().getId().equals(userId)) {
            if (updatedReceiver.getName() != null) {
                receiver.setName(updatedReceiver.getName());
            }
            if (updatedReceiver.getPhoneNumber() != null) {
                receiver.setPhoneNumber(updatedReceiver.getPhoneNumber());
            }
            if (updatedReceiver.getAddress() != null) {
                receiver.setAddress(updatedReceiver.getAddress());
            }
            if (updatedReceiver.getDetailedAddress() != null) {
                receiver.setDetailedAddress(updatedReceiver.getDetailedAddress());
            }
            if (updatedReceiver.getNote() != null) {
                receiver.setNote(updatedReceiver.getNote());
            }
            if (updatedReceiver.getReceivedPlace() != null) {
                receiver.setReceivedPlace(updatedReceiver.getReceivedPlace());
            }
            if (updatedReceiver.getDeliveryTimeFrame() != null) {
                receiver.setDeliveryTimeFrame(updatedReceiver.getDeliveryTimeFrame());
            }
            if (updatedReceiver.getCallBeforeSend() != null) {
                receiver.setCallBeforeSend(updatedReceiver.getCallBeforeSend());
            }
            if (updatedReceiver.getReceiveAtPost() != null) {
                receiver.setReceiveAtPost(updatedReceiver.getReceiveAtPost());
            }
            receiverRepository.save(receiver);
            return receiverMapper.mapToDto(receiver, null);
        } else {
            throw new RuntimeException(MessageConstants.RECEIVER_AND_USER_NOT_MATCHED);
        }
    }

    public ReceiverDto updateOwnerReceiverById(Long receiverId, ReceiverDto updatedReceiver, UUID userId) {
        List<EmployeeManagement> emplMgnts = employeeManagementRepository.findEmployeeManagementsByEmployee_IdAndApprovalStatus(userId, EmployeeRequestStatus.ACCEPTED);
        if (emplMgnts.isEmpty()) {
            throw new EmployeeManagementException(MessageConstants.ERROR_USER_NOT_HAS_OWNER);
        }
        EmployeeManagement emplMgnt = emplMgnts.get(0);
        ShopOwner owner = emplMgnt.getManager();
        ShopOwner employee = emplMgnt.getEmployee();
        Receiver receiver = receiverRepository.findById(receiverId).orElseThrow(() -> new RuntimeException(MessageConstants.RECEIVER_NOT_FOUND));
        if (receiver.getShopOwner().getId().equals(owner.getId())) {
            if (updatedReceiver.getName() != null) {
                receiver.setName(updatedReceiver.getName());
            }
            if (updatedReceiver.getPhoneNumber() != null) {
                receiver.setPhoneNumber(updatedReceiver.getPhoneNumber());
            }
            if (updatedReceiver.getAddress() != null) {
                receiver.setAddress(updatedReceiver.getAddress());
            }
            if (updatedReceiver.getDetailedAddress() != null) {
                receiver.setDetailedAddress(updatedReceiver.getDetailedAddress());
            }
            if (updatedReceiver.getNote() != null) {
                receiver.setNote(updatedReceiver.getNote());
            }
            if (updatedReceiver.getReceivedPlace() != null) {
                receiver.setReceivedPlace(updatedReceiver.getReceivedPlace());
            }
            if (updatedReceiver.getDeliveryTimeFrame() != null) {
                receiver.setDeliveryTimeFrame(updatedReceiver.getDeliveryTimeFrame());
            }
            if (updatedReceiver.getCallBeforeSend() != null) {
                receiver.setCallBeforeSend(updatedReceiver.getCallBeforeSend());
            }
            if (updatedReceiver.getReceiveAtPost() != null) {
                receiver.setReceiveAtPost(updatedReceiver.getReceiveAtPost());
            }
            receiverRepository.save(receiver);
            // notify to owner
            notificationService.notifyOrderInfoToOwner(
                    owner, employee, null,
                    String.format(MessageConstants.EMPLOYEE_RECEIVER_UPDATED_MESSAGE, employee.getEmail(), receiver.getName())
            );
            return receiverMapper.mapToDto(receiver, owner);
        } else {
            throw new RuntimeException(MessageConstants.RECEIVER_AND_USER_NOT_MATCHED);
        }
    }

    // receiver added today
    public Long getTodayReceivers(UUID userId) {
        ShopOwner owner = shopOwnerRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(MessageConstants.USER_NOT_FOUND)
        );

        return receiverRepository.countTotalReceiverCreatedToday(userId);
    }



//    update receiver by id


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

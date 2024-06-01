package org.ut.server.omsserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.ut.server.omsserver.common.MessageConstants;
import org.ut.server.omsserver.dto.StoreDto;
import org.ut.server.omsserver.exception.EmployeeManagementException;
import org.ut.server.omsserver.exception.StoreNotFoundException;
import org.ut.server.omsserver.exception.UserNotFoundException;
import org.ut.server.omsserver.mapper.StoreMapper;
import org.ut.server.omsserver.model.EmployeeManagement;
import org.ut.server.omsserver.model.ShopOwner;
import org.ut.server.omsserver.model.Store;
import org.ut.server.omsserver.model.User;
import org.ut.server.omsserver.model.enums.EmployeeRequestStatus;
import org.ut.server.omsserver.repo.EmployeeManagementRepository;
import org.ut.server.omsserver.repo.ShopOwnerRepository;
import org.ut.server.omsserver.repo.StoreRepository;
import org.ut.server.omsserver.repo.UserRepository;
import org.ut.server.omsserver.service.impl.NotificationService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final StoreMapper storeMapper;
    private final EmployeeManagementRepository employeeManagementRepository;
    private final ShopOwnerRepository shopOwnerRepository;
    private final NotificationService notificationService;

    public List<StoreDto> getAllStores(UUID userId, Pageable pageable) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException(MessageConstants.USER_NOT_FOUND);
        }

        List<Store> stores;
        if (pageable != null) {
            stores = storeRepository.findStoresByShopOwner(user.get(), pageable);
        }
        else {
            stores = storeRepository.findStoresByShopOwner(user.get());
        }
        List<StoreDto> storeDtos = storeMapper.mapToDtos(stores, null);
//        get owner stores
//        try {
//            List<StoreDto> ownerStoreDtos = this.getOwnerStores(userId, pageable);
//            storeDtos.addAll(ownerStoreDtos);
//        }
//        catch (Exception e) {
//        }
        return storeDtos;
    }

    public StoreDto addNewStore(StoreDto newStore, UUID userId) {
        Optional<User> owner = userRepository.findById(userId);
        if (owner.isEmpty()) {
            throw new UserNotFoundException(MessageConstants.USER_NOT_FOUND);
        }
        Store store = storeMapper.mapToEntity(newStore, owner.get().getId());
        storeRepository.save(store);
        return storeMapper.mapToDto(store, null);
    }

    public StoreDto addNewStoreForOwner(StoreDto storeDto, UUID userId) {
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
        storeDto.setOwnerId(owner.getId());
        Store store = storeMapper.mapToEntity(storeDto, owner.getId());
        storeRepository.save(store);
        notificationService.notifyOrderInfoToOwner(
                owner, employee, null,
                String.format(MessageConstants.EMPLOYEE_STORE_CREATED_MESSAGE, employee.getEmail(), store.getName())
        );
        return storeMapper.mapToDto(store, owner);
    }

    public void deleteStoreById(Long storeId, UUID userId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException(MessageConstants.STORE_NOT_FOUND));
        if (store.getShopOwner().getId().equals(userId)) {
            storeRepository.deleteById(storeId);
        }
        else {
            throw new RuntimeException(MessageConstants.STORE_AND_USER_NOT_MATCHED);
        }
    }

    public StoreDto getStoreById(UUID userId, Long storeId) {
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new StoreNotFoundException(MessageConstants.STORE_NOT_FOUND));
        if (store.getShopOwner().getId().equals(userId)) {
            return storeMapper.mapToDto(store, null);
        }
        else {
            throw new RuntimeException(MessageConstants.STORE_AND_USER_NOT_MATCHED);
        }
    }

    public List<StoreDto> getOwnerStores(UUID userId, Pageable pageable) {
        // TODO Auto-generated method stub
        // get its owner in employee table
        List<EmployeeManagement> emplMgnts= employeeManagementRepository.findEmployeeManagementsByEmployee_IdAndApprovalStatus(userId, EmployeeRequestStatus.ACCEPTED);
        if (emplMgnts.isEmpty()) {
            throw new EmployeeManagementException(MessageConstants.ERROR_USER_NOT_HAS_OWNER);
        }
        EmployeeManagement emplMgnt = emplMgnts.get(0);
        // TODO: check employee permission that has get or not
        ShopOwner owner = emplMgnt.getManager();
        // get all stores of user's owner
        List<Store> stores;
        if (pageable != null) {
            stores = storeRepository.findStoresByShopOwner(owner, pageable);
        }
        else {
            stores = storeRepository.findStoresByShopOwner(owner);
        }
        return storeMapper.mapToDtos(stores, owner);
    }


    public StoreDto updateStoreById(Long storeId, StoreDto updatedStore, UUID userId) {
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new StoreNotFoundException(MessageConstants.STORE_NOT_FOUND));
        if (store.getShopOwner().getId().equals(userId)) {
            // name
            if (updatedStore.getName() != null) {
                store.setName(updatedStore.getName());
            }
            // address
            if (updatedStore.getAddress() != null) {
                store.setAddress(updatedStore.getAddress());
            }
            // phone
            if (updatedStore.getPhoneNumber() != null) {
                store.setPhoneNumber(updatedStore.getPhoneNumber());
            }
            //  detailedAddress
            if (updatedStore.getDetailedAddress() != null) {
                store.setDetailedAddress(updatedStore.getDetailedAddress());
            }
//            description
            if (updatedStore.getDescription() != null) {
                store.setDescription(updatedStore.getDescription());
            }
//            storePickUpTime
            if (updatedStore.getStorePickUpTime() != null) {
                store.setStorePickUpTime(updatedStore.getStorePickUpTime());
            }
//            isDefault
            if (updatedStore.getIsDefault() != null) {
                store.setIsDefault(updatedStore.getIsDefault());
            }
//            sendAtPost
            if (updatedStore.getSendAtPost() != null) {
                store.setSendAtPost(updatedStore.getSendAtPost());
            }
            return storeMapper.mapToDto(store, null);
        }
        else {
            throw new RuntimeException(MessageConstants.STORE_AND_USER_NOT_MATCHED);
        }
    }

    public StoreDto updateOwnerStoreById(Long storeId, StoreDto updatedStore, UUID userId) {
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
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new StoreNotFoundException(MessageConstants.STORE_NOT_FOUND));
        if (store.getShopOwner().getId().equals(owner.getId())) {
            // name
            if (updatedStore.getName() != null) {
                store.setName(updatedStore.getName());
            }
            // address
            if (updatedStore.getAddress() != null) {
                store.setAddress(updatedStore.getAddress());
            }
            // phone
            if (updatedStore.getPhoneNumber() != null) {
                store.setPhoneNumber(updatedStore.getPhoneNumber());
            }
            //  detailedAddress
            if (updatedStore.getDetailedAddress() != null) {
                store.setDetailedAddress(updatedStore.getDetailedAddress());
            }
//            description
            if (updatedStore.getDescription() != null) {
                store.setDescription(updatedStore.getDescription());
            }
//            storePickUpTime
            if (updatedStore.getStorePickUpTime() != null) {
                store.setStorePickUpTime(updatedStore.getStorePickUpTime());
            }
//            isDefault
            if (updatedStore.getIsDefault() != null) {
                store.setIsDefault(updatedStore.getIsDefault());
            }
//            sendAtPost
            if (updatedStore.getSendAtPost() != null) {
                store.setSendAtPost(updatedStore.getSendAtPost());
            }

            storeRepository.save(store);
            notificationService.notifyOrderInfoToOwner(
                    owner, employee, null,
                    String.format(MessageConstants.EMPLOYEE_STORE_UPDATED_MESSAGE, employee.getEmail(), store.getName())
            );
            return storeMapper.mapToDto(store, owner);
        }
        else {
            throw new RuntimeException(MessageConstants.STORE_AND_USER_NOT_MATCHED);
        }



    }

    // store added today
    public Long getTodayStores(UUID userId) {
        ShopOwner owner = shopOwnerRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(MessageConstants.USER_NOT_FOUND)
        );

        return storeRepository.countTotalStoreCreatedToday(userId);
    }



}

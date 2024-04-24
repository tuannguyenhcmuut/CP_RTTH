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
import org.ut.server.omsserver.repo.StoreRepository;
import org.ut.server.omsserver.repo.UserRepository;

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

    public List<StoreDto> getAllStores(UUID userId, Pageable pageable) {
        Optional<User> owner = userRepository.findById(userId);
        if (owner.isEmpty()) {
            throw new UserNotFoundException(MessageConstants.USER_NOT_FOUND);
        }

        List<Store> stores;
        if (pageable != null) {
            stores = storeRepository.findStoresByShopOwner(owner.get(), pageable);
        }
        else {
            stores = storeRepository.findStoresByShopOwner(owner.get());
        }
        List<StoreDto> storeDtos = storeMapper.mapToDtos(stores, null);
//        get owner stores
        try {
            List<StoreDto> ownerStoreDtos = this.getOwnerStores(userId, pageable);
            storeDtos.addAll(ownerStoreDtos);
        }
        catch (Exception e) {
        }
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

    public void deleteStoreById(Long storeId, UUID userId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException("Store not found"));
        if (store.getShopOwner().getId().equals(userId)) {
            storeRepository.deleteById(storeId);
        }
        else {
            throw new RuntimeException("Store and User are not matched!");
        }
    }

    public StoreDto getStoreById(UUID userId, Long storeId) {
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new StoreNotFoundException("Store not found"));
        if (store.getShopOwner().getId().equals(userId)) {
            return storeMapper.mapToDto(store, null);
        }
        else {
            throw new RuntimeException("Store and User are not matched!");
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
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new StoreNotFoundException("Store not found"));
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
            storeRepository.save(store);
            return storeMapper.mapToDto(store, null);
        }
        else {
            throw new RuntimeException("Store and User are not matched!");
        }
    }
}

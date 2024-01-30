package org.ut.server.common.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.ut.server.common.server.common.MessageConstants;
import org.ut.server.common.server.dto.StoreDto;
import org.ut.server.common.server.mapper.StoreMapper;
import org.ut.server.common.server.model.Store;
import org.ut.server.common.server.model.User;
import org.ut.server.common.server.repo.StoreRepository;
import org.ut.server.common.server.repo.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final StoreMapper storeMapper;

    public List<StoreDto> getAllStores(UUID userId) {
        Optional<User> owner = userRepository.findById(userId);
        if (owner.isEmpty()) {
            throw new RuntimeException(MessageConstants.USER_NOT_FOUND);
        }

        List<Store> stores = storeRepository.findStoresByUser(owner.get());
        return storeMapper.mapToDtos(stores);
    }

    public StoreDto addNewStore(StoreDto newStore, UUID userId) {
        Optional<User> owner = userRepository.findById(userId);
        if (owner.isEmpty()) {
            throw new RuntimeException(MessageConstants.USER_NOT_FOUND);
        }
        Store store = storeMapper.mapToEntity(newStore, owner.get());
        storeRepository.save(store);
        return storeMapper.mapToDto(store);
    }

    public void deleteStoreById(Long storeId, UUID userId) {
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new RuntimeException("Store not found"));
        if (store.getUser().getId().equals(userId)) {
            storeRepository.deleteById(storeId);
        }
        else {
            throw new RuntimeException("Store and User are not matched!");
        }
    }

    public StoreDto getStoreById(UUID userId, Long storeId) {
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new RuntimeException("Store not found"));
        if (store.getUser().getId().equals(userId)) {
            return storeMapper.mapToDto(store);
        }
        else {
            throw new RuntimeException("Store and User are not matched!");
        }
    }
}

package org.ut.server.omsserver.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ut.server.omsserver.dto.StoreDto;
import org.ut.server.omsserver.exception.UserNotFoundException;
import org.ut.server.omsserver.model.ShopOwner;
import org.ut.server.omsserver.model.Store;
import org.ut.server.omsserver.repo.ShopOwnerRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StoreMapper {


    @Autowired
    private ShopOwnerRepository shopOwnerRepository;

    public StoreDto mapToDto(Store store, ShopOwner owner) {
        
        if (store == null) {
            return null;
        }
        return StoreDto.builder()
                .storeId(store.getId())
                .name(store.getName())
                .phoneNumber(store.getPhoneNumber())
                .address(store.getAddress())
                .detailedAddress(store.getDetailedAddress())
                .description(store.getDescription())
                .storePickUpTime(store.getStorePickUpTime())
                .isDefault(store.getIsDefault())
                .sendAtPost(store.getSendAtPost())
                .ownerId(owner == null ? null : owner.getId())
                .ownerName(owner == null ? null : String.format("%s %s", owner.getFirstName(), owner.getLastName()))
                .build();
    }

    public Store mapToEntity(StoreDto storeDto, UUID userId) {
        // find user
        ShopOwner user = shopOwnerRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return Store.builder()
                .id(storeDto.getStoreId())
                .shopOwner(user)
                .name(storeDto.getName())
                .phoneNumber(storeDto.getPhoneNumber())
                .address(storeDto.getAddress())
                .detailedAddress(storeDto.getDetailedAddress())
                .description(storeDto.getDescription())
                .storePickUpTime(storeDto.getStorePickUpTime())
                .isDefault(storeDto.getIsDefault())
                .sendAtPost(storeDto.getSendAtPost())
                .build();
    }

    public List<StoreDto> mapToDtos(List<Store> stores, ShopOwner owner) {
        if (stores != null) {
            return stores.stream().map(
                    store -> mapToDto(store, owner)
            ).collect(Collectors.toList());
        }
        else {
            return null;
        }
    }

    public List<Store> mapToEntities(List<StoreDto> storeDtos, UUID userId) {
        if (storeDtos != null) {
            return storeDtos.stream().map(
                    storeDto -> mapToEntity(storeDto, userId)
            ).collect(Collectors.toList());
        }
        else {
            return null;
        }
    }
}

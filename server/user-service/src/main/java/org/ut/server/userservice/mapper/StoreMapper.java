package org.ut.server.userservice.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.ut.server.userservice.dto.StoreDto;
import org.ut.server.userservice.model.Store;
import org.ut.server.userservice.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StoreMapper {

    public StoreDto mapToDto(Store store) {
        return StoreDto.builder()
                .id(store.getId())
                .name(store.getName())
                .phoneNumber(store.getPhoneNumber())
                .address(store.getAddress())
                .detailedAddress(store.getDetailedAddress())
                .description(store.getDescription())
                .storePickUpTime(store.getStorePickUpTime())
                .isDefault(store.getIsDefault())
                .sendAtPost(store.getSendAtPost())
                .build();
    }

    public Store mapToEntity(StoreDto storeDto, User user) {

        return Store.builder()
                .id(storeDto.getId())
                .user(user)
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

    public List<StoreDto> mapToDtos(List<Store> stores) {
        if (stores != null) {
            return stores.stream().map(
                    store -> mapToDto(store)
            ).collect(Collectors.toList());
        }
        else {
            return null;
        }
    }

    public List<Store> mapToEntities(List<StoreDto> storeDtos, User user) {
        if (storeDtos != null) {
            return storeDtos.stream().map(
                    storeDto -> mapToEntity(storeDto, user)
            ).collect(Collectors.toList());
        }
        else {
            return null;
        }
    }
}

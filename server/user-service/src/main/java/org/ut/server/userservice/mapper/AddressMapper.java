package org.ut.server.userservice.mapper;

import org.springframework.stereotype.Component;
import org.ut.server.userservice.dto.AddressDto;
import org.ut.server.userservice.model.Address;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AddressMapper {
    public AddressDto mapEntityToDto(Address address) {
        if (address == null) {
            return null;
        }
        return AddressDto.builder()
                .id(address.getId())
                .homeNumber(address.getHomeNumber())
                .country(address.getCountry())
                .city(address.getCity())
                .district(address.getDistrict())
                .ward(address.getWard())
                .street(address.getStreet())
                .description(address.getDescription())
                .build();
    }
    public Address mapDtoToEntity(AddressDto addressDto) {
        if (addressDto == null) {
            return null;
        }
        return Address.builder()
                .id(addressDto.getId())
                .homeNumber(addressDto.getHomeNumber())
                .country(addressDto.getCountry())
                .city(addressDto.getCity())
                .district(addressDto.getDistrict())
                .ward(addressDto.getWard())
                .street(addressDto.getStreet())
                .description(addressDto.getDescription())
                .build();
    }

    public List<Address> mapDtosToEntities(List<AddressDto> addressDtos) {
        return addressDtos != null ? addressDtos.stream().map(this::mapDtoToEntity).collect(Collectors.toList()) : null;
    }

    public List<AddressDto> mapEntitiesToDtos(List<Address> addresses) {
        return addresses != null ? addresses.stream().map(this::mapEntityToDto).collect(Collectors.toList()) : null;
    }
}

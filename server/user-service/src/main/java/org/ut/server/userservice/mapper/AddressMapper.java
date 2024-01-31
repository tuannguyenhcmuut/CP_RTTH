package org.ut.server.userservice.mapper;

import org.springframework.stereotype.Component;
import org.ut.server.common.dtos.user.AddressDto;
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
                .city(address.getCity())
                .country(address.getCountry())
                .district(address.getDistrict())
                .street(address.getStreet())
                .ward(address.getWard())
                .build();
    }
    public Address mapDtoToEntity(AddressDto addressDto) {
        if (addressDto == null) {
            return null;
        }
        return Address.builder()
                .id(addressDto.getId())
                .city(addressDto.getCity())
                .country(addressDto.getCountry())
                .district(addressDto.getDistrict())
                .street(addressDto.getStreet())
                .ward(addressDto.getWard())
                .build();
    }

    public List<Address> mapDtosToEntities(List<AddressDto> addressDtos) {
        return addressDtos != null ? addressDtos.stream().map(this::mapDtoToEntity).collect(Collectors.toList()) : null;
    }

    public List<AddressDto> mapEntitiesToDtos(List<Address> addresses) {
        return addresses != null ? addresses.stream().map(this::mapEntityToDto).collect(Collectors.toList()) : null;
    }
}

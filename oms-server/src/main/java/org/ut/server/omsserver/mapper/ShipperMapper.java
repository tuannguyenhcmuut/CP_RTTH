package org.ut.server.omsserver.mapper;

import org.springframework.stereotype.Component;
import org.ut.server.omsserver.dto.ShipperDto;
import org.ut.server.omsserver.dto.request.RegisterDto;
import org.ut.server.omsserver.model.Account;
import org.ut.server.omsserver.model.Shipper;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ShipperMapper {
//    @Autowired
//    private AddressMapper addressMapper;
//    @Autowired
//    private AccountRepository accountRepository;


    public Shipper newShipper(Account account, RegisterDto registerDTO) {
        Shipper shipper = new Shipper();
        shipper.setEmail(registerDTO.getEmail());
        shipper.setAccount(account);
        shipper.setFirstName(registerDTO.getFirstName());
        shipper.setLastName(registerDTO.getLastName());
        shipper.setPhoneNumber(registerDTO.getPhoneNumber());
        return shipper;
    }

    public ShipperDto mapToDto(Shipper shipper) {
        return ShipperDto.builder()
                .id(shipper.getId())
                .name(String.format("%s %s", shipper.getFirstName(), shipper.getLastName()))
                .phoneNumber(shipper.getPhoneNumber())
                .address(shipper.getAddress())
                .gender(shipper.getGender())
                .avatarUrl(shipper.getAvatarUrl())
                .rating(shipper.getRating())
                .build();
    }

    // map to dtos
    public List<ShipperDto> mapToDtos(List<Shipper> shippers) {
        if (shippers != null) {
            return shippers.stream().map(
                    this::mapToDto
            ).collect(Collectors.toList());
        }
        return List.of();
    }
}

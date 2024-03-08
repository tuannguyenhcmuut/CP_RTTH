package org.ut.server.omsserver.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ut.server.omsserver.dto.request.RegisterDto;
import org.ut.server.omsserver.dto.request.UserRequestDTO;
import org.ut.server.omsserver.dto.response.UserResponseDTO;
import org.ut.server.omsserver.model.Account;
import org.ut.server.omsserver.model.ShopOwner;
import org.ut.server.omsserver.repo.AccountRepository;

@Component
public class ShopOwnerMapper {

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private AccountRepository accountRepository;

    public UserResponseDTO mapEntityToResponse(ShopOwner user) {
        if (user == null) {
            return null;
        }
        return UserResponseDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getAccount().getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .gender(user.getGender())
                .avatar(user.getAvatarUrl())
                .dateOfBirth(user.getDateOfBirth())
                .addresses(addressMapper.mapEntitiesToDtos(user.getAddresses()))  // todo: fix addresses
                .phoneNumber(user.getPhoneNumber())
                .build();

    }

    public ShopOwner mapRequestToUser(UserRequestDTO userRequestDTO) {
        if (userRequestDTO == null) {
            return null;
        }

        Account account = accountRepository.findAccountByUsername(userRequestDTO.getUsername()).get();
        ShopOwner user = new ShopOwner();
        user.setEmail(userRequestDTO.getEmail());
        user.setAccount(account);
        user.setFirstName(userRequestDTO.getFirstName());
        user.setLastName(userRequestDTO.getLastName());
        user.setPhoneNumber(userRequestDTO.getPhoneNumber());
        user.setGender(userRequestDTO.getGender());
        user.setDateOfBirth(userRequestDTO.getDateOfBirth());
        user.setAvatarUrl(userRequestDTO.getAvatar());
        user.setAddresses(addressMapper.mapDtosToEntities(userRequestDTO.getAddresses()));

        return user;
    }

    // create new ShopOwner in
    public ShopOwner newShopOwner(Account account, RegisterDto registerDTO) {
        ShopOwner user = new ShopOwner();
        user.setEmail(registerDTO.getEmail());
        user.setAccount(account);
        user.setFirstName(registerDTO.getFirstName());
        user.setLastName(registerDTO.getLastName());
        user.setPhoneNumber(registerDTO.getPhoneNumber());

        return user;
    }
}

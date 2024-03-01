package org.ut.server.userservice.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ut.server.userservice.dto.request.RegisterDto;
import org.ut.server.userservice.model.Account;
import org.ut.server.userservice.model.Shipper;
import org.ut.server.userservice.model.ShopOwner;
import org.ut.server.userservice.repo.AccountRepository;

@Component
public class ShipperMapper {
    @Autowired
    private AddressMapper addressMapper;
    @Autowired
    private AccountRepository accountRepository;


    public Shipper newShipper(Account account, RegisterDto registerDTO) {
        Shipper shipper = new Shipper();
        shipper.setEmail(registerDTO.getEmail());
        shipper.setAccount(account);
        shipper.setFirstName(registerDTO.getFirstName());
        shipper.setLastName(registerDTO.getLastName());
        shipper.setPhoneNumber(registerDTO.getPhoneNumber());
        return shipper;
    }

}

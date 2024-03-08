package org.ut.server.omsserver.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ut.server.omsserver.dto.request.RegisterDto;
import org.ut.server.omsserver.model.Account;
import org.ut.server.omsserver.model.Shipper;
import org.ut.server.omsserver.repo.AccountRepository;

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

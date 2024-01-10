package org.ut.server.common.server.dto;

import lombok.Builder;
import lombok.Data;
import org.ut.server.common.server.enums.Gender;
import org.ut.server.common.server.model.Address;
import org.ut.server.common.server.model.Receiver;
import org.ut.server.common.server.model.Store;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class ShopOwnerDto {
    private String email;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Gender gender;
    private LocalDate dateOfBirth;
    private List<Address> addresses;
    private List<Receiver> receivers;
    private List<Store> stores;
}

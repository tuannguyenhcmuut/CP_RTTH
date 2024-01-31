package org.ut.server.userservice.dto;

import lombok.Builder;
import lombok.Data;
import org.ut.server.common.dtos.user.Gender;
import org.ut.server.userservice.model.Address;
import org.ut.server.userservice.model.Receiver;
import org.ut.server.userservice.model.Store;

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

package org.ut.server.dto;

import lombok.Builder;
import lombok.Data;
import org.ut.server.model.Address;
import org.ut.server.model.Gender;
import org.ut.server.model.Receiver;
import org.ut.server.model.Store;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class UserResponseDTO {
    private UUID id;
    private String email;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Gender gender;
    private Date dateOfBirth;
    private List<Address> addresses;
    private List<Receiver> receivers;
    private List<Store> stores;
}

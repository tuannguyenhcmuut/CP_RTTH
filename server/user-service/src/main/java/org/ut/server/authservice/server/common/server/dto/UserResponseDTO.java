package org.ut.server.authservice.server.common.server.dto;

import lombok.Builder;
import lombok.Data;
import org.ut.server.authservice.server.common.server.model.Address;
import org.ut.server.authservice.server.common.server.enums.Gender;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class UserResponseDTO {
    private UUID id;
    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Gender gender;
    private String avatar;
    private LocalDate dateOfBirth;
    private List<Address> addresses;
}

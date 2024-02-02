package org.ut.server.common.dtos.user;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class UserRequestDTO {
    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Gender gender;
    private String avatar;
    private LocalDate dateOfBirth;
    private List<AddressDto> addresses;
}

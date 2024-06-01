package org.ut.server.omsserver.dto.request;

import lombok.Builder;
import lombok.Data;
import org.ut.server.omsserver.dto.AddressDto;
import org.ut.server.omsserver.model.enums.Gender;

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
    private  String avatar;
    private LocalDate dateOfBirth;
    private List<AddressDto> addresses;
}

package org.ut.server.userservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeeInfoDto {
    // phone
    // email
    // managerId
    // permissions
    private String phone;
    private String email;
    private String managerId;
    private String permissions;

}

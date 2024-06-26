package org.ut.server.omsserver.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class EmployeeInfoDto {
    // phone
    // email
    // managerId
    // permissions
    private UUID employeeId;
    private String name;
    private String phone;
    private String email;
    private UUID managerId;
    private Set<String> permissions;

}

package org.ut.server.omsserver.dto.request;

import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class EmployeeRequestDto {
    private String employeePhone;
    private String employeeEmail;
    private UUID managerId;
    private Set<String> permissions;
}

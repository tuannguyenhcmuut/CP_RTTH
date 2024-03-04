package org.ut.server.userservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import org.ut.server.userservice.model.enums.EmployeeRequestStatus;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class EmployeeManagementDto {
    @JsonIgnore
    private Long id;
    private UUID employeeId;
    @JsonIgnore
    private UUID managerId;
    private Set<String> permissions;
    // status
    private EmployeeRequestStatus  status;

}

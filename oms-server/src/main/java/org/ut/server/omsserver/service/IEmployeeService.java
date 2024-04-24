package org.ut.server.omsserver.service;

import org.springframework.data.domain.Pageable;
import org.ut.server.omsserver.dto.EmployeeInfoDto;
import org.ut.server.omsserver.dto.EmployeeManagementDto;
import org.ut.server.omsserver.dto.request.EmployeeRequestDto;
import org.ut.server.omsserver.model.enums.PermissionLevel;

import java.util.List;
import java.util.UUID;

public interface IEmployeeService {
    // request employee from manager (shop owner) to other user
    EmployeeManagementDto requestEmployee(EmployeeRequestDto employeeRequestDto);

    // get all permissions level
    List<PermissionLevel> getPermissions();

    List<EmployeeManagementDto> getEmployeeRequests(UUID employeeId, UUID ownerId, String status);

    List<EmployeeManagementDto> getOwnerRequests(UUID managerId, String status);

    void approveEmployeeRequest(UUID employeeId, Long requestId);

    void rejectEmployeeRequest(UUID employeeId, Long requestId);

    List<EmployeeManagementDto> getRequests(UUID managerId, UUID employeeId, String status, Boolean isEmployeeGetAll);

    List<PermissionLevel> getEmployeePermissions(UUID employeeId, Long emplMgntId);

//    List<EmployeeInfoDto> getAllEmployees(UUID managerId);
    List<EmployeeInfoDto> getAllEmployees(UUID managerId, Pageable pageable);

    List<PermissionLevel> getEmployeePermissionsByManager(UUID employeeId, UUID ownerId);

    // approve employee

    // reject employee

    // get roles of employee

    // get all employees

    // get all employees by manager

    // get all employees by employee
}

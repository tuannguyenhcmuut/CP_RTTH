package org.ut.server.userservice.service;

import org.ut.server.userservice.dto.EmployeeInfoDto;
import org.ut.server.userservice.dto.EmployeeManagementDto;
import org.ut.server.userservice.dto.request.EmployeeRequestDto;
import org.ut.server.userservice.model.enums.PermissionLevel;

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

    List<EmployeeManagementDto> getRequests(UUID managerId, UUID employeeId, String status);

    List<PermissionLevel> getEmployeePermissions(UUID employeeId, Long emplMgntId);

    List<EmployeeInfoDto> getAllEmployees(UUID managerId);

    List<PermissionLevel> getEmployeePermissionsByManager(UUID employeeId, UUID ownerId);

    // approve employee

    // reject employee

    // get roles of employee

    // get all employees

    // get all employees by manager

    // get all employees by employee
}

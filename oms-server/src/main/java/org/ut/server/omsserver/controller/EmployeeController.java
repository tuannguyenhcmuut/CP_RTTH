package org.ut.server.omsserver.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.ut.server.omsserver.common.MessageCode;
import org.ut.server.omsserver.common.MessageConstants;
import org.ut.server.omsserver.config.JwtUtils;
import org.ut.server.omsserver.dto.EmployeeInfoDto;
import org.ut.server.omsserver.dto.EmployeeManagementDto;
import org.ut.server.omsserver.dto.request.EmployeeRequestDto;
import org.ut.server.omsserver.dto.response.GenericResponseDTO;
import org.ut.server.omsserver.model.enums.PermissionLevel;
import org.ut.server.omsserver.service.IEmployeeService;
import org.ut.server.omsserver.utils.RestParamUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/empl-mngt")
@Slf4j
@RequiredArgsConstructor
public class EmployeeController {
    private final IEmployeeService employeeService;
    private final JwtUtils jwtUtils;
    // request employee from manager (shop owner) to other user
    @PostMapping("/request")
    public GenericResponseDTO<EmployeeManagementDto> requestEmployee(
            @RequestBody EmployeeRequestDto employeeRequestDto,
            @RequestHeader("Authorization") String token
    ) {
        // request employee from manager (shop owner) to other user
        UUID userId = UUID.fromString(jwtUtils.extractUserIdFromBearerToken(token));
        employeeRequestDto.setManagerId(userId);
        EmployeeManagementDto employeeManagementResponse = employeeService.requestEmployee(employeeRequestDto);
        return GenericResponseDTO.<EmployeeManagementDto>builder()
                .data(employeeManagementResponse)
                .code(MessageCode.SUCCESS.toString())
                .message(MessageConstants.SUCCESS_CREATE_EMPLOYEE_REQUEST)
                .timestamps(new Date())
                .build();
    }

    // get all permissions level
    @GetMapping("/permissions")
    public GenericResponseDTO<List<PermissionLevel>> getPermissions() {
        // get all permissions level
        return GenericResponseDTO.<List<PermissionLevel>>builder()
                .data(employeeService.getPermissions())
                .code(MessageCode.SUCCESS.toString())
                .message(MessageConstants.SUCCESS_GET_PERMISSIONS)
                .timestamps(new Date())
                .build();
    }

    // approve employee
    @PostMapping("/{requestId}/approve")
    @ResponseStatus(HttpStatus.OK)
    public String  approveEmployee(
            @RequestHeader("Authorization") String token,
            @PathVariable Long requestId
    ) {
        // employeeId
        UUID employeeId = UUID.fromString(jwtUtils.extractUserIdFromBearerToken(token));
        // approve employee
        employeeService.approveEmployeeRequest(employeeId, requestId);
        return MessageConstants.SUCCESS_APPROVE_EMPLOYEE_REQUEST;
    }

    // reject employee
    @PostMapping("/{requestId}/reject")
    @ResponseStatus(HttpStatus.OK)
    public String rejectEmployee(
            @PathVariable Long requestId,
            @RequestHeader("Authorization") String token
    ) {
        // reject employee
        UUID employeeId = UUID.fromString(jwtUtils.extractUserIdFromBearerToken(token));
        // approve employee
        employeeService.rejectEmployeeRequest(employeeId, requestId);
        return MessageConstants.SUCCESS_REJECT_EMPLOYEE_REQUEST;
    }

    // get all requests of a manager
    @GetMapping("/owner/get-all")
    public GenericResponseDTO<List<EmployeeManagementDto>> getRequests(
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "status", required = false) String status
    ) {
        // get all requests of a manager
        UUID managerId = UUID.fromString(jwtUtils.extractUserIdFromBearerToken(token));
        List<EmployeeManagementDto> ownerRequests = employeeService.getOwnerRequests(managerId, status);

        return GenericResponseDTO.<List<EmployeeManagementDto>>builder()
                .data(ownerRequests)
                .code(MessageCode.SUCCESS.toString())
                .message(MessageConstants.SUCCESS_GET_REQUESTS_OF_MANAGER)
                .timestamps(new Date())
                .build();
    }

    @GetMapping("owner/{employeeId}")
    public GenericResponseDTO<List<EmployeeManagementDto>> getOwnerRequests(
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "status", required = false) String status,
            @PathVariable(value = "employeeId", required = false) UUID employeeId
    ) {
        // get all request of a manager
        UUID managerId = UUID.fromString(jwtUtils.extractUserIdFromBearerToken(token));
        List<EmployeeManagementDto> ownerRequests =  employeeService.getRequests(managerId, employeeId, status, false);
        return GenericResponseDTO.<List<EmployeeManagementDto>>builder()
                .data(ownerRequests)
                .code(MessageCode.SUCCESS.toString())
                .message(MessageConstants.SUCCESS_GET_MANAGER_REQUEST)
                .timestamps(new Date())
                .build();
    }

    // get all request of an employee
    @GetMapping("/employee/get-all")
    public GenericResponseDTO<List<EmployeeManagementDto>> getEmployeeRequests(
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "status", required = false) String status
    ) {
        // get all request of an employee
        UUID employeeId = UUID.fromString(jwtUtils.extractUserIdFromBearerToken(token));
        List<EmployeeManagementDto> employeeRequests =  employeeService.getRequests(null, employeeId, status, true);

        return GenericResponseDTO.<List<EmployeeManagementDto>>builder()
                .data(employeeRequests)
                .code(MessageCode.SUCCESS.toString())
                .message(MessageConstants.SUCCESS_GET_REQUESTS_OF_EMPLOYEE)
                .timestamps(new Date())
                .build();
    }

    // get all request of employee by manager
    @GetMapping("employee/{ownerId}")
    public GenericResponseDTO<List<EmployeeManagementDto>> getEmployeeRequests(
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "status", required = false) String status,
            @PathVariable(value = "ownerId", required = false) UUID ownerId
    ) {
        // get all request of an employee
        UUID employeeId = UUID.fromString(jwtUtils.extractUserIdFromBearerToken(token));
        List<EmployeeManagementDto> employeeRequests =  employeeService.getEmployeeRequests(employeeId, ownerId, status);

        return GenericResponseDTO.<List<EmployeeManagementDto>>builder()
                .data(employeeRequests)
                .code(MessageCode.SUCCESS.toString())
                .message(MessageConstants.SUCCESS_GET_EMPLOYEE_REQUEST)
                .timestamps(new Date())
                .build();
    }

    // get all request of manager


    // get permission of an employee with the managerId
    @GetMapping("employee/{ownerId}/permissions")
    public GenericResponseDTO<List<PermissionLevel>> getEmployeePermissions(
            @RequestHeader("Authorization") String token,
            @PathVariable UUID ownerId
    ) {
        UUID employeeId = UUID.fromString(jwtUtils.extractUserIdFromBearerToken(token));
        List<PermissionLevel> permissions =  employeeService.getEmployeePermissionsByManager(employeeId, ownerId);
        return GenericResponseDTO.<List<PermissionLevel>>builder()
                .data(permissions)
                .code(MessageCode.SUCCESS.toString())
                .message(MessageConstants.SUCCESS_GET_EMPLOYEE_PERMISSIONS)
                .timestamps(new Date())
                .build();
    }

    @GetMapping("{empl_mgnt_id}/permissions")
    public GenericResponseDTO<List<PermissionLevel>> getEmployeePermissionsFromEMPL_MNGNT(
            @RequestHeader("Authorization") String token,
            @PathVariable Long empl_mgnt_id
    ) {
        UUID employeeId = UUID.fromString(jwtUtils.extractUserIdFromBearerToken(token));
        List<PermissionLevel> permissions =  employeeService.getEmployeePermissions(employeeId, empl_mgnt_id);
        return GenericResponseDTO.<List<PermissionLevel>>builder()
                .data(permissions)
                .code(MessageCode.SUCCESS.toString())
                .message(MessageConstants.SUCCESS_GET_EMPLOYEE_PERMISSIONS)
                .timestamps(new Date())
                .build();
    }


    // get pending requests
    @GetMapping("/employee/pending")
    public GenericResponseDTO<List<EmployeeManagementDto>> getPendingRequests(
            @RequestHeader("Authorization") String token
    ) {
        // get pending requests
        UUID userId = UUID.fromString(jwtUtils.extractUserIdFromBearerToken(token));
        return GenericResponseDTO.<List<EmployeeManagementDto>>builder()
                .data(employeeService.getEmployeeRequests(userId,null,"PENDING"))
                .code(MessageCode.SUCCESS.toString())
                .message(MessageConstants.SUCCESS_GET_PENDING_REQUESTS)
                .timestamps(new Date())
                .build();
    }



    @GetMapping("")
    public GenericResponseDTO<List<EmployeeInfoDto>> getAllEmployees(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "approvalStatus,asc") String[] sort
    ) {
        // get all employees
        UUID managerId = UUID.fromString(jwtUtils.extractUserIdFromBearerToken(token));
        Pageable pageable = RestParamUtils.getPageable(page, size, sort);
        List<EmployeeInfoDto> employees =  employeeService.getAllEmployees(managerId, pageable);
        return GenericResponseDTO.<List<EmployeeInfoDto>>builder()
                .data(employees)
                .code(MessageCode.SUCCESS.toString())
                .message(MessageConstants.SUCCESS_GET_EMPLOYEES)
                .timestamps(new Date())
                .build();
    }

}

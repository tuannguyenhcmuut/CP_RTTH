package org.ut.server.omsserver.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.ut.server.omsserver.dto.EmployeeInfoDto;
import org.ut.server.omsserver.dto.EmployeeManagementDto;
import org.ut.server.omsserver.dto.request.EmployeeRequestDto;
import org.ut.server.omsserver.mapper.EmployeeManagementMapper;
import org.ut.server.omsserver.model.EmployeeManagement;
import org.ut.server.omsserver.model.Role;
import org.ut.server.omsserver.model.ShopOwner;
import org.ut.server.omsserver.model.enums.ERole;
import org.ut.server.omsserver.model.enums.EmployeeRequestStatus;
import org.ut.server.omsserver.model.enums.PermissionLevel;
import org.ut.server.omsserver.repo.EmployeeManagementRepository;
import org.ut.server.omsserver.repo.RoleRepository;
import org.ut.server.omsserver.repo.ShopOwnerRepository;
import org.ut.server.omsserver.service.IEmployeeService;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmployeeServiceImpl implements IEmployeeService {
    private final EmployeeManagementRepository employeeManagementRepository;
    private final ShopOwnerRepository shopOwnerRepository;
    private final EmployeeManagementMapper employeeManagementMapper;
    private final RoleRepository roleRepository;
    private final NotificationService notificationService;
    private static Set<PermissionLevel> mapPermissionLevelEnum(Set<String> permissionStr) {
        // convert list of permission level from employeeRequestDto to set of string
        return permissionStr.stream().map(
                PermissionLevel::valueOf
        ).collect(Collectors.toSet());
    }
    @Override
    public EmployeeManagementDto requestEmployee(EmployeeRequestDto employeeRequestDto) {
        // find employee by phone
        ShopOwner employee;
        if (employeeRequestDto.getEmployeePhone() != null) {
            // find employee by phone
            employee = shopOwnerRepository.findShopOwnerByPhoneNumber(employeeRequestDto.getEmployeePhone())
                    .orElseThrow(
                            () -> new RuntimeException("Employee not found with phone number " + employeeRequestDto.getEmployeePhone())
                    );
        }
        else if (employeeRequestDto.getEmployeeEmail() != null) {
            // find employee by email
            employee = shopOwnerRepository.findShopOwnerByEmail(employeeRequestDto.getEmployeeEmail())
                    .orElseThrow(
                            () -> new RuntimeException("Employee not found with email number " + employeeRequestDto.getEmployeeEmail())
                    );
        }
        else {
            // throw exception
            throw new RuntimeException("Employee not found with phone number or email");
        }
        // find manager by id
        ShopOwner manager = shopOwnerRepository.findShopOwnerById(employeeRequestDto.getManagerId())
                .orElseThrow(
                        () -> new RuntimeException("Manager not found with id " + employeeRequestDto.getManagerId())
                );

        // check if the employee and manager is already managed to each other
         Optional<EmployeeManagement> employeeManagementDB = employeeManagementRepository.findEmployeeManagementByManagerIdAndEmployeeId(manager, employee);

         if (employeeManagementDB.isPresent()) {
             throw new RuntimeException("Employee and manager are already managed to each other");
         }

        // Check if employee already has a manager
        Optional<EmployeeManagement> employeeManagementOptional = employeeManagementRepository.findEmployeeManagementByEmployeeId(employee);
        if (employeeManagementOptional.isPresent()) {
            throw new RuntimeException("Employee already has a manager");
        }

        // build employee management entity with pending status
        EmployeeManagement employeeManagement = EmployeeManagement.builder()
                .employeeId(employee)
                .managerId(manager)
                .permissionLevel(mapPermissionLevelEnum(employeeRequestDto.getPermissions()))
                .approvalStatus(EmployeeRequestStatus.PENDING)
                .build();
        EmployeeManagement savedEmployeeManagement = employeeManagementRepository.save(employeeManagement);
        // save the  request to EmployeeManagement Entity
        log.debug("Employee request saved with id {}", savedEmployeeManagement.getId());
//        savedEmployeeManagement.setManagerId(null);
        notificationService.requestEmployee(employee.getId(), manager.getId());
        return employeeManagementMapper.mapToDto(savedEmployeeManagement);
    }

    @Override
    public List<EmployeeManagementDto> getOwnerRequests(UUID managerId, String status) {
//        check null of status
        if (status == null) {
            // find all employee requests by manager id
            List<EmployeeManagement> employeeManagements = employeeManagementRepository.findEmployeeManagementsByManagerId_Id(managerId);
            // Convert the EmployeeManagement entities to EmployeeManagementDto objects and return the list
            return employeeManagements.stream()
                    .map(employeeManagementMapper::mapToDto)
                    .collect(Collectors.toList());
        }
        // Convert the status string to an EmployeeRequestStatus enum
        EmployeeRequestStatus requestStatus = EmployeeRequestStatus.valueOf(status.toUpperCase());
        // find all employee requests by manager id and status
        List<EmployeeManagement> employeeManagements = employeeManagementRepository.findEmployeeManagementsByManagerId_IdAndApprovalStatus(managerId, requestStatus);

        // Convert the EmployeeManagement entities to EmployeeManagementDto objects and return the list
        return employeeManagements.stream()
                .map(employeeManagementMapper::mapToDto)
                .collect(Collectors.toList());

    }

    @Override
    public void approveEmployeeRequest(UUID employeeId, Long requestId) {
        EmployeeManagement employeeManagementOptional = employeeManagementRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Employee request not found with id " + requestId));

        if (!employeeManagementOptional.getEmployeeId().getId().equals(employeeId)) {
            throw new RuntimeException("Employee ID mismatch");
        }
        // check if the current status is pending or not
        if (employeeManagementOptional.getApprovalStatus() != EmployeeRequestStatus.PENDING) {
            throw new RuntimeException("Employee request is not pending");
        }
        employeeManagementOptional.setApprovalStatus(EmployeeRequestStatus.ACCEPTED);

        // add role employee to employee
        ShopOwner employee = employeeManagementOptional.getEmployeeId();
        Set<Role> roles = employee.getAccount().getRoles();
        // find role with erole employee
        Optional<Role> employeeRole = roleRepository.findByName(ERole.ROLE_EMPLOYEE);

        //
        if (employeeRole.isEmpty()) {
            throw new RuntimeException("Employee role not found");
        }

        roles.add(employeeRole.get());
        employee.getAccount().setRoles(roles);
//        update new role
        ShopOwner newEmployee = shopOwnerRepository.save(employee);
        employeeManagementRepository.save(employeeManagementOptional);
        // Assuming there's a method to send notifications
//        sendNotificationToManager(employeeManagementOptional.getManagerId().getId(), "Your employee request has been approved.");
        notificationService.approveEmployeeRequest(employeeId, requestId);
    }

    @Override
    public void rejectEmployeeRequest(UUID employeeId, Long requestId) {
        EmployeeManagement employeeManagementOptional = employeeManagementRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Employee request not found with id " + requestId));

        if (!employeeManagementOptional.getEmployeeId().getId().equals(employeeId)) {
            throw new RuntimeException("Employee ID mismatch");
        }
        // check if the current status is pending or not
        if (employeeManagementOptional.getApprovalStatus() != EmployeeRequestStatus.PENDING) {
            throw new RuntimeException("Employee request is not pending");
        }
        employeeManagementOptional.setApprovalStatus(EmployeeRequestStatus.REJECTED);
        employeeManagementRepository.save(employeeManagementOptional);
        // Assuming there's a method to send notifications
//        sendNotificationToManager(employeeManagementOptional.getManagerId().getId(), "Your employee request has been approved.");
        notificationService.rejectEmployeeRequest(employeeId, requestId);
    }

    @Override
    public List<EmployeeManagementDto> getRequests(UUID managerId, UUID employeeId, String status) {
        // check if the managerId is null or not
        if (managerId != null) {
            // get all requests of a manager
            return getOwnerRequests(managerId, status);
        }
        else {
            // get all request of an employee
            return getEmployeeRequests(employeeId, null,  status);
        }
    }

    @Override
    public List<PermissionLevel> getEmployeePermissions(UUID employeeId, Long emplMgntId) {
        // find employee management by id
        EmployeeManagement employeeManagement = employeeManagementRepository.findEmployeeManagementByIdAndEmployeeId_Id(emplMgntId, employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id " + employeeId));
        // return the permission level of the employee
        return List.copyOf(employeeManagement.getPermissionLevel());
    }

    @Override
    public List<EmployeeInfoDto> getAllEmployees(UUID managerId) {
//        return null;
        // find all employee managements by manager id with status is accepted
        List<EmployeeManagement> employeeManagements = employeeManagementRepository.findEmployeeManagementsByManagerId_IdAndApprovalStatus(managerId, EmployeeRequestStatus.ACCEPTED);
        // Convert the EmployeeManagement entities to EmployeeInfoDto objects and return the list
        return employeeManagements.stream()
                .map(employeeManagementMapper::mapToEmployeeInfoDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeManagementDto> getEmployeeRequests(
            UUID employeeId,
            UUID ownerId,
            String status) {
//        return null;
        // find all employee managements by employee id with status is pending
        // check if the status is null or not
        if (ownerId != null && status != null) {
            List<EmployeeManagement> employeeManagements = employeeManagementRepository.findEmployeeManagementsByEmployeeId_IdAndManagerId_IdAndApprovalStatus(employeeId, ownerId, EmployeeRequestStatus.valueOf(status.toUpperCase()));
            // Convert the EmployeeManagement entities to EmployeeManagementDto objects and return the list
            return employeeManagements.stream()
                    .map(employeeManagementMapper::mapToDto)
                    .collect(Collectors.toList());

        }
        else if (ownerId != null) {
            List<EmployeeManagement> employeeManagements = employeeManagementRepository.findEmployeeManagementsByEmployeeId_IdAndManagerId_Id(employeeId, ownerId);
            // Convert the EmployeeManagement entities to EmployeeManagementDto objects and return the list
            return employeeManagements.stream()
                    .map(employeeManagementMapper::mapToDto)
                    .collect(Collectors.toList());
        }
        else if (status != null) {
            List<EmployeeManagement> employeeManagements = employeeManagementRepository.findEmployeeManagementsByEmployeeId_IdAndApprovalStatus(employeeId, EmployeeRequestStatus.valueOf(status.toUpperCase()));
            // Convert the EmployeeManagement entities to EmployeeManagementDto objects and return the list
            return employeeManagements.stream()
                    .map(employeeManagementMapper::mapToDto)
                    .collect(Collectors.toList());
        }
        else {
                // find all employee requests by employee id
                List<EmployeeManagement> employeeManagements = employeeManagementRepository.findEmployeeManagementsByEmployeeId_Id(employeeId);
                if (employeeManagements.isEmpty()) {
                    throw new RuntimeException("Employee not found with id " + employeeId);
                }
                // Convert the EmployeeManagement entities to EmployeeManagementDto objects and return the list
                return employeeManagements.stream()
                        .map(employeeManagementMapper::mapToDto)
                        .collect(Collectors.toList());
            }

    }

    @Override
    public List<PermissionLevel> getEmployeePermissionsByManager(UUID employeeId, UUID ownerId) {
        // find employee management by employee id and manager id
        // TODO: limit the request between 2 user is  unique
            EmployeeManagement employeeManagement = employeeManagementRepository.findEmployeeManagementByManagerIdAndEmployeeId(
                shopOwnerRepository.findShopOwnerById(ownerId).orElseThrow(() -> new RuntimeException("Manager not found with id " + ownerId)),
                shopOwnerRepository.findShopOwnerById(employeeId).orElseThrow(() -> new RuntimeException("Employee not found with id " + employeeId))
        ).orElseThrow(() -> new RuntimeException("Employee not found with id " + employeeId));
        // return the permission level of the employee
        return List.copyOf(employeeManagement.getPermissionLevel());
    }

    @Override
    public List<PermissionLevel> getPermissions() {
        // return all permission levels  value of PermissionLevel enum
        return List.of(PermissionLevel.values());
    }
}

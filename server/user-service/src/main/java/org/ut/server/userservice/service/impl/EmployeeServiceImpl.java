package org.ut.server.userservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.ut.server.userservice.dto.EmployeeInfoDto;
import org.ut.server.userservice.dto.EmployeeManagementDto;
import org.ut.server.userservice.dto.request.EmployeeRequestDto;
import org.ut.server.userservice.dto.response.GenericResponseDTO;
import org.ut.server.userservice.mapper.EmployeeManagementMapper;
import org.ut.server.userservice.model.EmployeeManagement;
import org.ut.server.userservice.model.ShopOwner;
import org.ut.server.userservice.model.enums.EmployeeRequestStatus;
import org.ut.server.userservice.model.enums.PermissionLevel;
import org.ut.server.userservice.repo.EmployeeManagementRepository;
import org.ut.server.userservice.repo.ShopOwnerRepository;
import org.ut.server.userservice.service.IEmployeeService;

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

//        TODO: check if the employee and manager is already managed to each other
//         employeeManagementRepository.findEmployeeManagementByManagerIdAndEmployeeId(manager, employee)
//                .ifPresent(
//                        employeeManagement -> {
//                            throw new RuntimeException("Employee is already a manager");
//                        }
//                );
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
        savedEmployeeManagement.setManagerId(null);
        return employeeManagementMapper.mapToDto(savedEmployeeManagement);
    }

    @Override
    public List<EmployeeManagementDto> getOwnerRequests(UUID userId, String status) {
        return null;
    }

    @Override
    public void approveEmployeeRequest(UUID employeeId, Long requestId) {

    }

    @Override
    public void rejectEmployeeRequest(UUID employeeId, Long requestId) {

    }

    @Override
    public List<EmployeeManagementDto> getRequests(UUID managerId, UUID employeeId, String status) {
        return null;
    }

    @Override
    public List<PermissionLevel> getEmployeePermissions(UUID employeeId, Long emplMgntId) {
        return null;
    }

    @Override
    public List<EmployeeInfoDto> getAllEmployees(UUID managerId) {
        return null;
    }

    @Override
    public List<EmployeeManagementDto> getPendingRequests(UUID employeeId) {
        return null;
    }


    @Override
    public List<PermissionLevel> getPermissions() {
        // return all permission levels  value of PermissionLevel enum
        return List.of(PermissionLevel.values());
    }
}

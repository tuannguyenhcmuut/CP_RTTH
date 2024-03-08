package org.ut.server.omsserver.mapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.ut.server.omsserver.dto.EmployeeInfoDto;
import org.ut.server.omsserver.dto.EmployeeManagementDto;
import org.ut.server.omsserver.model.EmployeeManagement;
import org.ut.server.omsserver.model.ShopOwner;
import org.ut.server.omsserver.model.enums.PermissionLevel;
import org.ut.server.omsserver.repo.ShopOwnerRepository;

import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class EmployeeManagementMapper {
//    mapDtoToEntity
    private final ShopOwnerRepository shopOwnerRepository;
    public EmployeeManagement mapDtoToEntity(EmployeeManagementDto employeeManagementDto) {
        // find employee by id
        ShopOwner employee = shopOwnerRepository.findShopOwnerById(employeeManagementDto.getEmployeeId())
                .orElseThrow(
                        () -> new RuntimeException("Employee not found with id " + employeeManagementDto.getEmployeeId())
                );

        // find manager by id
        ShopOwner manager = shopOwnerRepository.findShopOwnerById(employeeManagementDto.getManagerId())
                .orElseThrow(
                        () -> new RuntimeException("Manager not found with id " + employeeManagementDto.getManagerId())
                );

        return EmployeeManagement.builder()
                .id(employeeManagementDto.getId())
                .employeeId(employee)
                .managerId(manager)
                .permissionLevel(
                        employeeManagementDto.getPermissions().stream().map(
                                PermissionLevel::valueOf
                        ).collect(Collectors.toSet())
                )
                .build();
    }

//    mapToDto
    public EmployeeManagementDto mapToDto(EmployeeManagement employeeManagement) {

        return EmployeeManagementDto.builder()
                .id(employeeManagement.getId())
                .employeeId(employeeManagement.getEmployeeId().getId())
                .managerId(
                        employeeManagement.getManagerId() != null ? employeeManagement.getManagerId().getId() : null)
                .status(employeeManagement.getApprovalStatus())
                .permissions(
                        employeeManagement.getPermissionLevel().stream().map(
                                Enum::name
                        ).collect(Collectors.toSet())
                )
                .build();
    }

    public EmployeeInfoDto mapToEmployeeInfoDto(EmployeeManagement employeeManagement) {
        return EmployeeInfoDto.builder()
                .employeeId(employeeManagement.getEmployeeId().getId())
                .name( String.format("%s %s",
                        employeeManagement.getEmployeeId().getFirstName(),
                        employeeManagement.getEmployeeId().getLastName()
                        ))
                .phone(employeeManagement.getEmployeeId().getPhoneNumber())
                .email(employeeManagement.getEmployeeId().getEmail())
                .managerId(
                        employeeManagement.getManagerId() != null ? employeeManagement.getManagerId().getId() : null)
                .permissions(
                        employeeManagement.getPermissionLevel().stream().map(
                                Enum::name
                        ).collect(Collectors.toSet())
                )
                .build();
    }
}

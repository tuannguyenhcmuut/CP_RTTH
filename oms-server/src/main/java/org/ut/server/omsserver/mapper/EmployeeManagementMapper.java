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
                .employee(employee)
                .manager(manager)
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
                .employeeId(employeeManagement.getEmployee().getId())
                .managerId(
                        employeeManagement.getManager() != null ? employeeManagement.getManager().getId() : null)
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
                .employeeId(employeeManagement.getEmployee().getId())
                .name( String.format("%s %s",
                        employeeManagement.getEmployee().getFirstName(),
                        employeeManagement.getEmployee().getLastName()
                        ))
                .phone(employeeManagement.getEmployee().getPhoneNumber())
                .email(employeeManagement.getEmployee().getEmail())
                .managerId(
                        employeeManagement.getManager() != null ? employeeManagement.getManager().getId() : null)
                .permissions(
                        employeeManagement.getPermissionLevel().stream().map(
                                Enum::name
                        ).collect(Collectors.toSet())
                )
                .build();
    }
}

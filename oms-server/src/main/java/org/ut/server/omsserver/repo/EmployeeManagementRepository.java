package org.ut.server.omsserver.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ut.server.omsserver.model.EmployeeManagement;
import org.ut.server.omsserver.model.ShopOwner;
import org.ut.server.omsserver.model.enums.EmployeeRequestStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeManagementRepository extends JpaRepository<EmployeeManagement, Long> {
    // get employee request by id
    Optional<EmployeeManagement> findByEmployeeIdAndManagerId(ShopOwner employeeId, ShopOwner managerId);
    Optional<List<EmployeeManagement>> findEmployeeManagementsByManagerId_Id(UUID managerId);
    Optional<List<EmployeeManagement>> findEmployeeManagementsByEmployeeId_Id(UUID employeeId);
    Optional<List<EmployeeManagement>> findEmployeeManagementsByEmployeeId_IdAndApprovalStatus(UUID managerId, EmployeeRequestStatus requestStatus);
//    findEmployeeManagementsByEmployeeId_IdAndManagerId_IdAndApprovalStatus
    Optional<List<EmployeeManagement>> findEmployeeManagementsByEmployeeId_IdAndManagerId_IdAndApprovalStatus(UUID employeeId, UUID managerId, EmployeeRequestStatus requestStatus);
//    findEmployeeManagementsByEmployeeId_IdAndManagerId_Id
    Optional<List<EmployeeManagement>> findEmployeeManagementsByEmployeeId_IdAndManagerId_Id(UUID employeeId, UUID managerId);

    Optional<List<EmployeeManagement>> findEmployeeManagementsByManagerId(ShopOwner managerId);
    Optional<EmployeeManagement> findEmployeeManagementByIdAndEmployeeId(Long id, ShopOwner employeeId);
    Optional<EmployeeManagement> findEmployeeManagementByIdAndEmployeeId_Id(Long id, UUID employeeId);
//    findEmployeeManagementByManager_IdAndEmployee_Id
    Optional<EmployeeManagement> findEmployeeManagementByManagerIdAndEmployeeId(ShopOwner managerId, ShopOwner employeeId);

    List<EmployeeManagement> findEmployeeManagementsByManagerId_IdAndApprovalStatus(UUID managerId, EmployeeRequestStatus requestStatus);
}
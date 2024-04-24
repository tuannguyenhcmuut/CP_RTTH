package org.ut.server.omsserver.repo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.ut.server.omsserver.model.EmployeeManagement;
import org.ut.server.omsserver.model.ShopOwner;
import org.ut.server.omsserver.model.enums.EmployeeRequestStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeManagementRepository extends JpaRepository<EmployeeManagement, Long> {
    // get employee request by id
    Optional<EmployeeManagement> findByEmployeeAndManager(ShopOwner employeeId, ShopOwner managerId);
    Optional<EmployeeManagement> findByEmployee_IdAndManager_Id(UUID employeeId, UUID managerId);
    Optional<EmployeeManagement> findByEmployee_IdAndManager_IdAndApprovalStatus(UUID employeeId_id, UUID managerId_id, EmployeeRequestStatus approvalStatus);
    List<EmployeeManagement> findEmployeeManagementsByManager_Id(UUID managerId);
    List<EmployeeManagement> findEmployeeManagementsByEmployee_Id(UUID employeeId);
    List<EmployeeManagement> findEmployeeManagementsByEmployee_IdAndApprovalStatus(UUID employeeId, EmployeeRequestStatus requestStatus);
//    findEmployeeManagementsByEmployee_IdAndManager_IdAndApprovalStatus
    List<EmployeeManagement> findEmployeeManagementsByEmployee_IdAndManager_IdAndApprovalStatus(UUID employeeId, UUID managerId, EmployeeRequestStatus requestStatus);
//    findEmployeeManagementsByEmployee_IdAndManager_Id
    List<EmployeeManagement> findEmployeeManagementsByEmployee_IdAndManager_Id(UUID employeeId, UUID managerId);

    List<EmployeeManagement> findEmployeeManagementsByManager(ShopOwner managerId);
    Optional<EmployeeManagement> findEmployeeManagementByIdAndEmployee(Long id, ShopOwner employeeId);
    Optional<EmployeeManagement> findEmployeeManagementByIdAndEmployee_Id(Long id, UUID employeeId);
//    findEmployeeManagementByManager_IdAndEmployee_Id
    Optional<EmployeeManagement> findEmployeeManagementByManagerAndEmployee(ShopOwner manager, ShopOwner employee);

    List<EmployeeManagement> findEmployeeManagementsByManager_IdAndApprovalStatus(UUID managerId, EmployeeRequestStatus requestStatus);
    List<EmployeeManagement> findEmployeeManagementsByManager_IdAndApprovalStatus(UUID managerId, EmployeeRequestStatus requestStatus, Pageable pageable);
    Optional<EmployeeManagement> findEmployeeManagementByEmployee_Id(UUID employeeId);

    Optional<EmployeeManagement> findEmployeeManagementByEmployee(ShopOwner employee);
}

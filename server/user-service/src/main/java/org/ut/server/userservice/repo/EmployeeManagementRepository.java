package org.ut.server.userservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ut.server.userservice.model.EmployeeManagement;
import org.ut.server.userservice.model.ShopOwner;

import java.util.List;
import java.util.Optional;

public interface EmployeeManagementRepository extends JpaRepository<EmployeeManagement, Long> {
    // get employee request by id
    Optional<EmployeeManagement> findByEmployeeIdAndManagerId(ShopOwner employeeId, ShopOwner managerId);

    Optional<List<EmployeeManagement>> findEmployeeManagementsByManagerId(ShopOwner managerId);
    Optional<EmployeeManagement> findEmployeeManagementByIdAndEmployeeId(Long id, ShopOwner employeeId);
    // save employee request
    // update employee request
    // delete employee request
}

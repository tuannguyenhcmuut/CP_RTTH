package org.ut.server.common.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.ut.server.common.server.enums.EmployeeRequestStatus;
import org.ut.server.common.server.model.EmployeeManagement;
import org.ut.server.common.server.model.ShopOwner;
import org.ut.server.common.server.repo.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ShopOwnerService extends UserService {
    @Autowired
    private final ShopOwnerRepository shopOwnerRepository;

    @Autowired
    private final EmployeeManagementRepository employeeManagementRepository;




    public ShopOwnerService(UserRepository userRepository, AddressRepository addressRepository, ReceiverRepository receiverRepository, PasswordEncoder passwordEncoder, ShopOwnerRepository shopOwnerRepository, EmployeeManagementRepository employeeManagementRepository) {
        super(userRepository, addressRepository, receiverRepository, passwordEncoder);
        this.shopOwnerRepository = shopOwnerRepository;
        this.employeeManagementRepository = employeeManagementRepository;
    }

    // manage employee
    // get all employee
    public List<ShopOwner> getAllApprovalEmployees(UUID userId) {
        // get all employee request by userId
        // return list of employee request which status is accepted
        Optional<ShopOwner> userOpt = shopOwnerRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        Optional<List<EmployeeManagement>> employeeRequestsOpt = employeeManagementRepository
                .findEmployeeManagementsByManagerId(userOpt.get());
        if (employeeRequestsOpt.isEmpty()) {
            return null;
        }
        // get list of employee from employee request
        List<ShopOwner> employees = null;
        for (EmployeeManagement employeeRequest : employeeRequestsOpt.get()) {
            employees.add(employeeRequest.getEmployeeId());
        }
        // return list of employee
        return employees;
    }

    // add new employee
    public String requestEmployee(UUID userId, UUID newEmployeeID) {
        // check if newEmployeeID is already exist in shopOwner's employee list
        Optional<ShopOwner> employeeOpt = shopOwnerRepository.findById(newEmployeeID);
        if (employeeOpt.isEmpty()) {
            throw new RuntimeException("Employee not found");
        }
        Optional<ShopOwner> managerOpt = shopOwnerRepository.findById(userId);
        if (managerOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        Optional<EmployeeManagement> employeeRequestOpt = employeeManagementRepository
                .findByEmployeeIdAndManagerId(employeeOpt.get(), managerOpt.get());

        // if exist, throw exception
        if (employeeRequestOpt.isPresent()) {
            throw new RuntimeException("Employee request already exist");
        }
        // else,
        // add new employeeManagement request
        EmployeeManagement newEmployeeRequest = new EmployeeManagement();
            // find Employee
        Optional<ShopOwner> employee = shopOwnerRepository.findById(newEmployeeID);
        if (employee.isEmpty()) {
            throw new RuntimeException("Employee not found");
        }
        newEmployeeRequest.setEmployeeId(employee.get());
        newEmployeeRequest.setManagerId(managerOpt.get());
        newEmployeeRequest.setApprovalStatus(EmployeeRequestStatus.PENDING);
        employeeManagementRepository.save(newEmployeeRequest);
        //  TODO: send  employment request notification to employee,

        return "success request employee";
    }

    /* TODO: create controller with action accept or reject employee request
    * eg: /api/user/employment-request/accept
    * eg: /api/user/employment-request/reject
    *
    * */
    public void responseEmployeeRequest(Long requestId, UUID employeeID, Boolean isAccepted) {
        // find employee
        Optional<ShopOwner> employeeOpt = shopOwnerRepository.findById(employeeID);
        if (employeeOpt.isEmpty()) {
            throw new RuntimeException("Employee not found");
        }
        Optional<EmployeeManagement> employeeRequestOpt = employeeManagementRepository
                .findEmployeeManagementByIdAndEmployeeId(requestId, employeeOpt.get());

        // check if requestId is already exist in employee's request list
        // if not exist, throw exception
        if (employeeRequestOpt.isEmpty()) {
            throw new RuntimeException("Employee request not exist");
        }

        // check if status is pending state
        if (employeeRequestOpt.get().getApprovalStatus() != EmployeeRequestStatus.PENDING) {
            throw new RuntimeException("Employee request is not pending");
        }
        // else,
        //  if isAccepted == true
        //      change status to accepted
        //  else
        //      change status to rejected

        if (isAccepted) {
            employeeRequestOpt.get().setApprovalStatus(EmployeeRequestStatus.ACCEPTED);
        } else {
            employeeRequestOpt.get().setApprovalStatus(EmployeeRequestStatus.REJECTED);
        }
        employeeManagementRepository.save(employeeRequestOpt.get());
        // TODO: Send status notification to manager
    }
    // request employee

    // remove employee
    public void removeEmployee(UUID userId, UUID employeeId) {
        // check if employeeId is already exist in shopOwner's employee list
        Optional<ShopOwner> employeeOpt = shopOwnerRepository.findById(employeeId);
        if (employeeOpt.isEmpty()) {
            throw new RuntimeException("Employee not found");
        }
        Optional<ShopOwner> managerOpt = shopOwnerRepository.findById(userId);
        if (managerOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        Optional<EmployeeManagement> employeeRequestOpt = employeeManagementRepository
                .findByEmployeeIdAndManagerId(employeeOpt.get(), managerOpt.get());


        // if not exist, throw exception
        if (employeeRequestOpt.isEmpty()) {
            throw new RuntimeException("Employee request not exist");
        }
        // else,
        // remove employeeManagement request
        employeeManagementRepository.deleteById(employeeRequestOpt.get().getId());
    }


    // get all receivers
    // add receiver
    // remove receiver
    // update receiver





    // get all stores
    // add store
    // update store
    // remove store



}

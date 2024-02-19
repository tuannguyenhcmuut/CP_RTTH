package org.ut.server.userservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ut.server.userservice.model.enums.EmployeeRequestStatus;
import org.ut.server.userservice.model.enums.PermissionLevel;

import javax.persistence.*;

@Entity
@Table(name = "employee_management")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeManagement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private User employeeId;
//    private ShopOwner employeeId;
    @ManyToOne
    @JoinColumn(name = "manager_id", nullable = false)
    private User managerId;
//    private ShopOwner managerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_status", nullable = false)
    private EmployeeRequestStatus approvalStatus;
    @Enumerated(EnumType.STRING)
    @Column(name = "permission_level", nullable = false)
    private PermissionLevel permissionLevel;


}

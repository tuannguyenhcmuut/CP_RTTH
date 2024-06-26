package org.ut.server.omsserver.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ut.server.omsserver.model.enums.EmployeeRequestStatus;
import org.ut.server.omsserver.model.enums.PermissionLevel;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "employee_management")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeManagement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private ShopOwner employee;
    @ManyToOne
    @JoinColumn(name = "manager_id", nullable = false)
    private ShopOwner manager;

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_status", nullable = false)
    private EmployeeRequestStatus approvalStatus;
    @Enumerated(EnumType.STRING)
    @Column(name = "permission_level", nullable = false)
    @ElementCollection(targetClass = PermissionLevel.class, fetch = FetchType.EAGER)
    @JoinTable(name = "permission_level", joinColumns = @JoinColumn(name = "id"))
     private Set<PermissionLevel> permissionLevel;
}

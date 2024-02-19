package org.ut.server.userservice.model;

import lombok.Data;
import org.ut.server.userservice.model.enums.ERole;

import javax.persistence.*;

@Entity
@Table(name = "`role`")
@Data
public class Role {
    @Id
    @Column(name = "role_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERole name;
}

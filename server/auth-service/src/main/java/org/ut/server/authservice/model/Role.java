package org.ut.server.authservice.model;

import lombok.Data;

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

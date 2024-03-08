package org.ut.server.omsserver.model;

import lombok.Data;
import org.ut.server.omsserver.model.enums.ERole;

import javax.persistence.*;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return name == role.name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}

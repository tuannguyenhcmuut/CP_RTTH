package org.ut.server.authservice.server.common.server.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

//@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
//@Builder
//@PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id")
public class Store extends Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    public Long id;
    private Boolean isDefault;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Store(Long id, String username, String phoneNumber, String address,
                 User user, Boolean isDefault) {
        super(username, phoneNumber, address);
        this.id = id;
        this.user = user;
        this.isDefault = isDefault;
    }

}

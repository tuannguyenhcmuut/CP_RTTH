package org.ut.server.common.server.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.lang.Nullable;
import org.ut.server.common.dtos.user.Gender;
import org.ut.server.common.server.enums.Role;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name="`user`")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@MappedSuperclass
public class User {

    @Id
    @GeneratedValue
    @Type(type="org.hibernate.type.PostgresUUIDType")
    @Column(name = "user_id")
    private UUID id;
    @Column(nullable = false, length = 50, unique = true)
    private String email;
    @Column(nullable = false, length = 50, unique = true)
    private String username;
//    @Column(nullable = false)
//    private String password;
    @Column(nullable = false, length = 50)
    private String firstName;
    @Column(nullable = false, length = 50)
    private String lastName;
    @Column(nullable = false, length = 15, name = "phone", unique = true)
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private Gender gender;
//    @JsonFormat(pattern="dd-MM-yyyy") // date format: 20-10-2023
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Role role;

//    @Column(nullable = false, name = "register_at")
//    @CreatedDate
//    private LocalDateTime registerAt;

    private LocalDateTime lastLogin;

    private String avatar;

    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses;

    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Nullable
    private Set<Receiver> receivers;

    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Store> stores;
}

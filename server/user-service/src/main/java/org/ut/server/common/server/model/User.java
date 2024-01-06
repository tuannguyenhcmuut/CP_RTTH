package org.ut.server.common.server.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.ut.server.common.server.enums.Gender;
import org.ut.server.common.server.enums.Role;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="`user`")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
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
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, length = 50)
    private String firstName;
    @Column(nullable = false, length = 50)
    private String lastName;
    @Column(nullable = false, length = 15, name = "phone")
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private Gender gender;
//    @JsonFormat(pattern="dd-MM-yyyy") // date format: 20-10-2023
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false, name = "register_at")
    @CreatedDate
    private LocalDateTime registerAt;

    private LocalDateTime lastLogin;

    byte[] photo;

    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses;
}

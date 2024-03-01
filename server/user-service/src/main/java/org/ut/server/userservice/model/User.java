package org.ut.server.userservice.model;

import lombok.*;
import org.hibernate.annotations.Type;
import org.ut.server.userservice.model.enums.Gender;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="`user`")
@Data
//@Builder
@AllArgsConstructor
@NoArgsConstructor
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
//@SuperBuilder(toBuilder = true)
public class User {

    @Id
    @GeneratedValue
    @Type(type="org.hibernate.type.PostgresUUIDType")
    @Column(name = "user_id")
    private UUID id;
    @Column(nullable = false, length = 50, unique = true)
    private String email;
//    @Column(nullable = false, length = 20, unique = true)
//    private String username;
//    @Column(nullable = false, length = 50)
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "username", nullable=false, unique=true, referencedColumnName = "username")
//    private Account account;
    private String firstName;
    @Column(nullable = false, length = 50)
    private String lastName;
    @Column(nullable = false, length = 15, name = "phone", unique = true)
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private Gender gender;
//    @JsonFormat(pattern="dd-MM-yyyy") // date format: 20-10-2023
    private LocalDate dateOfBirth;

//    @Column(nullable = false, name = "register_at")
//    @CreatedDate
//    private LocalDateTime registerAt;

    private LocalDateTime lastLogin;

    private String avatarUrl;

//    @JsonManagedReference
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private List<Address> addresses;
//
//    @JsonManagedReference
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    @Nullable
//    private Set<Receiver> receivers;
//
//    @JsonManagedReference
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private Set<Store> stores;
//
//    public void setAddresses(List<Address> addresses) {
//        //1. remove the existing addresses that are not found in the new ones
//        this.addresses.retainAll(addresses);
//        //2. add the new addresses too, the common ones will be ignored by the Set semantics
//        for (Address address : addresses) {
//            address.setUser(this);
//        }
//        this.addresses.addAll(addresses);
//    }
}

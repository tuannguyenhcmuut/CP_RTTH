package org.ut.server.userservice.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "shop_owner")
@Data
@NoArgsConstructor
@AllArgsConstructor
//@MappedSuperclass
public class ShopOwner extends User {
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "username", nullable=false, unique=true, referencedColumnName = "username")
//    private Account account;
    @JsonManagedReference
    @OneToMany(mappedBy = "shopOwner", cascade = CascadeType.ALL)
    private List<Address> addresses;

    @JsonManagedReference
    @OneToMany(mappedBy = "shopOwner", cascade = CascadeType.ALL)
    @Nullable
    private Set<Receiver> receivers;

    @JsonManagedReference
    @OneToMany(mappedBy = "shopOwner", cascade = CascadeType.ALL)
    private Set<Store> stores;





    //    @Builder
//    public Child(String parentName, int parentAge, String childName, int childAge) {
//        super(parentName, parentAge);
//        this.childName = childName;
//        this.childAge = childAge;
//    }
    @Builder
//    public ShopOwner(UUID id, String email, Account account, String firstName, String lastName, String phoneNumber,
//                     Gender gender, LocalDate dateOfBirth, byte[] avatar, List<Address> addresses, Set<Receiver> receivers, Set<Store> stores) {
//        super(id, email, account,  firstName,lastName, phoneNumber, gender, dateOfBirth, LocalDateTime.now(), avatar);
//        this.addresses = addresses;
//        this.receivers = receivers;
//        this.stores = stores;
//    }

//    @Builder
//    public ShopOwner(UUID id, String email, Account account, String firstName, String lastName, String phoneNumber,
//                     Gender gender, LocalDate dateOfBirth, byte[] avatar, List<Address> addresses) {
//        super(id, email, account,  firstName,lastName, phoneNumber, gender, dateOfBirth, LocalDateTime.now(), avatar);
//        this.addresses = addresses;
//        this.receivers = null;
//        this.stores = null;
//    }
//
//    @Builder
//    public ShopOwner(String email, Account account, String firstName, String lastName, String phoneNumber,
//                     Gender gender, LocalDate dateOfBirth, byte[] avatar, List<Address> addresses) {
//        super(null, email, account,  firstName,lastName, phoneNumber, gender, dateOfBirth, LocalDateTime.now(), avatar);
//        this.addresses = addresses;
//        this.receivers = null;
//        this.stores = null;
//    }



    public void setAddresses(List<Address> addresses) {
        //1. remove the existing addresses that are not found in the new ones
        this.addresses.retainAll(addresses);
        //2. add the new addresses too, the common ones will be ignored by the Set semantics
        for (Address address : addresses) {
            address.setShopOwner(this);
        }
        this.addresses.addAll(addresses);
    }

//            @Override
//            public ShopOwner.ShopOwnerBuilder addresses(List<Address> addresses) {
//                super.addresses(addresses);
//                return this;
//            }
//        @Override
//        public ShopOwnerBuilder addresses(List<Address> addresses) {
//            super.(addresses);
//            return this;
//        }
//    }
}

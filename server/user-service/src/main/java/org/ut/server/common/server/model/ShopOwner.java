package org.ut.server.common.server.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ut.server.common.server.enums.Gender;
import org.ut.server.common.server.enums.Role;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

//@Entity
//@Table(name = "shop_owner")
//@Data
////@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//@PrimaryKeyJoinColumn(name = "user_id", referencedColumnName = "user_id")
public class ShopOwner extends User {
//    @JsonManagedReference
//    @OneToMany(mappedBy = "shopOwner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Receiver> receivers;

//    @JsonManagedReference
//    @OneToMany(mappedBy = "shopOwner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Store> stores;

}

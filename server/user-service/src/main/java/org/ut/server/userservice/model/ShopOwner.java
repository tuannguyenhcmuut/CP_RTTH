package org.ut.server.common.server.model;

import java.util.List;

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

package org.ut.server.userservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(	name = "account")
public class Account {
    @Id
    @NotBlank(message = "Username is mandatory")
    @Size(min = 3, max = 20)
    private String username;
    @NotBlank(message = "Password is mandatory")
    @Size(max = 120)
    private String password;

//    @OneToOne(mappedBy = "account", fetch=FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//    private ShopOwner user;
//
//    // shipper
//    @OneToOne(mappedBy = "account")
//    private Shipper shipper;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(	name = "user_roles",
            joinColumns = @JoinColumn(name = "username"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdDate;

//    public void setShipper(Shipper shipper) {
//        if (shipper == null) {
//            if (this.shipper != null) {
//                this.shipper.setAccount(null);
//            }
//        } else {
//            shipper.setAccount(this);
//        }
//        this.shipper = shipper;
//    }
//
//    public void setShopOwner(ShopOwner shopOwner) {
//        if (shopOwner == null) {
//            if (this.user != null) {
//                this.user.setAccount(null);
//            }
//        } else {
//            shopOwner.setAccount(this);
//        }
//        this.user = shopOwner;
//    }

}

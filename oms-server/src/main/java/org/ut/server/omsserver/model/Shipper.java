package org.ut.server.omsserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Table(name = "shipper")
@Data
//@Builder
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyJoinColumn(name = "user_id", referencedColumnName = "user_id")
public class Shipper extends User {
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "username", nullable=false, unique=true, referencedColumnName = "username")
//    private Account account;
    private String address;
    private Float rating;
//    public void setAccount(Account account) {
//        if (account == null) {
//            if (this.account != null) {
//                this.account.setShipper(null);
//            }
//        } else {
//            account.setShipper(this);
//        }
//        this.account = account;
//    }
}

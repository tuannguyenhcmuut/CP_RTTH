package org.ut.server.omsserver.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.ut.server.omsserver.model.enums.StorePickUpTime;

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
    private Long id;

    private String description;

    @Column(name = "store_pick_up_time")
    @Enumerated(EnumType.STRING)
    private StorePickUpTime storePickUpTime;
    @Column(name = "is_default")
    private Boolean isDefault;
    @Column(name = "send_at_post")
    private Boolean sendAtPost;
    @JsonBackReference
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private ShopOwner shopOwner;

//    @OneToOne(fetch = FetchType.LAZY, mappedBy = "store")
//    private Order order;

    @Builder
    public Store(Long id, String name, String phoneNumber, String address, String detailedAddress,
                 ShopOwner shopOwner, String description, StorePickUpTime storePickUpTime , Boolean isDefault, Boolean sendAtPost) {
        super(name, phoneNumber, address, detailedAddress);
        this.id = id;
        this.shopOwner = shopOwner;
        this.isDefault = isDefault;
        this.description = description;
        this.storePickUpTime = storePickUpTime;
        this.sendAtPost = sendAtPost;
    }

}

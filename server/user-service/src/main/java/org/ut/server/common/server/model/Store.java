package org.ut.server.common.server.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.ut.server.common.server.enums.StorePickUpTime;

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

    private String description;

    @Column(name = "store_pick_up_time")
    @Enumerated(EnumType.STRING)
    private StorePickUpTime storePickUpTime;
    @Column(name = "is_default")
    private Boolean isDefault;
    @Column(name = "send_at_post")
    private Boolean sendAtPost;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Builder
    public Store(Long id, String name, String phoneNumber, String address, String detailedAddress,
                 User user, String description, StorePickUpTime storePickUpTime ,Boolean isDefault, Boolean sendAtPost) {
        super(name, phoneNumber, address, detailedAddress);
        this.id = id;
        this.user = user;
        this.isDefault = isDefault;
        this.description = description;
        this.storePickUpTime = storePickUpTime;
        this.sendAtPost = sendAtPost;
    }

}

package org.ut.server.omsserver.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.ut.server.omsserver.model.enums.DeliveryTime;
import org.ut.server.omsserver.model.enums.LegitLevel;
import org.ut.server.omsserver.model.enums.ReceivedPlace;

import javax.persistence.*;


//@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
//@PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id")
public class  Receiver extends Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "receiver_id", nullable = false)
    private Long id;
    private String note;
    @Column(name = "received_place")
    @Enumerated(EnumType.STRING)
    private ReceivedPlace receivedPlace;

    @Column(name = "delivery_time_frame")
    @Enumerated(EnumType.STRING)
    private DeliveryTime deliveryTimeFrame;
    @Column(name = "call_before_send")
    private Boolean callBeforeSend;

    @Column(name = "receive_at_post")
    private Boolean receiveAtPost;
    @Column(name = "legit_point") // default is 0
    private Long legitPoint = 0L;
    @Column(name = "legit_level")
    @Enumerated(EnumType.STRING)
    private LegitLevel legitLevel = LegitLevel.NORMAL;
    @JsonBackReference
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private ShopOwner shopOwner;
//
//    @OneToOne(fetch = FetchType.LAZY, mappedBy = "receiver")
//    private Order order;
//    private ShopOwner shopOwner;

    @Builder
    public Receiver(Long id, String name, String phoneNumber, String address,
                    ShopOwner shopOwner, String detailedAddress, String note, ReceivedPlace receivedPlace,
                    DeliveryTime deliveryTimeFrame, Boolean callBeforeSend, Boolean receiveAtPost, Long legitPoint, LegitLevel legitLevel) {
        super(name, phoneNumber, address, detailedAddress);
        this.id = id;
        this.shopOwner = shopOwner;
        this.note = note;
        this.receivedPlace = receivedPlace;
        this.deliveryTimeFrame = deliveryTimeFrame;
        this.callBeforeSend = callBeforeSend;
        this.receiveAtPost = receiveAtPost;
        this.legitPoint = legitPoint;
        this.legitLevel = legitLevel;
    }
}

package org.ut.server.common.server.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.ut.server.common.server.enums.DeliveryTime;
import org.ut.server.common.server.enums.ReceivedPlace;

import javax.persistence.*;


//@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
//@PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id")
public class  Receiver extends Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
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

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
//    private ShopOwner shopOwner;

    @Builder
    public Receiver(Long id, String name, String phoneNumber, String address,
                    User user, String detailedAddress, String note, ReceivedPlace receivedPlace,
                    DeliveryTime deliveryTimeFrame, Boolean callBeforeSend) {
        super(name, phoneNumber, address, detailedAddress);
        this.id = id;
        this.user = user;
        this.note = note;
        this.receivedPlace = receivedPlace;
        this.deliveryTimeFrame = deliveryTimeFrame;
        this.callBeforeSend = callBeforeSend;
    }
}

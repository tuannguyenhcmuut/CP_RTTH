package org.ut.server.omsserver.model;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.ut.server.omsserver.model.enums.DeliveryTime;
import org.ut.server.omsserver.model.enums.OrderStatus;
import org.ut.server.omsserver.model.enums.ReceivedPlace;
import org.ut.server.omsserver.model.enums.StorePickUpTime;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "`order`")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@EntityListeners(OrderListener.class)
public class Order {

    @Id
//    @GeneratedValue
//    @Type(type="org.hibernate.type.PostgresUUIDType")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // order code
    @Column(name = "code",  insertable = true, updatable = true, nullable = true, unique = true)
    private String code;

    private Float height;
    private Float width;
    private Float length;


//    @Column(name = "user_id", nullable = false)
//    @Type(type="org.hibernate.type.PostgresUUIDType")
//    private UUID userId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private ShopOwner shopOwner;
//    @OneToOne
//    @JoinColumn(name = "store_id", nullable = false)
//    private Store store;
    @Column(name = "store_id", nullable = false)
    private Long storeId;

    @Column(name = "store_description")
    private String storeDescription;

    @Column(name = "store_pick_up_time")
    @Enumerated(EnumType.STRING)
    private StorePickUpTime storePickUpTime;
    @Column(name = "is_default")
    private Boolean isDefault;
    @Column(name = "send_at_post")
    private Boolean sendAtPost;
//
//    @OneToOne
//    @JoinColumn(name = "receiver_id", nullable = false)
//    private Receiver receiver;
    @Column(name = "receiver_id", nullable = false)
    private Long receiverId;
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

//    @OneToOne(fetch = FetchType.LAZY)
    @Column(name = "status_code", nullable = true)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    // join orderPrice
    @OneToOne( cascade={CascadeType.ALL}, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "price_id", nullable = true)
    private OrderPrice price;

//    @Column(name = "delivery_id", insertable = false, updatable = false)
    @OneToOne(mappedBy = "order", cascade=CascadeType.ALL, orphanRemoval = true)
    private Delivery delivery;

//    @OneToMany(mappedBy = "order")
////    @JoinColumn(name = /)
//    private List<OrderOption> orderOptions;

    @OneToOne(cascade=CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "discount_id", nullable=true)
    private Discount discount;

    @Column(name = "is_document")
    private Boolean isDocument; // tai lieu

    // options
    @Column(name = "is_bulky")
    private Boolean isBulky;
    @Column(name = "is_fragile")
    private Boolean isFragile;
    @Column(name = "is_valuable")
    private Boolean isValuable;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "last_updated_by")
    private String lastUpdatedBy;

    @Column(name = "last_updated_date")
    private LocalDateTime lastUpdatedDate;
    @OneToMany(mappedBy = "orderId",cascade=CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items;

//    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
//    private List<OrderHistory> orderHistories;

    // set order history
//    public void setStore(Store store) {
//        if (this.store != null) {
//            this.store.setOrder(null);
//        }
//
//        this.store = store;
//        store.setOrder(this);
//    }
//
//    // set receiver
//    public void setReceiver(Receiver receiver) {
//        if (this.receiver != null) {
//            this.receiver.setOrder(null);
//        }
//
//        this.receiver = receiver;
//        receiver.setOrder(this);
//    }

    public void setDelivery(Delivery delivery) {
        if (this.delivery != null) {
            this.delivery.setOrder(null);
        }

        this.delivery = delivery;
        delivery.setOrder(this);
    }


//    @Override
    public void setItems(List<OrderItem> items) {

        for (OrderItem item : items) {
            // initializing the TestObj instance in Children class (Owner side)
            // so that it is not a null and PK can be created
            item.setOrderId(this);
        }
        this.items = items;
    }

//    public void appendOrderHistory(OrderHistory orderHistory) {
//        orderHistory.setOrder(this);
//        this.orderHistories.add(orderHistory);
//    }

//    public void setOrderHistories(List<OrderHistory> items) {
//
//        for (OrderHistory item : items) {
//            // initializing the TestObj instance in Children class (Owner side)
//            // so that it is not a null and PK can be created
//            item.setOrder(this);
//        }
//        this.orderHistories = items;
//    }
}

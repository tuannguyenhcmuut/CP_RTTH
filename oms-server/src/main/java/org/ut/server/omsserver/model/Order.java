package org.ut.server.omsserver.model;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.ut.server.omsserver.model.enums.OrderStatus;

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

    @OneToMany(mappedBy = "orderId",cascade=CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items;

//    @Column(name = "user_id", nullable = false)
//    @Type(type="org.hibernate.type.PostgresUUIDType")
//    private UUID userId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private ShopOwner shopOwner;
    @OneToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @OneToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private Receiver receiver;

//    @OneToOne(fetch = FetchType.LAZY)
    @Column(name = "status_code", nullable = true)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    // join orderPrice
    @OneToOne( cascade=CascadeType.ALL)
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
}

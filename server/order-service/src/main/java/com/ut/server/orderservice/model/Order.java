package com.ut.server.orderservice.model;


import lombok.*;
import org.hibernate.annotations.Type;
import org.ut.server.authservice.server.common.events.OrderStatus;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

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
    @Column(name = "code",  insertable = false, updatable = false, nullable = true, unique = true)
    private String code;

    private Float height;
    private Float width;
    private Float depth;

    @OneToMany(mappedBy = "id", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items;

    @Column(name = "user_id", nullable = false)
    @Type(type="org.hibernate.type.PostgresUUIDType")
    private UUID userId;

    @Column(name = "store_id")
    private Long storeId;

    @Column(name = "receiver_id")
    private Long receiverId;

//    @OneToOne(fetch = FetchType.LAZY)
    @Column(name = "status_code", nullable = true)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    // join orderPrice
    @OneToOne( cascade=CascadeType.ALL)
    @JoinColumn(name = "price_id", nullable = true)
    private OrderPrice price;

    @Column(name = "ship_id", insertable = false, updatable = false)
    private Long shipId;

//    @OneToMany(mappedBy = "order")
////    @JoinColumn(name = /)
//    private List<OrderOption> orderOptions;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "discount_id", nullable=true)
    private Discount discount;

    // options
    @Column(name = "is_bulky")
    private Boolean isBulky;
    @Column(name = "is_fragile")
    private Boolean isFragile;
    @Column(name = "is_valuable")
    private Boolean isValuable;


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

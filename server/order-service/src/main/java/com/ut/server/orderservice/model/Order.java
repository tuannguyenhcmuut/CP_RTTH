package com.ut.server.orderservice.model;


import lombok.*;
import org.hibernate.annotations.Type;
import org.ut.server.common.events.OrderStatus;

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
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // order code
    @Column(name = "code",  insertable = false, updatable = false)
    private String code;

    private Float height;
    private Float width;
    private Float depth;

    @OneToMany(mappedBy = "id", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
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
    @OneToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
    @JoinColumn(name = "price_id", nullable = true)
    private OrderPrice price;

    @Column(name = "ship_id", insertable = false, updatable = false)
    private Long shipId;

    @OneToMany(mappedBy = "order")
//    @JoinColumn(name = /)
    private List<OrderOption> orderOptions;

    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "discount_id", nullable=true)
    private Discount discount;
}

package com.ut.server.orderservice.model;


import lombok.*;
import org.hibernate.annotations.Type;

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

    @Column(name = "user_id")
    @Type(type="org.hibernate.type.PostgresUUIDType")
    private UUID userId;

    @Column(name = "store_id")
    private Long storeId;

    @Column(name = "receiver_id")
    private Long receiverId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_code", nullable = true)
    private Status statusId;

    // join orderPrice
    @OneToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
    @JoinColumn(name = "price_id", nullable = true)
    private OrderPrice price;

    @Column(name = "discount_id")
    private Long discountId;

    @Column(name = "ship_id")
    private Long shipId;

    @OneToMany(mappedBy = "order")
    List<OrderOptions> orderOptions;

}

package org.ut.server.omsserver.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "order_price")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "collection_charge")// phí thu hộ
    private double collectionCharge;

    @Column(name = "total_items_price")
    private double itemsPrice;

    @Column(name = "shipping_fee")
    private double shippingFee;
}

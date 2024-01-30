package com.ut.server.orderservice.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "order_item")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "orderId")
@EqualsAndHashCode(exclude = "orderId")
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_item_id")
    private Long id;

    private int quantity;

    private double price;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order orderId;

    private Long productId; // goi den product service


    public OrderItem(int quantity, double price, Long productId, Order orderId) {
        this.quantity = quantity;
        this.price = price;
        this.productId = productId;
        this.orderId = orderId;
    }
}

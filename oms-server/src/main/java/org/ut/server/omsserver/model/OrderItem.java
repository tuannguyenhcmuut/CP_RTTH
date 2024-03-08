package org.ut.server.omsserver.model;

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

    @OneToOne
    private Product product; // goi den product service



    public OrderItem(int quantity, double price, Product product, Order orderId) {
        this.quantity = quantity;
        this.price = price;
        this.product = product;
        this.orderId = orderId;
    }
}

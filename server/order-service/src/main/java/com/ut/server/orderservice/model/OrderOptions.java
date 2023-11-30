package com.ut.server.orderservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "order_options")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderOptions {

    @EmbeddedId
    private OrderOptionsKey orderOptionsKey;

    // order
    @ManyToOne
    @MapsId("orderId")  // map vao ten attribute của OrderOptionsKey
    @JoinColumn(name = "order_id")
    private Order order;

    // OrderOptionType
    @ManyToOne
    @MapsId("typeId")
    @JoinColumn(name = "type_id")
    private OrderOptionType orderOptionType;

    @Column
    private Boolean value;  // giá trị của order option (true, false)

}

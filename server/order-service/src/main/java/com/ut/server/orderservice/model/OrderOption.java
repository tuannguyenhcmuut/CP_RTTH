package com.ut.server.orderservice.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "order_options")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "order")
@EqualsAndHashCode(exclude = "order")
public class OrderOption {

    @EmbeddedId
    private OrderOptionPK optionIds;

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
    private Boolean isChecked;  // giá trị của order option (true, false)

}

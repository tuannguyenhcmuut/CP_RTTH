package com.ut.server.orderservice.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "order_option_type")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderOptionType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "type_id")
    private Long id;

    @Column(columnDefinition = "varchar(255)")
    private String description;

    @OneToMany(mappedBy = "orderOptionType")
    List<OrderOption> orderOptions;

}

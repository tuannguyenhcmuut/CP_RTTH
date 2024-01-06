package com.ut.server.orderservice.model;

import lombok.*;

import javax.persistence.*;


@Entity
@Table(name = "discount_type")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscountType {
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private Float rate;
}

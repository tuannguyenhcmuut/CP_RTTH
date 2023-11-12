package com.ut.server.orderservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "product_line")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductLine {
    private Long product_id;
}

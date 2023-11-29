package com.ut.server.orderservice.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class OrderOptionsKey implements Serializable {

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "type_id")
    private Long typeId;
}

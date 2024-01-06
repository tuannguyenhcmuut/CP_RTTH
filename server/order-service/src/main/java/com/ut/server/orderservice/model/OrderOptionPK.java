package com.ut.server.orderservice.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Data
public class OrderOptionPK implements Serializable {

    @Column(name = "order_id")
    private UUID orderId;

    @Column(name = "type_id")
    private Long typeId;
}

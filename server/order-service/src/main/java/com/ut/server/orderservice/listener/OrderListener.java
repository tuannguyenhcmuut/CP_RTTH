package com.ut.server.orderservice.listener;

import com.ut.server.orderservice.model.Order;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.PrePersist;

@Slf4j
public class OrderListener {
    @PrePersist
    public void prePersist(Order order) {
        if (order.getCode() == null) {
            // Assuming the format is 'ON-' followed by the generated ID
            log.error("Order id in Listener: {}", order.getId());
            order.setCode("ON-" + order.getId());
        }
    }
}

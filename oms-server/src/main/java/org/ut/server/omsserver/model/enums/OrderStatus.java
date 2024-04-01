package org.ut.server.omsserver.model.enums;

import java.awt.*;

public enum OrderStatus {
    CREATED,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    CANCELLED;

    public static boolean contains(String test) {

        for (OrderStatus c : OrderStatus.values()) {
            if (c.name().equals(test)) {
                return true;
            }
        }

        return false;
    }
}

/**
 * ORDER_PROCESSING: representing that an order is being processed.
 * ORDER_PROBLEM: representing that there is a problem with the order.
 * ORDER_PICKUP_AVAILABLE: availability of an order for pickup.
 * ORDER_CANCELLED: representing cancellation of an order.
 * ORDER_DELIVERED: representing successful delivery of an order.
 * ORDER_IN_TRANSIT: an order is in transit.
 * ORDER_RETURNED: representing that an order has been returned.
* */
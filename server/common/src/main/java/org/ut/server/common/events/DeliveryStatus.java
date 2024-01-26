package org.ut.server.common.events;

public enum DeliveryStatus {
    PENDING, SHIPPED, DELIVERED, CANCELED
}
/**
  *  Shipped: This status means that the order has been shipped from the store and is on its way to the customer
  *  Delivered: This status means that the order has been delivered to the customer's address
  *  Canceled: This status means that the order has been canceled and will not be delivered to the customer.
  *  Pending: This status means that the order is still being processed and has not yet shipped from the warehouse.
*/
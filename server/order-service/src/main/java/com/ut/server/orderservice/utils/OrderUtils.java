package com.ut.server.orderservice.utils;

public class OrderUtils {

    public static String generateOrderCode(Long orderId) {
        // Logic to generate the order code based on your format
        // You can use timestamps, counters, random strings, etc. to create the code
        // For example:
        String prefix = "ON-" + String.valueOf(orderId);

        return prefix;
    }
}

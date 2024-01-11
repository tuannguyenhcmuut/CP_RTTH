package com.ut.server.orderservice.utils;

public class RandomGenUtils {
    public static int getRandomInt(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}

package org.ut.server.userservice.utils;

public class RandomGenUtils {
    public static int getRandomInt(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}

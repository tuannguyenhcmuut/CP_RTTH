package org.ut.server.omsserver.utils;

public class RandomGenUtils {
    private RandomGenUtils() {
    }
    public static int getRandomInt(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}

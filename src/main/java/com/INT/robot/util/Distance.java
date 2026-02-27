package com.INT.robot.util;

public class Distance {

    public static double calculateDistance(double x1, double y1, double x2, double y2) {
        double xDifference = x2 - x1;
        double yDifference = y2 - y1;

        return Math.hypot(xDifference, yDifference);
    }
}
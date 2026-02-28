package com.INT.robot.util;

import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;

public class HoodInterpolation {

    private static final InterpolatingDoubleTreeMap interpolatingDoubleTreeMap;

    private static final double[][] AngleandDistance = {
        {0, 0},
        {0, 0},
        {0, 0}
    }; //degrees hopefully

    static {
        interpolatingDoubleTreeMap = new InterpolatingDoubleTreeMap();
        for (double[] data : AngleandDistance) {
            interpolatingDoubleTreeMap.put(data[1], data[0]);
        }
    }

    public static double getAngle(double distanceInInches) {
        return interpolatingDoubleTreeMap.get(distanceInInches);
    }
}
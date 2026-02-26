package com.INT.robot.util;

import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;

public class ShootInterpolation {

    private static final InterpolatingDoubleTreeMap interpolatingDoubleTreeMap;

    private static final double[][] RPMandDistance = {
        {0, 0},
        {0, 0},
        {0, 0}
    };

    static {
        interpolatingDoubleTreeMap = new InterpolatingDoubleTreeMap();
        for (double[] data : RPMandDistance) {
            interpolatingDoubleTreeMap.put(data[1], data[0]);
        }
    }

    public static double getRPM(double distanceInInches) {
        return interpolatingDoubleTreeMap.get(distanceInInches);
    }
}
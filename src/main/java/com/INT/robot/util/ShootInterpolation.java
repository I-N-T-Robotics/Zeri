package com.INT.robot.util;

import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;

public class ShootInterpolation {

    private static final InterpolatingDoubleTreeMap interpolatingDoubleTreeMap;

    private static final double[][] RPMandDistance = {
        {1.22, 2700.0},
        {2.15, 2930.0},
        {3.38, 3200.0},
        {4.43, 3550.0},
        {5.66, 3900.0}
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
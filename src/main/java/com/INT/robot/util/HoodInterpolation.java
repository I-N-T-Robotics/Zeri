package com.INT.robot.util;

import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;
import edu.wpi.first.math.util.Units;

public class HoodInterpolation {

    private static final InterpolatingDoubleTreeMap interpolatingDoubleTreeMap;

    private static final double[][] AngleandDistance = {
        {1.22, Units.degreesToRadians(22.5)},
        {2.15, Units.degreesToRadians(27)},
        {3.38, Units.degreesToRadians(37)},
        {4.43, Units.degreesToRadians(39)},
        {5.66, Units.degreesToRadians(39)}
    };

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
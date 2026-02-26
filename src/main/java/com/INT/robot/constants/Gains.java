package com.INT.robot.constants;

import com.pathplanner.lib.config.PIDConstants;

public class Gains {

    public interface Swerve {
        public interface Drive {
            double kP = 0;
            double kI = 0;
            double kD = 0;

            double kS = 0;
            double kV = 0;
            double kA = 0;
        }

        public interface Turn {
            double kP = 0;
            double kI = 0;
            double kD = 0;

            double kS = 0;
            double kV = 0;
            double kA = 0;
        }

        public interface Alignment {
            public interface Rotation {  
                double kp = 112.3;
                double ki = 0.0;
                double kd = 2.3758;
                double ks = 0.31395;
                double kv = 0.10969;
                double ka = 0.026589;
            }

            double kP = 0.0;
            double kI = 0.0;
            double kD = 0.0;
            double akP = 0.0;
            double akI = 0.0;
            double akD = 0.0;

            PIDConstants XY = new PIDConstants(3.0, 0.0, 0.2);
            PIDConstants THETA = new PIDConstants(3.0, 0.0, 0.2);
        }
    }
}
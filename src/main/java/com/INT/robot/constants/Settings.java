package com.INT.robot.constants;

import com.ctre.phoenix6.CANBus;
import com.pathplanner.lib.path.PathConstraints;

import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.Vector;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.math.util.Units;

public interface Settings {
    public final CANBus CANIVORE = new CANBus("canivore", ".logs/example.hoot");

    public interface Swerve {
        public final double MODULE_VELOCITY_DEADBAND_M_PER_S = 0.1;
        public final double ROTATIONAL_DEADBAND_RAD_PER_S = 0.1;

        public interface Constraints {
            public final double MAX_VELOCITY_M_PER_S = 4.3;
            public final double MAX_ACCEL_M_PER_S_SQUARED = 15.0;
            public final double MAX_ANGULAR_VEL_RAD_PER_S = Units.degreesToRadians(400.0);
            public final double MAX_ANGULAR_ACCEL_RAD_PER_S = Units.degreesToRadians(900.0);

            public final PathConstraints DEFAULT_CONSTRAINTS =
                new PathConstraints(
                    MAX_VELOCITY_M_PER_S,
                    MAX_ACCEL_M_PER_S_SQUARED,
                    MAX_ANGULAR_VEL_RAD_PER_S,
                    MAX_ANGULAR_ACCEL_RAD_PER_S);
        }
    }

    public interface Turret {
        public interface Constants {
            public final Transform2d TURRET_OFFSET = new Transform2d(0, 0, new Rotation2d(0));
            public final double toleranceRadians = Units.degreesToRadians(5);
            //TODO: add constants
        }
    }

    public interface Vision {
        public final Vector<N3> MT1_STDDEVS = VecBuilder.fill(0.5, 0.5, 1.0);
        public final Vector<N3> MT2_STDDEVS = VecBuilder.fill(0.7, 0.7, 694694.0);
    }
}
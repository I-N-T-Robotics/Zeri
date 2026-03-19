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

    public static final String upper = "Upper";
    public static final String essentials = "Essentials";

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
           public final Transform2d TURRET_OFFSET = new Transform2d(5.65, 4.45, new Rotation2d(Math.toRadians(90))); //inches
            public final double toleranceRadians = Units.degreesToRadians(5);
            public final double TURRET_MIN_ROTATIONS = -0.375;
            public final double TURRET_MAX_ROTATIONS = 0.375;
            public final double GEAR_RATIO_MOTOR_TO_MECH = 136;
        }

        public interface SoftwareLimit {
            public final double FORWARD_MAX_ROTATIONS = 0.375;
            public final double BACKWARDS_MAX_ROTATIONS = 0.375;
        }

        public interface Encoder {
            //in rotations
            public final double ENCODER_ENCODER_OFFSET = 0.0;
            public final double ENCODER_TURRET_OFFSET = 0.0;
        }
    }

    public interface Vision {
        public final Vector<N3> MT1_STDDEVS = VecBuilder.fill(0.5, 0.5, 1.0);
        public final Vector<N3> MT2_STDDEVS = VecBuilder.fill(0.7, 0.7, 96429642.0);
    }

    public interface Shooter {
        public final double SHOOTER_RPM_TOLERANCE = 50;
        public final double GEAR_RATIO = 1;
    }

    public interface Intake {
        public final double DEPLOYED_POSITION = 90;
        public final double UP_POSITION = 0;
        public final double INTAKE_RPM = 5000;
        public final double OUTTAKE_RPM = -5000;
        public final double GEAR_RATIO = 1;

        public final double INTAKE_POSITION_TOLERANCE = 5;
    }

    public interface Spindexer {
        public final double SPINDEXER_RPM = 5000;
        public final double TRANSITION_TO_SPEED_RATIO = 1;
        public final double TRANSITION_MIN_SPEED = 4800;
        public final double GEAR_RATIO = 5; //motor to mech
    }//16 - 60 - 18 - 24T

    public interface Hood {
        public final double HOOD_TOLERANCE = 5;
        public final double HOOD_RESET_RPS = -0.2;
        public final double HOOD_STALL_CURRENT = 20;
        public final double GEAR_RATIO = 0.67;
        //15 - 30 - 16 - 66 - 10T
        //TODO: sensor to mech; not motor to mech

        //in rotations
        public final double FORWARD_SOFT_LIMIT = 0.5;
        public final double REVERSE_SOFT_LIMIT = 0.0;
        public final double ENCODER_OFFSET = 0;
    }

    public interface Transition {
        public final double GEAR_RATIO = 2;
    }
}
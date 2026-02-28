package com.INT.robot.constants;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;

public interface Motors {

    public static class TurretConstants {
        public static int TURRET_MOTOR = 0; //TODO: Fix
        public static int TURRET_ENCODER_TURRET = 0;
        public static int TURRET_ENCODER_ENCODER = 0;

        public static TalonFXConfiguration turretConfigs = new TalonFXConfiguration();
        public static CANcoderConfiguration turretCANcoderConfigs = new CANcoderConfiguration();
    }

    public static class ShooterConstants {
        public static int RIGHT_MOTOR = 0; //TODO: Fix
        public static int LEFT_MOTOR = 0;

        public static TalonFXConfiguration shooterRightMotorConfig = new TalonFXConfiguration();
        public static TalonFXConfiguration shooterLeftMotorConfig = new TalonFXConfiguration();
    }

    public static class IntakeConstants {
        public static int PIVOT = 0;
        public static int MOTOR1 = 0;
        public static int MOTOR2 = 0;

        public static TalonFXConfiguration intakePivotConfig = new TalonFXConfiguration();
        public static TalonFXConfiguration intakeMotor1Config = new TalonFXConfiguration();
        public static TalonFXConfiguration intakeMotor2Config = new TalonFXConfiguration();
    }

    public static class SpindexerConstants {
        public static int INTAKE_SPINDEXER_MOTOR = 0;
        public static int FAR_SPINDEXER_MOTOR = 0;

        public static TalonFXConfiguration intakeSpindexerMotorConfig = new TalonFXConfiguration();
        public static TalonFXConfiguration farSpindexerMotorConfig = new TalonFXConfiguration();

        public static int TRANSITION_MOTOR = 0;
        public static TalonFXConfiguration transitionMotorConfig = new TalonFXConfiguration();
    }

    public static class HoodConstants {
        public static int HOOD_MOTOR = 0;
        public static int HOOD_ENCODER = 0;

        public static TalonFXConfiguration hoodMotorConfigs = new TalonFXConfiguration();
        public static CANcoderConfiguration hoodEncoderConfigs = new CANcoderConfiguration();

        static {
            hoodMotorConfigs.Feedback.FeedbackRemoteSensorID = Motors.HoodConstants.HOOD_ENCODER;
            hoodMotorConfigs.Feedback.FeedbackSensorSource = FeedbackSensorSourceValue.FusedCANcoder;
            hoodMotorConfigs.Feedback.SensorToMechanismRatio = 1; //sensor to mech
            hoodMotorConfigs.Feedback.RotorToSensorRatio = 1; //motor to sensor
        }
    }
}
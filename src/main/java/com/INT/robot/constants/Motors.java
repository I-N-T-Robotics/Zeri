package com.INT.robot.constants;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfiguration;

public interface Motors {

    public static class TurretConstants {
        public static int TURRET_MOTOR = 0; //TODO: Fix
        public static int TURRET_ENCODER_TURRET = 0;
        public static int TURRET_ENCODER_ENCODER = 0;

        public static TalonFXConfiguration turretConfigs = new TalonFXConfiguration();
        public static CANcoderConfiguration turretCANcoderConfigs = new CANcoderConfiguration();
    }
}
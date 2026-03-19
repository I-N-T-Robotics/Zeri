package com.INT.robot.constants;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.ClosedLoopGeneralConfigs;
import com.ctre.phoenix6.configs.ClosedLoopRampsConfigs;
import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.FeedbackConfigs;
import com.ctre.phoenix6.configs.MagnetSensorConfigs;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.OpenLoopRampsConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.Slot1Configs;
import com.ctre.phoenix6.configs.Slot2Configs;
import com.ctre.phoenix6.configs.SlotConfigs;
import com.ctre.phoenix6.configs.SoftwareLimitSwitchConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.configs.VoltageConfigs;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import com.ctre.phoenix6.signals.GainSchedBehaviorValue;
import com.ctre.phoenix6.signals.GravityTypeValue;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;
import com.ctre.phoenix6.signals.StaticFeedforwardSignValue;

public interface Motors {

    public static class CANCoderConfig {
        private final CANcoderConfiguration configuration = new CANcoderConfiguration();
        private final MagnetSensorConfigs magnetSensorConfigs = new MagnetSensorConfigs();

        public void configure(CANcoder encoder) {
            CANcoderConfiguration defaultConfig = new CANcoderConfiguration();
            encoder.getConfigurator().apply(defaultConfig);

            encoder.getConfigurator().apply(configuration);
        }

        public CANcoderConfiguration getConfiguration() {
            return this.configuration;
        }

        // MAGNET SENSOR CONFIGS

        public CANCoderConfig withSensorDirection(SensorDirectionValue sensorDirection) {
            magnetSensorConfigs.SensorDirection = sensorDirection;

            configuration.withMagnetSensor(magnetSensorConfigs);

            return this;
        }

        public CANCoderConfig withAbsoluteSensorDiscontinuityPoint(double discontinuityPoint) {
            magnetSensorConfigs.AbsoluteSensorDiscontinuityPoint = discontinuityPoint;

            configuration.withMagnetSensor(magnetSensorConfigs);

            return this;
        }

        public CANCoderConfig withMagnetOffset(double magnetOffset) {
            magnetSensorConfigs.MagnetOffset = magnetOffset;

            configuration.withMagnetSensor(magnetSensorConfigs);

            return this;
        }
    }

    public static class TalonFXConfig {
        private final TalonFXConfiguration configuration = new TalonFXConfiguration();
        private final Slot0Configs slot0Configs = new Slot0Configs();
        private final Slot1Configs slot1Configs = new Slot1Configs();
        private final Slot2Configs slot2Configs = new Slot2Configs();
        private final MotorOutputConfigs motorOutputConfigs = new MotorOutputConfigs();
        private final ClosedLoopRampsConfigs closedLoopRampsConfigs = new ClosedLoopRampsConfigs();
        private final OpenLoopRampsConfigs openLoopRampsConfigs = new OpenLoopRampsConfigs();
        private final CurrentLimitsConfigs currentLimitsConfigs = new CurrentLimitsConfigs();
        private final FeedbackConfigs feedbackConfigs = new FeedbackConfigs();
        private final MotionMagicConfigs motionMagicConfigs = new MotionMagicConfigs();
        private final SoftwareLimitSwitchConfigs softwareLimitSwitchConfigs = new SoftwareLimitSwitchConfigs();
        private final ClosedLoopGeneralConfigs closedLoopGeneralConfigs = new ClosedLoopGeneralConfigs();
        private final VoltageConfigs voltageConfigs = new VoltageConfigs();

        private final double[] lastKP = new double[3];
        private final double[] lastKI = new double[3];
        private final double[] lastKD = new double[3];
        private final double[] lastKS = new double[3];
        private final double[] lastKV = new double[3];
        private final double[] lastKA = new double[3];

        public void configure(TalonFX motor) {
            TalonFXConfiguration defaultConfig = new TalonFXConfiguration();
            motor.getConfigurator().apply(defaultConfig);

            motor.getConfigurator().apply(configuration);
        }

        public TalonFXConfiguration getConfiguration() {
            return this.configuration;
        }

        public void updateGainsConfig(TalonFX motor, int slot, int kP, int kI, int kD, int kS, int kV, int kA) {
            if (slot != 0 && slot != 1 && slot != 2) {
                return;
            }

            double currentKP = kP;
            double currentKI = kI;
            double currentKD = kD;
            double currentKS = kS;
            double currentKV = kV;
            double currentKA = kA;

            boolean changed =
                currentKP != lastKP[slot] ||
                currentKI != lastKI[slot] ||
                currentKD != lastKD[slot] ||
                currentKS != lastKS[slot] ||
                currentKV != lastKV[slot] ||
                currentKA != lastKA[slot];

            if (!changed) {
                return;
            }

            SlotConfigs gainConfig = new SlotConfigs()
                .withKP(currentKP)
                .withKI(currentKI)
                .withKD(currentKD)
                .withKS(currentKS)
                .withKV(currentKV)
                .withKA(currentKA);

            gainConfig.SlotNumber = slot;

            motor.getConfigurator().apply(gainConfig);

            lastKP[slot] = currentKP;
            lastKI[slot] = currentKI;
            lastKD[slot] = currentKD;
            lastKS[slot] = currentKS;
            lastKV[slot] = currentKV;
            lastKA[slot] = currentKA;

            switch (slot) {
                case 0:
                    motor.getConfigurator().refresh(this.getConfiguration().Slot0);
                    break;
                case 1:
                    motor.getConfigurator().refresh(this.getConfiguration().Slot1);
                    break;
                case 2:
                    motor.getConfigurator().refresh(this.getConfiguration().Slot2);
                    break;
            }
        }

        // SLOT CONFIGS

        public TalonFXConfig withPIDConstants(double kP, double kI, double kD, int slot) {
            switch (slot) {
                case 0:
                    slot0Configs.kP = kP;
                    slot0Configs.kI = kI;
                    slot0Configs.kD = kD;
                    configuration.withSlot0(slot0Configs);
                    break;
                case 1:
                    slot1Configs.kP = kP;
                    slot1Configs.kI = kI;
                    slot1Configs.kD = kD;
                    configuration.withSlot1(slot1Configs);
                    break;
                case 2:
                    slot2Configs.kP = kP;
                    slot2Configs.kI = kI;
                    slot2Configs.kD = kD;
                    configuration.withSlot2(slot2Configs);
                    break;
            }
            return this;
        }

        public TalonFXConfig withFFConstants(double kS, double kV, double kA, int slot) {
            return withFFConstants(kS, kV, kA, 0.0, slot);
        }

        public TalonFXConfig withFFConstants(double kS, double kV, double kA, double kG, int slot) {
            switch (slot) {
                case 0:
                    slot0Configs.kS = kS;
                    slot0Configs.kV = kV;
                    slot0Configs.kA = kA;
                    slot0Configs.kG = kG;
                    configuration.withSlot0(slot0Configs);
                    break;
                case 1:
                    slot1Configs.kS = kS;
                    slot1Configs.kV = kV;
                    slot1Configs.kA = kA;
                    slot1Configs.kG = kG;
                    configuration.withSlot1(slot1Configs);
                    break;
                case 2:
                    slot2Configs.kS = kS;
                    slot2Configs.kV = kV;
                    slot2Configs.kA = kA;
                    slot2Configs.kG = kG;
                    configuration.withSlot2(slot2Configs);
                    break;
            }
            return this;
        }

        public TalonFXConfig withStaticFeedforwardSign(StaticFeedforwardSignValue staticFeedforwardSign, int slot) {
            switch (slot) {
                case 0:
                    slot0Configs.StaticFeedforwardSign = staticFeedforwardSign;
                    configuration.withSlot0(slot0Configs);
                    break;
                case 1:
                    slot1Configs.StaticFeedforwardSign = staticFeedforwardSign;
                    configuration.withSlot1(slot1Configs);
                    break;
                case 2:
                    slot2Configs.StaticFeedforwardSign = staticFeedforwardSign;
                    configuration.withSlot2(slot2Configs);
                    break;
            }

            return this;
        }

        public TalonFXConfig withGravityType(GravityTypeValue gravityType) {
            slot0Configs.GravityType = gravityType;
            slot1Configs.GravityType = gravityType;
            slot2Configs.GravityType = gravityType;

            configuration.withSlot0(slot0Configs);
            configuration.withSlot1(slot1Configs);
            configuration.withSlot2(slot2Configs);

            return this;
        }

        public TalonFXConfig withGainSchedBehavior(GainSchedBehaviorValue value, double threshold, int slot) {
            closedLoopGeneralConfigs.GainSchedErrorThreshold = threshold;
            configuration.withClosedLoopGeneral(closedLoopGeneralConfigs);
            
            switch(slot) {
                case 0: {
                    slot0Configs.GainSchedBehavior = value;
                    configuration.withSlot0(slot0Configs);
                }
                break;
                case 1: {
                    slot1Configs.GainSchedBehavior = value;
                    configuration.withSlot1(slot1Configs);
                }
                break;
                case 2: {
                    slot2Configs.GainSchedBehavior = value;
                    configuration.withSlot2(slot2Configs);
                }
                break;
            }

            return this;
        }

        // MOTOR OUTPUT CONFIGS

        public TalonFXConfig withInvertedValue(InvertedValue invertedValue) {
            motorOutputConfigs.Inverted = invertedValue;

            configuration.withMotorOutput(motorOutputConfigs);

            return this;
        }

        public TalonFXConfig withNeutralMode(NeutralModeValue neutralMode) {
            motorOutputConfigs.NeutralMode = neutralMode;

            configuration.withMotorOutput(motorOutputConfigs);

            return this;
        }

        public TalonFXConfig withVelocityTimeFilter(double filterInSeconds) {
            feedbackConfigs.withVelocityFilterTimeConstant(filterInSeconds);

            configuration.withFeedback(feedbackConfigs);

            return this;
        }

        // RAMP RATE CONFIGS

        public TalonFXConfig withRampRate(double rampRate) {
            closedLoopRampsConfigs.DutyCycleClosedLoopRampPeriod = rampRate;
            closedLoopRampsConfigs.TorqueClosedLoopRampPeriod = rampRate;
            closedLoopRampsConfigs.VoltageClosedLoopRampPeriod = rampRate;

            openLoopRampsConfigs.DutyCycleOpenLoopRampPeriod = rampRate;
            openLoopRampsConfigs.TorqueOpenLoopRampPeriod = rampRate;
            openLoopRampsConfigs.VoltageOpenLoopRampPeriod = rampRate;

            configuration.withClosedLoopRamps(closedLoopRampsConfigs);
            configuration.withOpenLoopRamps(openLoopRampsConfigs);

            return this;
        }

        // CURRENT LIMIT CONFIGS

        public TalonFXConfig withLowerLimitSupplyCurrent(double currentLowerLimitAmps) {
            currentLimitsConfigs.SupplyCurrentLowerLimit = currentLowerLimitAmps;
            currentLimitsConfigs.StatorCurrentLimitEnable = true;

            configuration.withCurrentLimits(currentLimitsConfigs);

            return this;
        }

        public TalonFXConfig withSupplyCurrentLimitAmps(double currentLimitAmps) {
            currentLimitsConfigs.SupplyCurrentLimit = currentLimitAmps;
            currentLimitsConfigs.SupplyCurrentLimitEnable = true;

            configuration.withCurrentLimits(currentLimitsConfigs);

            return this;
        }

        public TalonFXConfig withSupplyCurrentLimitEnabled(boolean enabled) {
            currentLimitsConfigs.SupplyCurrentLimitEnable = enabled;

            configuration.withCurrentLimits(currentLimitsConfigs);

            return this;
        }

        public TalonFXConfig withStatorCurrentLimitAmps(double currentLimitAmps) {
            currentLimitsConfigs.StatorCurrentLimit = currentLimitAmps;
            currentLimitsConfigs.StatorCurrentLimitEnable = true;

            configuration.withCurrentLimits(currentLimitsConfigs);

            return this;
        }

        public TalonFXConfig withStatorCurrentLimitEnabled(boolean enabled) {
            currentLimitsConfigs.StatorCurrentLimitEnable = enabled;

            configuration.withCurrentLimits(currentLimitsConfigs);

            return this;
        }

        // VOLTAGE LIMIT CONFIGS

        public TalonFXConfig withVoltageLimits(double peakForwardVoltage, double peakReverseVoltage) {
            voltageConfigs.PeakForwardVoltage = peakForwardVoltage;
            voltageConfigs.PeakReverseVoltage = peakReverseVoltage;

            configuration.withVoltage(voltageConfigs);

            return this;
        }

        // SOFTWARE LIMIT CONFIGS

        public TalonFXConfig withSoftLimits(boolean forwardEnable, boolean reverseEnable, double forwardThreshold, double reverseThreshold) {
            softwareLimitSwitchConfigs.ForwardSoftLimitEnable = forwardEnable;
            softwareLimitSwitchConfigs.ReverseSoftLimitEnable = reverseEnable;
            softwareLimitSwitchConfigs.ForwardSoftLimitThreshold = forwardThreshold;
            softwareLimitSwitchConfigs.ReverseSoftLimitThreshold = reverseThreshold;

            configuration.withSoftwareLimitSwitch(softwareLimitSwitchConfigs);

            return this;
        }

        // MOTION MAGIC CONFIGS

        public TalonFXConfig withMotionProfile(double maxVelocity, double maxAcceleration) {
            motionMagicConfigs.MotionMagicCruiseVelocity = maxVelocity;
            motionMagicConfigs.MotionMagicAcceleration = maxAcceleration;

            configuration.withMotionMagic(motionMagicConfigs);

            return this;
        }

        // FEEDBACK CONFIGS

        public TalonFXConfig withRemoteSensor(
                int ID, FeedbackSensorSourceValue source, double rotorToSensorRatio) {
            feedbackConfigs.FeedbackRemoteSensorID = ID;
            feedbackConfigs.FeedbackSensorSource = source;
            feedbackConfigs.RotorToSensorRatio = rotorToSensorRatio;

            configuration.withFeedback(feedbackConfigs);

            return this;
        }

        public TalonFXConfig withSensorToMechanismRatio(double sensorToMechanismRatio) {
            feedbackConfigs.SensorToMechanismRatio = sensorToMechanismRatio;

            configuration.withFeedback(feedbackConfigs);

            return this;
        }
    }

        public static class TurretConstants {
        public static int TURRET_MOTOR = 0;
        public static int TURRET_ENCODER_TURRET = 0;
        public static int TURRET_ENCODER_ENCODER = 0;

        public static TalonFXConfig turretConfigs = new TalonFXConfig()
            .withInvertedValue(InvertedValue.Clockwise_Positive)
            .withNeutralMode(NeutralModeValue.Brake)
            
            .withSupplyCurrentLimitAmps(80)
            .withStatorCurrentLimitEnabled(false)
            .withRampRate(0.25)
            
            .withPIDConstants(Gains.Turret.kP, Gains.Turret.kI, Gains.Turret.kD, 0)
            .withFFConstants(Gains.Turret.kS, Gains.Turret.kV, Gains.Turret.kA, 0)
            .withStaticFeedforwardSign(StaticFeedforwardSignValue.UseClosedLoopSign, 0)
            
            .withSensorToMechanismRatio(Settings.Turret.Constants.GEAR_RATIO_MOTOR_TO_MECH)

            .withSoftLimits(
                false, false,
                Settings.Turret.SoftwareLimit.FORWARD_MAX_ROTATIONS,
                Settings.Turret.SoftwareLimit.BACKWARDS_MAX_ROTATIONS);

        public static CANCoderConfig turretEncoderTurret = new CANCoderConfig()
            .withSensorDirection(SensorDirectionValue.CounterClockwise_Positive)
            .withMagnetOffset(Settings.Turret.Encoder.ENCODER_TURRET_OFFSET)
            .withAbsoluteSensorDiscontinuityPoint(1.0);

        public static CANCoderConfig turretEncoderEncoder = new CANCoderConfig()
            .withSensorDirection(SensorDirectionValue.CounterClockwise_Positive)
            .withMagnetOffset(Settings.Turret.Encoder.ENCODER_ENCODER_OFFSET)
            .withAbsoluteSensorDiscontinuityPoint(1.0);
    }

    public static class ShooterConstants {
        public static int RIGHT_MOTOR = 0;
        public static int LEFT_MOTOR = 0;

        public static TalonFXConfig shooterRightMotorConfig = new TalonFXConfig()
            .withInvertedValue(InvertedValue.CounterClockwise_Positive)
            .withNeutralMode(NeutralModeValue.Coast)
            
            .withSupplyCurrentLimitEnabled(false)
            .withStatorCurrentLimitEnabled(false)

            .withPIDConstants(Gains.Shooter.kP, Gains.Shooter.kI, Gains.Shooter.kD, 0)
            .withFFConstants(Gains.Shooter.kS, Gains.Shooter.kV, Gains.Shooter.kA, 0)
                             
            .withSensorToMechanismRatio(Settings.Shooter.GEAR_RATIO)
            .withVelocityTimeFilter(0.1);

        public static TalonFXConfig shooterLeftMotorConfig = new TalonFXConfig()
            .withInvertedValue(InvertedValue.CounterClockwise_Positive)
            .withNeutralMode(NeutralModeValue.Coast)
            
            .withSupplyCurrentLimitEnabled(false)
            .withStatorCurrentLimitEnabled(false)

            .withPIDConstants(Gains.Shooter.kP, Gains.Shooter.kI, Gains.Shooter.kD, 0)
            .withFFConstants(Gains.Shooter.kS, Gains.Shooter.kV, Gains.Shooter.kA, 0)
                             
            .withSensorToMechanismRatio(Settings.Shooter.GEAR_RATIO)
            .withVelocityTimeFilter(0.1);
    }

    public static class IntakeConstants {
        public static int PIVOT = 0;
        public static int MOTOR1 = 0;
        public static int MOTOR2 = 0;

        public static TalonFXConfig intakePivotConfig = new TalonFXConfig()
                .withInvertedValue(InvertedValue.Clockwise_Positive)
                .withNeutralMode(NeutralModeValue.Brake)

                .withSupplyCurrentLimitAmps(60)
                .withStatorCurrentLimitEnabled(false)
                .withRampRate(0.25)

                .withPIDConstants(Gains.Intake.Pivot.kP, Gains.Intake.Pivot.kI, Gains.Intake.Pivot.kD, 0)
                .withFFConstants(Gains.Intake.Pivot.kS, Gains.Intake.Pivot.kV, Gains.Intake.Pivot.kA,
                        Gains.Intake.Pivot.kG, 0)
                .withStaticFeedforwardSign(StaticFeedforwardSignValue.UseVelocitySign, 0)
                .withGravityType(GravityTypeValue.Arm_Cosine)

                .withSensorToMechanismRatio(Settings.Intake.GEAR_RATIO);

        public static TalonFXConfig intakeMotor1Config = new TalonFXConfig()
                .withInvertedValue(InvertedValue.Clockwise_Positive)
                .withNeutralMode(NeutralModeValue.Brake)

                .withSupplyCurrentLimitAmps(60)
                .withStatorCurrentLimitEnabled(false)
                .withRampRate(0.25)

                .withPIDConstants(Gains.Intake.Pivot.kP, Gains.Intake.Pivot.kI, Gains.Intake.Pivot.kD, 0)
                .withFFConstants(Gains.Intake.Pivot.kS, Gains.Intake.Pivot.kV, Gains.Intake.Pivot.kA,
                        Gains.Intake.Pivot.kG, 0)
                .withStaticFeedforwardSign(StaticFeedforwardSignValue.UseVelocitySign, 0)
                .withGravityType(GravityTypeValue.Arm_Cosine)

                .withSensorToMechanismRatio(Settings.Intake.GEAR_RATIO);

        public static TalonFXConfig intakeMotor2Config = new TalonFXConfig()
                .withInvertedValue(InvertedValue.Clockwise_Positive)
                .withNeutralMode(NeutralModeValue.Brake)

                .withSupplyCurrentLimitAmps(60)
                .withStatorCurrentLimitEnabled(false)
                .withRampRate(0.25)

                .withPIDConstants(Gains.Intake.Pivot.kP, Gains.Intake.Pivot.kI, Gains.Intake.Pivot.kD, 0)
                .withFFConstants(Gains.Intake.Pivot.kS, Gains.Intake.Pivot.kV, Gains.Intake.Pivot.kA,
                        Gains.Intake.Pivot.kG, 0)
                .withStaticFeedforwardSign(StaticFeedforwardSignValue.UseVelocitySign, 0)
                .withGravityType(GravityTypeValue.Arm_Cosine)

                .withSensorToMechanismRatio(Settings.Intake.GEAR_RATIO);
    }

    public static class SpindexerConstants {
        public static int INTAKE_SPINDEXER_MOTOR = 0;
        public static int FAR_SPINDEXER_MOTOR = 0;
        public static int TRANSITION_MOTOR = 0;

        public static TalonFXConfig intakeSpindexerMotorConfig = new TalonFXConfig()
            .withInvertedValue(InvertedValue.Clockwise_Positive)
            .withNeutralMode(NeutralModeValue.Brake)
            
            .withSupplyCurrentLimitAmps(45)
            .withStatorCurrentLimitEnabled(false)
            .withRampRate(0.25)
            
            .withPIDConstants(Gains.Spindexer.kP, Gains.Spindexer.kI, Gains.Spindexer.kD, 0)
            .withFFConstants(Gains.Spindexer.kS, Gains.Spindexer.kV, Gains.Spindexer.kA, 0)
            
            .withSensorToMechanismRatio(Settings.Spindexer.GEAR_RATIO);

        public static TalonFXConfig farSpindexerMotorConfig = new TalonFXConfig()
            .withInvertedValue(InvertedValue.Clockwise_Positive)
            .withNeutralMode(NeutralModeValue.Brake)
            
            .withSupplyCurrentLimitAmps(45)
            .withStatorCurrentLimitEnabled(false)
            .withRampRate(0.25)
            
            .withPIDConstants(Gains.Spindexer.kP, Gains.Spindexer.kI, Gains.Spindexer.kD, 0)
            .withFFConstants(Gains.Spindexer.kS, Gains.Spindexer.kV, Gains.Spindexer.kA, 0)
            
            .withSensorToMechanismRatio(Settings.Spindexer.GEAR_RATIO);

        public static TalonFXConfig transitionMotorConfig = new TalonFXConfig()
            .withInvertedValue(InvertedValue.CounterClockwise_Positive)
            .withNeutralMode(NeutralModeValue.Brake)
            
            .withSupplyCurrentLimitAmps(80.0)
            .withStatorCurrentLimitEnabled(false)
            .withRampRate(0.25)
            
            .withPIDConstants(Gains.Transition.kP, Gains.Transition.kI, Gains.Transition.kD, 0)
            .withFFConstants(Gains.Transition.kS, Gains.Transition.kV, Gains.Transition.kA, 0)
            
            .withSensorToMechanismRatio(Settings.Transition.GEAR_RATIO);
    }

    public static class HoodConstants {
        public static int HOOD_MOTOR = 0;
        public static int HOOD_ENCODER = 0;

        public static TalonFXConfig hoodMotorConfigs = new TalonFXConfig()
                .withInvertedValue(InvertedValue.CounterClockwise_Positive)
                .withNeutralMode(NeutralModeValue.Brake)
                .withSupplyCurrentLimitAmps(80.0)
                .withStatorCurrentLimitEnabled(false)
                .withRampRate(0.25)
                .withPIDConstants(Gains.Hood.kP, Gains.Hood.kI, Gains.Hood.kD, 0)
                .withFFConstants(Gains.Hood.kS, Gains.Hood.kV, Gains.Hood.kA, 0)
                .withStaticFeedforwardSign(StaticFeedforwardSignValue.UseClosedLoopSign, 0)
                .withSensorToMechanismRatio(Settings.Hood.GEAR_RATIO)
                .withSoftLimits(
                        true, true,
                        Settings.Hood.FORWARD_SOFT_LIMIT,
                        Settings.Hood.REVERSE_SOFT_LIMIT);

        public static CANCoderConfig hoodEncoderConfigs = new CANCoderConfig()
                .withSensorDirection(SensorDirectionValue.CounterClockwise_Positive)
                .withAbsoluteSensorDiscontinuityPoint(1.0)
                .withMagnetOffset(Settings.Hood.ENCODER_OFFSET);
    }
}
package com.INT.robot.subsystems.Hood;

import com.INT.robot.constants.Motors;
import com.INT.robot.constants.Settings;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Hood extends SubsystemBase {

    // private static final Hood instance; 

    // static {
    //     instance = new Hood();
    // }

    // public static Hood getInstance() {
    //     return instance;
    // }

    private TalonFX hoodMotor;
    private CANcoder hoodEncoder;

    private double targetAngle;

    private final PositionVoltage positionVoltage = new PositionVoltage(0).withEnableFOC(true);

    public Hood() {
        hoodMotor = new TalonFX(Motors.HoodConstants.HOOD_MOTOR, "yuumi");
        Motors.HoodConstants.hoodMotorConfigs.Feedback.FeedbackRemoteSensorID = Motors.HoodConstants.HOOD_ENCODER;
        hoodMotor.getConfigurator().apply(Motors.HoodConstants.hoodMotorConfigs);
        hoodMotor.setNeutralMode(NeutralModeValue.Brake);

        hoodEncoder = new CANcoder(Motors.HoodConstants.HOOD_ENCODER, "yuumi");
        hoodEncoder.getConfigurator().apply(Motors.HoodConstants.hoodEncoderConfigs);
    }

    public void setHoodAngle(double Angle) {
        targetAngle = Angle;

        hoodMotor.setControl(
            positionVoltage
            .withPosition(Angle));
    }

    public void stowHood() {
        hoodMotor.setControl(
            positionVoltage
            .withPosition(0)
        );
    }

    public void resetHood() {
        hoodEncoder.setPosition(0);
    }

    public boolean hoodAtPosition() {
        return (hoodEncoder.getPosition().getValueAsDouble() - targetAngle) < Settings.Hood.HOOD_TOLERANCE;
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("Hood/TargetAngle", targetAngle);
    }
}
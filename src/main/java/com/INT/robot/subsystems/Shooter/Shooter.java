package com.INT.robot.subsystems.Shooter;

import com.INT.robot.constants.Motors;
import com.INT.robot.constants.Motors.ShooterConstants;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {

    private static final Shooter instance; 

    static {
        instance = new Shooter();
    }

    public static Shooter getInstance() {
        return instance;
    }

    private TalonFX rightMotor;
    private TalonFX leftMotor;
    private CANcoder shooterEncoder;

    public Shooter() {
        rightMotor = new TalonFX(ShooterConstants.RIGHT_MOTOR, "yuumi");
        leftMotor = new TalonFX(ShooterConstants.LEFT_MOTOR, "yuumi");
        shooterEncoder = new CANcoder(ShooterConstants.SHOOTER_ENCODER, "yuumi");

        rightMotor.getConfigurator().apply(ShooterConstants.shooterRightMotorConfig);
        leftMotor.getConfigurator().apply(ShooterConstants.shooterLeftMotorConfig);
        shooterEncoder.getConfigurator().apply(ShooterConstants.shooterEncoderConfig);

        leftMotor.setControl(new Follower(Motors.ShooterConstants.RIGHT_MOTOR, MotorAlignmentValue.Aligned)); //set to Opposite for other direction
    }

    public void setRightMotorRPM() {
        return 
    } 
}

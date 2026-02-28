package com.INT.robot.subsystems.Shooter;

import com.INT.robot.constants.Motors;
import com.INT.robot.constants.Motors.ShooterConstants;
import com.INT.robot.constants.Settings;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {

    // private static final Shooter instance; 

    // static {
    //     instance = new Shooter();
    // }

    // public static Shooter getInstance() {
    //     return instance;
    // }

    private TalonFX rightMotor;
    private TalonFX leftMotor;

    private double speed;
    public boolean isShooting;
    private final VelocityVoltage velocityRequest = new VelocityVoltage(0).withEnableFOC(true);

    public Shooter() {
        rightMotor = new TalonFX(ShooterConstants.RIGHT_MOTOR, "yuumi");
        rightMotor.getConfigurator().apply(ShooterConstants.shooterRightMotorConfig);
        rightMotor.setNeutralMode(NeutralModeValue.Coast);

        leftMotor = new TalonFX(ShooterConstants.LEFT_MOTOR, "yuumi");
        leftMotor.getConfigurator().apply(ShooterConstants.shooterLeftMotorConfig);
        leftMotor.setNeutralMode(NeutralModeValue.Coast);

        leftMotor.setControl(new Follower(Motors.ShooterConstants.RIGHT_MOTOR, MotorAlignmentValue.Aligned)); //set to Opposite for other direction

        isShooting = true;
    }

    public void setRightMotorRPM(double rpm) {
        this.speed = rpm;

        if (!shooterAtSpeed() && isShooting == true) {
            double rps = rpm/60;
            rightMotor.setControl(velocityRequest.withVelocity(rps));
        } else {
            isShooting = getRightMotorRPM() > 0;
        }
    }

    public double getRightMotorRPM() {
        return rightMotor.getVelocity().getValueAsDouble() * 60;
    }

    public double getLeftMotorRPM() {
        return leftMotor.getVelocity().getValueAsDouble() * 60;
    }

    public void stopShooter() {
        rightMotor.stopMotor();
    }

    public void setShooting(boolean shooting) {
        this.isShooting = shooting;
    }

    public double getRightTargetVoltage() {
        return rightMotor.getMotorVoltage().getValueAsDouble();
    }

    public boolean shooterAtSpeed() {
        return (Math.abs(getRightMotorRPM() - speed) < Settings.Shooter.SHOOTER_RPM_TOLERANCE) &&
                (Math.abs(getLeftMotorRPM() - speed) < Settings.Shooter.SHOOTER_RPM_TOLERANCE);
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("Shooter/rightSpeed", getRightMotorRPM());
        SmartDashboard.putNumber("Shooter/leftSpeed", getLeftMotorRPM());
        SmartDashboard.putNumber("Shooter/targetSpeed", speed);
        SmartDashboard.putBoolean("Shooter/atSpeed", shooterAtSpeed());
        SmartDashboard.putBoolean("Shooter/isShooting", isShooting);
        SmartDashboard.putNumber("Shooter/rightTargetVoltage", getRightTargetVoltage());
    }
}
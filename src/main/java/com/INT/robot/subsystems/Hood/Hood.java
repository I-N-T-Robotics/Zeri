package com.INT.robot.subsystems.Hood;

import com.INT.robot.constants.Motors;
import com.INT.robot.constants.Settings;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Hood extends SubsystemBase {
    
    private TalonFX hoodMotor;
    private CANcoder hoodEncoder;

    private double targetAngle;
    private double targetVel;

    private final PositionVoltage positionVoltage = new PositionVoltage(0).withEnableFOC(true);
    private final VelocityVoltage velocityVoltage = new VelocityVoltage(0).withEnableFOC(true);

    public Hood() {
        hoodMotor = new TalonFX(Motors.HoodConstants.HOOD_MOTOR, "yuumi");
        hoodMotor.setNeutralMode(NeutralModeValue.Brake);

        hoodEncoder = new CANcoder(Motors.HoodConstants.HOOD_ENCODER, "yuumi");
    }

    public void setHoodAngle(double Angle) {
        targetAngle = Angle;

        hoodMotor.setControl(
            positionVoltage
            .withPosition(Angle));
    }

    public void stowHood() {
        hoodMotor.setControl(
            velocityVoltage
            .withVelocity(Settings.Hood.HOOD_RESET_RPS)
        );
        targetVel = Settings.Hood.HOOD_RESET_RPS;
    }

    public void resetHood() {
        hoodEncoder.setPosition(0);
        targetVel = 0;
    }

    public boolean hoodAtPosition() {
        return (hoodEncoder.getPosition().getValueAsDouble() - targetAngle) < Settings.Hood.HOOD_TOLERANCE;
    }

    public boolean hoodIsStalling() {
        return ((hoodMotor.getStatorCurrent().getValueAsDouble() >  Settings.Hood.HOOD_STALL_CURRENT) &&
                (targetVel == Settings.Hood.HOOD_RESET_RPS));
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("Hood/TargetAngle", targetAngle);
        SmartDashboard.putBoolean("Hood/isStalling", hoodIsStalling());
        SmartDashboard.putBoolean("Hood/atPosition", hoodAtPosition());
    }
}
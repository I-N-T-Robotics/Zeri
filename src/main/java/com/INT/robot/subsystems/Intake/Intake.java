package com.INT.robot.subsystems.Intake;

import com.INT.robot.constants.Motors.IntakeConstants;
import com.INT.robot.constants.Settings;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase {

    private TalonFX intakePivot;
    private TalonFX intakeMotor1;
    private TalonFX intakeMotor2;

    private final PositionVoltage positionVoltage = new PositionVoltage(0).withEnableFOC(true);
    private final VelocityVoltage velocityVoltage = new VelocityVoltage(0).withEnableFOC(true);

    public Intake() {
        intakePivot = new TalonFX(IntakeConstants.PIVOT, "yuumi");
        intakePivot.getConfigurator().apply(IntakeConstants.intakePivotConfig);
        intakePivot.setNeutralMode(NeutralModeValue.Coast);

        intakeMotor1 = new TalonFX(IntakeConstants.MOTOR1, "yuumi");
        intakeMotor1.getConfigurator().apply(IntakeConstants.intakeMotor1Config);
        intakeMotor1.setNeutralMode(NeutralModeValue.Brake);

        intakeMotor2 = new TalonFX(IntakeConstants.MOTOR2, "yuumi");
        intakeMotor2.getConfigurator().apply(IntakeConstants.intakeMotor2Config);
        intakeMotor2.setNeutralMode(NeutralModeValue.Brake);

        intakeMotor2.setControl(new Follower(IntakeConstants.MOTOR1, MotorAlignmentValue.Opposed));
    }

    public void deploy() {
        intakePivot.setControl(
            positionVoltage
            .withPosition(Settings.Intake.DEPLOYED_POSITION));
    }

    public void undeploy() {
        intakePivot.setControl(
            positionVoltage
            .withPosition(Settings.Intake.UP_POSITION));
    }

    public void intake() {
        intakeMotor1.setControl(
            velocityVoltage
            .withVelocity(Settings.Intake.INTAKE_RPM)
        );
    }

    public void outtake() {
        intakeMotor1.setControl(
            velocityVoltage
            .withVelocity(Settings.Intake.OUTTAKE_RPM)
        );
    }

    public void stopIntake() {
        intakeMotor1.stopMotor();
    }

    public void stopIntakePivot() {
        intakePivot.stopMotor();
    }

    public double getIntakePosition() {
        return intakePivot.getPosition().getValueAsDouble();
    }

    public double getIntakeSpeed() {
        return intakeMotor1.getVelocity().getValueAsDouble();
    }

    public boolean intakeAtDeployPosition() {
        return Math.abs(getIntakePosition() - Settings.Intake.DEPLOYED_POSITION) < Settings.Intake.INTAKE_POSITION_TOLERANCE;
    }

    public boolean intakeAtUndeployPosition() {
        return Math.abs(getIntakePosition() - Settings.Intake.UP_POSITION) < Settings.Intake.INTAKE_POSITION_TOLERANCE;
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("Intake/position", getIntakePosition());
        SmartDashboard.putNumber("Intake/Speed", getIntakeSpeed());
        SmartDashboard.putBoolean("Intake/atDeployedPosition", intakeAtDeployPosition());
        SmartDashboard.putBoolean("Intake/atUndeployedPosition", intakeAtUndeployPosition());
    }
}
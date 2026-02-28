package com.INT.robot.subsystems.Spindexer;

import com.INT.robot.constants.Motors;
import com.INT.robot.constants.Settings;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Spindexer extends SubsystemBase {

    // private static final Spindexer instance; 

    // static {
    //     instance = new Spindexer();
    // }

    // public static Spindexer getInstance() {
    //     return instance;
    // }

    private TalonFX intakeSpindexMotor;
    private TalonFX farSpindexMotor;

    private TalonFX transitionMotor;

    private final VelocityVoltage velocityVoltage = new VelocityVoltage(0).withEnableFOC(true);

    public Spindexer() {
        intakeSpindexMotor = new TalonFX(Motors.SpindexerConstants.INTAKE_SPINDEXER_MOTOR, "yuumi");
        intakeSpindexMotor.getConfigurator().apply(Motors.SpindexerConstants.intakeSpindexerMotorConfig);
        intakeSpindexMotor.setNeutralMode(NeutralModeValue.Coast);

        farSpindexMotor = new TalonFX(Motors.SpindexerConstants.FAR_SPINDEXER_MOTOR, "yuumi");
        farSpindexMotor.getConfigurator().apply(Motors.SpindexerConstants.farSpindexerMotorConfig);
        farSpindexMotor.setNeutralMode(NeutralModeValue.Coast);

        transitionMotor = new TalonFX(Motors.SpindexerConstants.TRANSITION_MOTOR, "yuumi");
        transitionMotor.getConfigurator().apply(Motors.SpindexerConstants.transitionMotorConfig);
        transitionMotor.setNeutralMode(NeutralModeValue.Coast);

        intakeSpindexMotor.setControl(new Follower(Motors.SpindexerConstants.FAR_SPINDEXER_MOTOR,  MotorAlignmentValue.Aligned));
    }

    public void startTransition() {
        transitionMotor.setControl(
            velocityVoltage
            .withVelocity(Settings.Spindexer.SPINDEXER_RPM * Settings.Spindexer.TRANSITION_TO_SPEED_RATIO));
    }

    public void stopTransition() {
        transitionMotor.setControl(
            velocityVoltage
            .withVelocity(0));
    }

    public double getTransitionRPM() {
        return transitionMotor.getVelocity().getValueAsDouble();
    }

    public void startSpindexer() {
        farSpindexMotor.setControl(
            velocityVoltage
            .withVelocity(Settings.Spindexer.SPINDEXER_RPM));
    }

    public void stopSpindexer() {
        farSpindexMotor.setControl(
            velocityVoltage
            .withVelocity(0));
    }

    public double getSpindexerRPM() {
        return farSpindexMotor.getVelocity().getValueAsDouble();
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("Spindexer/SpindexerRPM", getSpindexerRPM());
        SmartDashboard.putNumber("Spindexer/TransitionRPM", getTransitionRPM());
    }
}
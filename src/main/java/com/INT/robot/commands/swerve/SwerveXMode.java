package com.INT.robot.commands.swerve;

import com.INT.robot.subsystems.Swerve.CommandSwerveDrivetrain;

import edu.wpi.first.wpilibj2.command.Command;

import com.ctre.phoenix6.swerve.SwerveRequest;

public class SwerveXMode extends Command {
    private final CommandSwerveDrivetrain drivetrain;
    private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();

    public SwerveXMode(CommandSwerveDrivetrain drivetrain) {
        this.drivetrain = drivetrain;
        addRequirements(drivetrain);
    }

    @Override
    public void execute() {
        drivetrain.setControl(brake);
    }
}
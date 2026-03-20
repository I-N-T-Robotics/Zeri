package com.INT.robot.commands.swerve;

import com.INT.robot.subsystems.Swerve.CommandSwerveDrivetrain;

import edu.wpi.first.wpilibj2.command.Command;

import com.ctre.phoenix6.swerve.SwerveRequest;

public class SwerveXMode extends Command {
    private final CommandSwerveDrivetrain drivetrain;
    private boolean isXMode;
    private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
    private final SwerveRequest.RobotCentric request = new SwerveRequest.RobotCentric();

    public SwerveXMode(CommandSwerveDrivetrain drivetrain) {
        this.drivetrain = drivetrain;
        addRequirements(drivetrain);
        isXMode = false;
    }

    @Override
    public void initialize() {
        drivetrain.setControl(brake);
        isXMode = !isXMode;
    }

    @Override
    public boolean isFinished() {
        return isXMode == false;
    }

    @Override
    public void end(boolean interrupted) {
        drivetrain.setControl(request);
    }
}

//is a toggle command
package com.INT.robot.commands.swerve;

import com.INT.robot.subsystems.Swerve.CommandSwerveDrivetrain;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;

public class SwerveResetHeading extends Command {
    private final CommandSwerveDrivetrain drivetrain;

    public SwerveResetHeading(CommandSwerveDrivetrain drivetrain) {
        this.drivetrain = drivetrain;
        addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        drivetrain.resetRotation(Rotation2d.kZero);
    }
}
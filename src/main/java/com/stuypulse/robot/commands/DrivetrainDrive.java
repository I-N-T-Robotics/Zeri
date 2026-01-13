package com.stuypulse.robot.commands;

import com.stuypulse.robot.subsystems.Drivetrain;
import com.stuypulse.stuylib.input.Gamepad;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class DrivetrainDrive extends CommandBase {
    
    private Drivetrain drivetrain;
    private Gamepad driver;

    public DrivetrainDrive(Drivetrain drivetrain, Gamepad driver) {
        this.drivetrain = drivetrain;
        this.driver = driver;

        addRequirements(drivetrain);
    }

    @Override
    public void execute() {
        drivetrain.curvatureDrive(
            (driver.getRightTrigger() - driver.getLeftTrigger()) / 2,
            -driver.getLeftX() / 3,
            true);
    }

}

package com.INT.robot.commands.hood;

import com.INT.robot.subsystems.Hood.Hood;
import com.INT.robot.subsystems.Swerve.CommandSwerveDrivetrain;
import com.INT.robot.subsystems.Turret.Turret;
import com.INT.robot.util.Distance;
import com.INT.robot.util.HoodInterpolation;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;

public class HoodAim extends Command {
    private final Hood hood;
    private final Turret turret;
    private final CommandSwerveDrivetrain drivetrain;

    public HoodAim(Hood hood, Turret turret, CommandSwerveDrivetrain drivetrain) {
        this.hood = hood;
        this.turret = turret;
        this.drivetrain = drivetrain;
        addRequirements(hood, turret, drivetrain);
    }

    @Override
    public void execute() {
        Pose2d robotPose = drivetrain.getPose();
        Translation2d targetPose = turret.getGoalPosition();

        double distance = Distance.calculateDistance(robotPose.getX(), robotPose.getY(), targetPose.getX(), targetPose.getY());

        hood.setHoodAngle(HoodInterpolation.getAngle(distance)); //TODO: make sure distance is in inches (For shooter too)
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
package com.INT.robot.commands.shooter;

import com.INT.robot.subsystems.Shooter.Shooter;
import com.INT.robot.subsystems.Swerve.CommandSwerveDrivetrain;
import com.INT.robot.subsystems.Turret.Turret;
import com.INT.robot.util.Distance;
import com.INT.robot.util.ShootInterpolation;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;

public class ShooterShoot extends Command {
    private final Shooter shooter;

    public ShooterShoot(Shooter shooter) {
        this.shooter = shooter;
        addRequirements(shooter);
    }

    @Override
    public void execute() {
        Pose2d robotPose = CommandSwerveDrivetrain.getInstance().getPose();
        Translation2d targetPose = Turret.getInstance().getGoalPosition();

        double distance = Distance.calculateDistance(robotPose.getX(), robotPose.getY(), targetPose.getX(), targetPose.getY());

        shooter.setRightMotorRPM(ShootInterpolation.getRPM(distance));
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
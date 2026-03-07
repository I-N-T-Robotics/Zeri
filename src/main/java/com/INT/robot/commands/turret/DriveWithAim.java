package com.INT.robot.commands.turret;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;
import org.littletonrobotics.junction.Logger;

import com.INT.robot.subsystems.Swerve.CommandSwerveDrivetrain;

public class DriveWithAim extends Command {

  private final CommandSwerveDrivetrain drive;
  private final DoubleSupplier xSupplier;
  private final DoubleSupplier ySupplier;
  private final Supplier<Pose2d> targetPoseSupplier;
  private final PIDController rotationController;

  private static final double DEADBAND = 0.1;

  private static final double KP = 20.0;
  private static final double KI = 0.0;
  private static final double KD = 2;
  public static final double KFF = 1;
  public static final double kA = 0.2;

  private ChassisSpeeds previousSpeeds = new ChassisSpeeds();
  private double previousTime = 0.0;

  private static final double ROTATION_TOLERANCE_RADIANS = Math.toRadians(2.0);

  /**
   * @param drive The drive subsystem
   * @param xSupplier Supplier for frontal movement
   * @param ySupplier Supplier for strafe
   * @param targetPoseSupplier Supplier for the target pose to face
   */
  public DriveWithAim(
      CommandSwerveDrivetrain drive,
      DoubleSupplier xSupplier,
      DoubleSupplier ySupplier,
      Supplier<Pose2d> targetPoseSupplier) {
    this.drive = drive;
    this.xSupplier = xSupplier;
    this.ySupplier = ySupplier;
    this.targetPoseSupplier = targetPoseSupplier;

    this.rotationController = new PIDController(KP, KI, KD);
    rotationController.enableContinuousInput(-Math.PI, Math.PI);
    rotationController.setTolerance(ROTATION_TOLERANCE_RADIANS);

    addRequirements(drive);
  }

  @Override
  public void initialize() {
    rotationController.reset();
    previousSpeeds = new ChassisSpeeds();
    previousTime = Timer.getFPGATimestamp();
  }

  @Override
  public void execute() {
    double currentTime = Timer.getFPGATimestamp();
    double dt = currentTime - previousTime;

    Pose2d currentPose = drive.getPose();

    Pose2d targetPose = targetPoseSupplier.get();

    double dx = targetPose.getX() - currentPose.getX();
    double dy = targetPose.getY() - currentPose.getY();
    Rotation2d angleToTarget = new Rotation2d(dx, dy);

    ChassisSpeeds currentSpeeds = drive.getFieldRelativeSpeeds();
    double vx = currentSpeeds.vxMetersPerSecond;
    double vy = currentSpeeds.vyMetersPerSecond;

    double distance = Math.hypot(dx, dy);

    double ax = 0.0;
    double ay = 0.0;
    if (dt > 0.001) {
      ax = (vx - previousSpeeds.vxMetersPerSecond) / dt;
      ay = (vy - previousSpeeds.vyMetersPerSecond) / dt;
    }

    double angleVelocityFeedforward = 0.0;
    double angleAccelerationFeedforward = 0.0;

    if (distance > 0.1) {
      angleVelocityFeedforward = KFF * (dx * vy - dy * vx) / (distance * distance);
      angleAccelerationFeedforward = kA * (dx * ay - dy * ax) / (distance * distance);
    }

    double rotationVelocity =
        rotationController.calculate(
                currentPose.getRotation().getRadians(), angleToTarget.getRadians())
            + angleVelocityFeedforward
            + angleAccelerationFeedforward;

    double maxAngularSpeed = drive.getMaxAngularSpeedRadPerSec();
    rotationVelocity = Math.max(-maxAngularSpeed, Math.min(maxAngularSpeed, rotationVelocity));

    Translation2d linearVelocity =
        CommandSwerveDrivetrain.getLinearVelocityFromJoysticks(
            xSupplier.getAsDouble(), ySupplier.getAsDouble());

    ChassisSpeeds speeds =
        new ChassisSpeeds(
            linearVelocity.getX() * drive.getMaxLinearSpeedMetersPerSec(),
            linearVelocity.getY() * drive.getMaxLinearSpeedMetersPerSec(),
            rotationVelocity);

    boolean isFlipped =
        DriverStation.getAlliance().isPresent()
            && DriverStation.getAlliance().get() == Alliance.Red;
    drive.setChassisSpeeds(
        ChassisSpeeds.fromFieldRelativeSpeeds(
            speeds,
            isFlipped ? drive.getRotation().plus(new Rotation2d(Math.PI)) : drive.getRotation()));

    Logger.recordOutput("DriveWithAim/TargetPose", targetPose);
    Logger.recordOutput("DriveWithAim/AngleToTarget", angleToTarget.getDegrees());
    Logger.recordOutput("DriveWithAim/AngleError", rotationController.getPositionError());
    Logger.recordOutput("DriveWithAim/RotationVelocity", rotationVelocity);
    Logger.recordOutput("DriveWithAim/AtTarget", rotationController.atSetpoint());
    Logger.recordOutput("DriveWithAim/FeedforwardRadPerSec", angleVelocityFeedforward);
  }

  @Override
  public boolean isFinished() {
    return false;
  }

  public boolean isAimed() {
    return rotationController.atSetpoint();
  }
}

package com.INT.robot.subsystems.Vision;

import com.INT.robot.Robot;
import com.INT.robot.constants.Cameras;
import com.INT.robot.constants.Cameras.Camera;
import com.INT.robot.constants.Settings;
import com.INT.robot.subsystems.Swerve.CommandSwerveDrivetrain;
import com.INT.robot.util.vision.LimelightHelpers;
import com.INT.robot.util.vision.LimelightHelpers.PoseEstimate;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LimelightVision extends SubsystemBase {

    private static final LimelightVision instance;

    static {
        instance = new LimelightVision();
    }

    public static LimelightVision getInstance() {
        return instance;
    }

    public enum MegaTagMode {
        MT1,
        MT2
    }

    private MegaTagMode megaTagMode;
    private int imuMode;
    private int maxTagCount;

    private LimelightVision() {
        for (Camera camera : Cameras.LimelightCameras) {
            Pose3d cameraLocation = camera.getLocation();
            LimelightHelpers.setCameraPose_RobotSpace(
                camera.getName(),
                cameraLocation.getX(),
                -cameraLocation.getY(),
                cameraLocation.getZ(),
                Units.radiansToDegrees(cameraLocation.getRotation().getX()),
                Units.radiansToDegrees(cameraLocation.getRotation().getY()),
                Units.radiansToDegrees(cameraLocation.getRotation().getZ())
                );
        }
        setMegaTagMode(MegaTagMode.MT1);
        setIMUMode(1);
        maxTagCount = 0;
    }

    public void setMegaTagMode(MegaTagMode mode) {
        this.megaTagMode = mode;
        switch (mode) {
            case MT1:
                CommandSwerveDrivetrain.getInstance().setVisionMeasurementStdDevs(Settings.Vision.MT1_STDDEVS);
                break;
            case MT2:
                CommandSwerveDrivetrain.getInstance().setVisionMeasurementStdDevs(Settings.Vision.MT1_STDDEVS);
                break;
        }
    }
    
    public void setPipelineMode(int pipeline, String limelightName) {
        LimelightHelpers.setPipelineIndex(limelightName, pipeline);
    }

    public void setIMUMode(int mode) {
        this.imuMode = mode;
        for (Camera camera : Cameras.LimelightCameras) {
            LimelightHelpers.SetIMUMode(camera.getName(), mode);
        }
    }

    public int getMaxTagCount() {
        return this.maxTagCount;
    }

    public MegaTagMode getMegaTagMode() {
        return megaTagMode;
    }

    public PoseEstimate getMT1PoseEstimate(String limelightName) {
        return Robot.isBlue()
            ? LimelightHelpers.getBotPoseEstimate_wpiBlue(limelightName)
            : LimelightHelpers.getBotPoseEstimate_wpiRed(limelightName);
    }

    public PoseEstimate getMT2PoseEstimate(String limelightName) {
        return Robot.isBlue()
            ? LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2(limelightName)
            : LimelightHelpers.getBotPoseEstimate_wpiRed_MegaTag2(limelightName);
    }

    private boolean robotIsOnAllianceSide() {
        Pose2d pose = CommandSwerveDrivetrain.getInstance().getPose();
        return Robot.isBlue()
            ? pose.getX() < Units.inchesToMeters(182.11)
            : pose.getX() > Units.inchesToMeters(469.11);
    }

    @Override
    public void periodic() {
        this.maxTagCount = 0;

        for (Camera camera : Cameras.LimelightCameras) {
            PoseEstimate poseEstimate = (megaTagMode == MegaTagMode.MT2)
                ? getMT2PoseEstimate(camera.getName())
                : getMT1PoseEstimate(camera.getName());

            LimelightHelpers.SetRobotOrientation(
                camera.getName(),
                (CommandSwerveDrivetrain.getInstance().getPose().getRotation().getDegrees() + (Robot.isBlue() ? 0 : 180)) % 360,
                imuMode, imuMode, imuMode, imuMode, imuMode);

                if ((poseEstimate != null) && poseEstimate.tagCount > 0) {
                    CommandSwerveDrivetrain.getInstance().addVisionMeasurement(poseEstimate.pose, poseEstimate.timestampSeconds);
                    SmartDashboard.putBoolean("Vision/" + camera.getName() + "/Has Data", true);
                    SmartDashboard.putNumber("Vision/" + camera.getName() + "/Tag Count", poseEstimate.tagCount);
                    maxTagCount = Math.max(maxTagCount, poseEstimate.tagCount);
                } else {
                    SmartDashboard.putBoolean("Vision/" + camera.getName() + "/Has Data", false);
                    SmartDashboard.putNumber("Vision/" + camera.getName() + "/TagCount", 0);
                }   
        }
        SmartDashboard.putString("Vision/Megatag Mode", getMegaTagMode().toString());
        SmartDashboard.putNumber("Vision/IMU Mode", imuMode);
    }
}

//TODO: fix after generate swerve code
package com.INT.robot.subsystems.Turret;

import java.util.Optional;

import com.INT.robot.Robot;
import com.INT.robot.constants.Field;
import com.INT.robot.constants.Motors.TurretConstants;
import com.INT.robot.constants.Settings;
import com.INT.robot.subsystems.Swerve.CommandSwerveDrivetrain;
import com.INT.robot.util.ChineseRemainderTheorem;
import com.INT.robot.util.SysId;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;

public class Turret extends SubsystemBase {

    private TalonFX turretMotor;
    private CANcoder turretMotorEncoderTurret; // 60T
    private CANcoder turretMotorEncoderEncoder; // 43T

    private MotionMagicVoltage targetPosition;

    private boolean aimingAtGoal = true;
    private boolean isSeeded = false;
    private boolean isFerrying = false;

    private CommandSwerveDrivetrain drivetrain;

    // private static final Turret instance;

    // static {
    //     instance = new Turret();
    // }

    // public static Turret getInstance() {
    //     return instance;
    // }

    public Turret() {
        turretMotor = new TalonFX(TurretConstants.TURRET_MOTOR, Settings.upper);
        turretMotor.getConfigurator();
        turretMotor.setNeutralMode(NeutralModeValue.Brake);

        turretMotorEncoderTurret = new CANcoder(TurretConstants.TURRET_ENCODER_TURRET, Settings.upper);

        turretMotorEncoderEncoder = new CANcoder(TurretConstants.TURRET_ENCODER_ENCODER, Settings.upper);

        targetPosition = new MotionMagicVoltage(0);
        setCurrentPosition();

        Optional.empty();
    }

    public void setDrivetrain(CommandSwerveDrivetrain drivetrain) {
        this.drivetrain = drivetrain;
    }

    public double getAbsoluteTurretRotations() {
        double encoderEncoder = turretMotorEncoderEncoder.getPosition().getValueAsDouble();
        double turretEncoder = turretMotorEncoderTurret.getPosition().getValueAsDouble();

        // return ChineseRemainderTheorem.getTurretRotations(
        // 60,
        // 43,
        // 14,
        // 136,
        // turretEncoder,
        // encoderEncoder);
        return ChineseRemainderTheorem.getTurretRotations(
                turretEncoder,
                encoderEncoder,
                60,
                43,
                136);
    } // returns rotations

    public Rotation2d getAbsoluteTurretRotationsRot2d() {
        return Rotation2d.fromRotations(getAbsoluteTurretRotations());
    } // returns rotation2d

    public void reset() {
        turretMotorEncoderTurret.getConfigurator().setPosition(0);
        turretMotorEncoderEncoder.getConfigurator().setPosition(0);
        setCurrentPosition();
    }

    public boolean isInTopSide() {
        return getRobotPose().getX() <= Field.WIDTH / 2.0;
    }

    public void toggleAimAtTarget() {
        aimingAtGoal = !aimingAtGoal;
    }

    public boolean robotIsOnAllianceSide() {
        Pose2d pose = drivetrain.getPose();
        return Robot.isBlue()
                ? pose.getX() < Units.inchesToMeters(182.11)
                : pose.getX() > Units.inchesToMeters(469.11);
    }

    public Translation2d getGoalPosition() {
        if (aimingAtGoal && robotIsOnAllianceSide() && Robot.isHubActive()) {
            isFerrying = false;
            return Field.hubCenter;
        } else {
            isFerrying = true;
            return isInTopSide() ? Field.topFerry : Field.bottomFerry;
        }
    }

    public Pose2d getRobotPose() {
        return drivetrain != null ? drivetrain.getPose() : new Pose2d();
    }

    public double getTargetPosition(Pose2d robotPose) {
        Translation2d goalPosition = getGoalPosition();

        double dx = goalPosition.getX() - robotPose.getX();
        double dy = goalPosition.getY() - robotPose.getY();

        if (isFerrying) {
            double vx = drivetrain.getChassisSpeeds().vxMetersPerSecond;
            double shootWhileMovingFactor = 0.2;

            dx += vx * shootWhileMovingFactor;
        }
        //ferry while moving 

        // Field-relative angle
        double fieldAngle = Math.atan2(dy, dx);

        // Convert to robot-relative
        return MathUtil.angleModulus(fieldAngle - robotPose.getRotation().getRadians());
    }

    public void setCurrentPosition() {
        turretMotor.getConfigurator().setPosition(getAbsoluteTurretRotations());
    }

    public void setMagicPosition() {

        // rotations
        double currentRotations = turretMotor.getPosition().getValueAsDouble();

        // rotations
        double targetRotations = getTargetPosition(getRobotPose()) / (2.0 * Math.PI);

        // Wrap
        double wrappedTarget = MathUtil.inputModulus(
                targetRotations,
                currentRotations - Settings.Turret.Constants.TURRET_MIN_ROTATIONS,
                currentRotations + Settings.Turret.Constants.TURRET_MAX_ROTATIONS);

        // Clamp
        wrappedTarget = MathUtil.clamp(
                wrappedTarget,
                Settings.Turret.Constants.TURRET_MIN_ROTATIONS,
                Settings.Turret.Constants.TURRET_MAX_ROTATIONS);

        turretMotor.setControl(
                targetPosition
                        .withPosition(wrappedTarget)
                        .withEnableFOC(true));
    }

    public boolean atTarget(double toleranceRadians) {
        double error = MathUtil.angleModulus(
                (turretMotor.getPosition().getValueAsDouble() * 2.0 * Math.PI)
                        - getTargetPosition(getRobotPose()));

        return Math.abs(error) < toleranceRadians;
    }

    public boolean atTarget() {
        return atTarget(Settings.Turret.Constants.toleranceRadians);
    }

    public double getPos() {
        return turretMotor.getPosition().getValueAsDouble();
    }

    @Override
    public void periodic() {
        SmartDashboard.putBoolean("Turret/atTarget", atTarget(Settings.Turret.Constants.toleranceRadians));
        SmartDashboard.putNumber("Turret/targetPosition", getTargetPosition(getRobotPose()));
        SmartDashboard.putNumber("Turret/rotationPosition", getAbsoluteTurretRotations());
        SmartDashboard.putNumber("Turret/localPos", getPos());
        SmartDashboard.putBoolean("Turret/aimingAtGoal", aimingAtGoal);

        if (!isSeeded && Math.abs(turretMotor.getRotorVelocity().getValueAsDouble()) < 1) {
            setCurrentPosition();
            isSeeded = true;
        }
    }   

    public SysIdRoutine getSysIdRoutine() {
        return SysId.getRoutine(
                2,
                6,
                "Turret",
                voltage -> setVoltageOverride(Optional.of(voltage)),
                () -> this.turretMotor.getPosition().getValueAsDouble(),
                () -> this.turretMotor.getVelocity().getValueAsDouble(),
                () -> this.turretMotor.getMotorVoltage().getValueAsDouble(),
                this);
    }

    private void setVoltageOverride(Optional<Double> volts) {
    }
}
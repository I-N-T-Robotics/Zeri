package com.INT.robot.subsystems.Turret;

import com.INT.robot.Robot;
import com.INT.robot.constants.Motors.TurretConstants;
import com.INT.robot.util.ChineseRemainderTheorem;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Turret extends SubsystemBase {

    private static final Turret instance;

    static {
        instance = new Turret();
    }
    
    public static Turret getInstance() {
        return instance;
    }

    private TalonFX turretMotor;
    private CANcoder turretMotorEncoderTurret; //60T
    private CANcoder turretMotorEncoderEncoder; //43T
    private DigitalInput magneticReset;

    private MotionMagicVoltage targetPosition;
    
    private boolean lastResetState = false;

    public Turret() {
        turretMotor = new TalonFX(TurretConstants.TURRET_MOTOR, "Yuumi");
        turretMotor.getConfigurator().apply(TurretConstants.turretConfigs);

        turretMotorEncoderTurret = new CANcoder(TurretConstants.TURRET_ENCODER_TURRET, "Yuumi");
        turretMotorEncoderTurret.getConfigurator().apply(TurretConstants.turretCANcoderConfigs);

        turretMotorEncoderEncoder = new CANcoder(TurretConstants.TURRET_ENCODER_ENCODER, "Yuumi");
        turretMotorEncoderEncoder.getConfigurator().apply(TurretConstants.turretCANcoderConfigs);

        magneticReset = new DigitalInput(0);

        targetPosition = new MotionMagicVoltage(0);
    }

    public double getAbsoluteTurretRotations() {
        double encoderEncoder = turretMotorEncoderEncoder.getPosition().getValueAsDouble();
        double turretEncoder = turretMotorEncoderTurret.getPosition().getValueAsDouble();

        return ChineseRemainderTheorem.getTurretRotations(
            turretEncoder,
            encoderEncoder,
            60,
            43,
            136
        );
    }

    public boolean passedReset() {
        return magneticReset.get();
    }

    public void reset() {
        turretMotorEncoderTurret.getConfigurator().setPosition(0);
        turretMotorEncoderEncoder.getConfigurator().setPosition(0);
    }

    public Translation3d getGoalPosition() {
        if (Robot.isBlue()) {
            return new Translation3d(Units.inchesToMeters(182.11), Units.inchesToMeters(158.84), Units.inchesToMeters(72));
        } else {
            return new Translation3d(Units.inchesToMeters(469.11), Units.inchesToMeters(158.84), Units.inchesToMeters(72)); 
        }
    }

    public Pose2d getRobotPose() {
        return CommandSwerveDrivetrain.getInstance().getPose();
    }

    public double getTargetPosition(Pose2d robotPose) {
        Translation3d goalPosition = getGoalPosition();
        return Math.atan2((goalPosition.getY() - robotPose.getY()),
        (goalPosition.getX() - robotPose.getX()));
    }

    public void setCurrentPosition() {
        turretMotor.getConfigurator().setPosition(getAbsoluteTurretRotations());
    }

    public void setMagicPosition() {
        double targetRotations = getTargetPosition(getRobotPose()) / (2 * Math.PI);
        turretMotor.setControl(targetPosition.withPosition(targetRotations).withEnableFOC(true));
    }

    @Override
    public void periodic() {
        // TODO: make into command later
        // setCurrentPosition();
        // setMagicPosition();

        boolean current = passedReset();

        if (current && !lastResetState) {
            reset();
        }
    
        lastResetState = current;
    }
}
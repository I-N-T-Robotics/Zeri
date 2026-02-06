// package com.INT.robot.subsystems.Turret;

// import com.INT.robot.Robot;
// import com.INT.robot.constants.Field;
// import com.INT.robot.constants.Motors.TurretConstants;
// import com.INT.robot.util.ChineseRemainderTheorem;
// import com.ctre.phoenix6.controls.MotionMagicVoltage;
// import com.ctre.phoenix6.hardware.CANcoder;
// import com.ctre.phoenix6.hardware.TalonFX;

// import edu.wpi.first.math.MathUtil;
// import edu.wpi.first.math.geometry.Pose2d;
// import edu.wpi.first.math.geometry.Translation3d;
// import edu.wpi.first.math.util.Units;
// import edu.wpi.first.wpilibj.DigitalInput;
// import edu.wpi.first.wpilibj2.command.SubsystemBase;

// public class Turret extends SubsystemBase {

//     private static final Turret instance;

//     static {
//         instance = new Turret();
//     }

//     public static Turret getInstance() {
//         return instance;
//     }

//     private TalonFX turretMotor;
//     private CANcoder turretMotorEncoderTurret; // 60T
//     private CANcoder turretMotorEncoderEncoder; // 43T
//     private DigitalInput magneticReset;

//     private MotionMagicVoltage targetPosition;

//     private boolean lastResetState = false;
//     private boolean aimingAtGoal = true;
//     private boolean inTopSide = true;

//     public Turret() {
//         turretMotor = new TalonFX(TurretConstants.TURRET_MOTOR, "Yuumi");
//         turretMotor.getConfigurator().apply(TurretConstants.turretConfigs);

//         turretMotorEncoderTurret = new CANcoder(TurretConstants.TURRET_ENCODER_TURRET, "Yuumi");
//         turretMotorEncoderTurret.getConfigurator().apply(TurretConstants.turretCANcoderConfigs);

//         turretMotorEncoderEncoder = new CANcoder(TurretConstants.TURRET_ENCODER_ENCODER, "Yuumi");
//         turretMotorEncoderEncoder.getConfigurator().apply(TurretConstants.turretCANcoderConfigs);

//         magneticReset = new DigitalInput(0);

//         targetPosition = new MotionMagicVoltage(0);
//         setCurrentPosition();
//     }

//     public double getAbsoluteTurretRotations() {
//         double encoderEncoder = turretMotorEncoderEncoder.getPosition().getValueAsDouble();
//         double turretEncoder = turretMotorEncoderTurret.getPosition().getValueAsDouble();

//         return ChineseRemainderTheorem.getTurretRotations(
//                 60,
//                 43,
//                 14,
//                 136,
//                 turretEncoder,
//                 encoderEncoder);
//     }

//     public boolean passedReset() {
//         return magneticReset.get();
//     }

//     public void reset() {
//         turretMotorEncoderTurret.getConfigurator().setPosition(0);
//         turretMotorEncoderEncoder.getConfigurator().setPosition(0);
//         setCurrentPosition();
//     }

//     public boolean isInTopSide() {
//         inTopSide = getRobotPose().getX() <= Field.WIDTH / 2.0;
//         return inTopSide;
//     }

//     public void toggleAimAtTarget() {
//         aimingAtGoal = !aimingAtGoal;
//     }

//     public Translation3d getGoalPosition() {
//         if (Robot.isBlue() && aimingAtGoal) {
//             return Field.BlueGoal;
//         } else if (Robot.isBlue() && inTopSide && !aimingAtGoal) {
//             return Field.BlueTopFerry;
//         } else if (Robot.isBlue() && !inTopSide && !aimingAtGoal) {
//             return Field.BlueBottomFerry;
//         } else if (!Robot.isBlue() && aimingAtGoal) {
//             return Field.transformToOppositeAlliance(Field.BlueGoal);
//         } else if (!Robot.isBlue() && inTopSide) {
//             return Field.transformToOppositeAlliance(Field.BlueTopFerry);
//         } else {
//             return Field.transformToOppositeAlliance(Field.BlueBottomFerry);
//         }
//     }

// //    public Pose2d getRobotPose() {
// //        return CommandSwerveDrivetrain.getInstance().getPose();
// //    }

//     public double getTargetPosition(Pose2d robotPose) {
//         Translation3d goalPosition = getGoalPosition();
    
//         double dx = goalPosition.getX() - robotPose.getX();
//         double dy = goalPosition.getY() - robotPose.getY();
    
//         // Field-relative angle
//         double fieldAngle = Math.atan2(dy, dx);
    
//         // Convert to robot-relative
//         return fieldAngle - robotPose.getRotation().getRadians();
//     }

//     public void setCurrentPosition() {
//         turretMotor.getConfigurator().setPosition(getAbsoluteTurretRotations());
//     }

//     private double getShortestPathRotations(double targetAngleRadians) {
//         double currentRotations = turretMotor.getPosition().getValueAsDouble();

//         double targetRotations = targetAngleRadians / (2.0 * Math.PI);

//         double wrappedTarget = MathUtil.inputModulus(
//                 targetRotations,
//                 currentRotations - 0.5,
//                 currentRotations + 0.5);

//         return wrappedTarget;
//     }

//     public void setMagicPosition() {
//         double robotRelativeAngle = getTargetPosition(getRobotPose());
    
//         robotRelativeAngle = MathUtil.angleModulus(robotRelativeAngle);
    
//         double targetRotations = getShortestPathRotations(robotRelativeAngle);
    
//         turretMotor.setControl(
//             targetPosition
//                 .withPosition(targetRotations)
//                 .withEnableFOC(true)
//         );
//     }

//     public boolean atTarget(double toleranceRadians) {
//         double error =
//             MathUtil.angleModulus(
//                 (turretMotor.getPosition().getValueAsDouble() * 2.0 * Math.PI)
//                 - getTargetPosition(getRobotPose())
//             );
    
//         return Math.abs(error) < toleranceRadians;
//     }    

//     @Override
//     public void periodic() {
//         // TODO: make into command later
//         // setMagicPosition();

//         isInTopSide();

//         boolean current = passedReset();

//         if (current && !lastResetState) {
//             reset();
//         }

//         lastResetState = current;
//     }
// }
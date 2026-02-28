package com.INT.robot;

import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.RotationsPerSecond;

import com.INT.robot.commands.auton.DoNothingAuton;
import com.INT.robot.commands.hood.HoodAim;
import com.INT.robot.commands.hood.HoodReset;
import com.INT.robot.commands.intake.IntakeIntake;
import com.INT.robot.commands.intake.IntakeOuttake;
import com.INT.robot.commands.intake.IntakeStop;
import com.INT.robot.commands.shooter.ShooterShoot;
import com.INT.robot.commands.shooter.ShooterStart;
import com.INT.robot.commands.shooter.ShooterStop;
import com.INT.robot.commands.spindexer.SpindexerStart;
import com.INT.robot.commands.spindexer.SpindexerStop;
import com.INT.robot.commands.swerve.SwerveXMode;
import com.INT.robot.commands.turret.AimTurret;
import com.INT.robot.constants.Field;
import com.INT.robot.subsystems.Hood.Hood;
import com.INT.robot.subsystems.Intake.Intake;
import com.INT.robot.subsystems.Shooter.Shooter;
import com.INT.robot.subsystems.Spindexer.Spindexer;
import com.INT.robot.subsystems.Swerve.CommandSwerveDrivetrain;
import com.INT.robot.subsystems.Swerve.TunerConstants;
import com.INT.robot.subsystems.Turret.Turret;
import com.INT.robot.subsystems.Vision.LimelightVision;
import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

public class RobotContainer {
    private double MaxSpeed = TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed
    private double MaxAngularRate = RotationsPerSecond.of(1).in(RadiansPerSecond); // 3/4 of a rotation per second max angular velocity

    /* Setting up bindings for necessary control of the swerve drive platform */
    private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
            .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors
            
    private final CommandXboxController driver = new CommandXboxController(0);

    public final CommandSwerveDrivetrain drivetrain = TunerConstants.createDrivetrain();
    private final Turret turret = new Turret();
    private final Shooter shooter = new Shooter();
    private final Hood hood = new Hood();
    private final Intake intake = new Intake();
    private final Spindexer spindexer = new Spindexer();
    private final LimelightVision limelightVision = new LimelightVision();

    // Gamepads
    
    // Subsystem

    // Autons
    private static SendableChooser<Command> autonChooser = new SendableChooser<>();

    // Robot container

    public RobotContainer() {
        configureDefaultCommands();
        configureButtonBindings();
        configureAutons();

        drivetrain.setTurret(turret);
        turret.setDrivetrain(drivetrain);
        limelightVision.setDrivetrain(drivetrain);

        SmartDashboard.putData("Field", Field.FIELD2D);
    }

    /****************/
    /*** DEFAULTS ***/
    /****************/

    private void configureDefaultCommands() {
        turret.setDefaultCommand(new AimTurret(turret));
        shooter.setDefaultCommand(new ShooterShoot(shooter, drivetrain, turret));
        hood.setDefaultCommand(new HoodAim(hood, turret, drivetrain));
        
        drivetrain.setDefaultCommand(
        drivetrain.applyRequest(() ->
            drive.withVelocityX((-driver.getLeftY() * MaxSpeed)) // Drive forward with negative Y (forward)
                .withVelocityY((-driver.getLeftX() * MaxSpeed)) // Drive left with negative X (left)
                .withRotationalRate((-driver.getRightX() * MaxAngularRate)) // Drive counterclockwise with negative X (left)
        )
    ); 
    }

    /***************/
    /*** BUTTONS ***/
    /***************/

    private void configureButtonBindings() {
        //start intake
        driver.rightTrigger()
            .onTrue(new IntakeIntake(intake));

        //stop intake
        driver.rightBumper()
            .onTrue(new IntakeStop(intake));

        //outtake
        driver.leftTrigger()
            .onTrue(new IntakeOuttake(intake));

        //spindexer to shoot
        driver.y()
            .onTrue(new SpindexerStart(spindexer));

        //stop spindexer
        driver.a()
            .onTrue(new SpindexerStop(spindexer));

        //X-mode
        driver.b()
            .whileTrue(new SwerveXMode(drivetrain));

        //reset hood
        driver.start()
            .onTrue(new HoodReset(hood));

        //stop shooter
        driver.x()
            .onTrue(new ShooterStop(shooter));

        //start shooter
        driver.povRight()
            .onTrue(new ShooterStart(shooter));
    }

    /**************/
    /*** AUTONS ***/
    /**************/

    public void configureAutons() {
        autonChooser.setDefaultOption("Do Nothing", new DoNothingAuton());

        SmartDashboard.putData("Autonomous", autonChooser);
    }

    public Command getAutonomousCommand() {
        return autonChooser.getSelected();
    }
}

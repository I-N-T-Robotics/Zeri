package com.INT.robot;

import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.RotationsPerSecond;

import com.INT.robot.commands.auton.DoNothingAuton;
import com.INT.robot.commands.shooter.ShooterShoot;
import com.INT.robot.commands.turret.AimTurret;
import com.INT.robot.subsystems.Shooter.Shooter;
import com.INT.robot.subsystems.Swerve.CommandSwerveDrivetrain;
import com.INT.robot.subsystems.Swerve.TunerConstants;
import com.INT.robot.subsystems.Turret.Turret;
import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandPS5Controller;

public class RobotContainer {
    private double MaxSpeed = TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed
    private double MaxAngularRate = RotationsPerSecond.of(1).in(RadiansPerSecond); // 3/4 of a rotation per second max angular velocity

    /* Setting up bindings for necessary control of the swerve drive platform */
    private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
            .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors
            
    private final CommandPS5Controller driver = new CommandPS5Controller(0);

    public final CommandSwerveDrivetrain drivetrain = TunerConstants.createDrivetrain();
    private final Turret turret;
    private final Shooter shooter;

    // Gamepads
    
    // Subsystem

    // Autons
    private static SendableChooser<Command> autonChooser = new SendableChooser<>();

    // Robot container

    public RobotContainer() {
        turret = new Turret();
        shooter = new Shooter();
        
        configureDefaultCommands();
        configureButtonBindings();
        configureAutons();
    }

    /****************/
    /*** DEFAULTS ***/
    /****************/

    private void configureDefaultCommands() {
        turret.setDefaultCommand(new AimTurret(turret));
        shooter.setDefaultCommand(new ShooterShoot(shooter));
        
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

    private void configureButtonBindings() {}

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

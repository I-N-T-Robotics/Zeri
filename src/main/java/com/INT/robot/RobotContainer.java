package com.INT.robot;

import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.RotationsPerSecond;

import com.INT.robot.commands.auton.DoNothingAuton;
import com.INT.robot.commands.hood.HoodAim;
import com.INT.robot.commands.hood.HoodReset;
import com.INT.robot.commands.intake.DeployIntake;
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
import com.INT.robot.commands.turret.ResetTurret;
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
import com.pathplanner.lib.auto.NamedCommands;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;

public class RobotContainer {
    private double MaxSpeed = TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed
    private double MaxAngularRate = RotationsPerSecond.of(1).in(RadiansPerSecond); // 3/4 of a rotation per second max angular velocity

    /* Setting up bindings for necessary control of the swerve drive platform */
    private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
            .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors
            
    //Gamepads
    private final CommandXboxController driver = new CommandXboxController(0);
    private final CommandXboxController testControls = new CommandXboxController(1);

    //subsystems
    public final CommandSwerveDrivetrain drivetrain = TunerConstants.createDrivetrain();
    private final Turret turret = new Turret();
    private final Shooter shooter = new Shooter();
    private final Hood hood = new Hood();
    private final Intake intake = new Intake();
    private final Spindexer spindexer = new Spindexer();
    private final LimelightVision limelightVision = new LimelightVision(drivetrain);

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

        NamedCommands.registerCommand("StartIntake", new IntakeIntake(intake));
        NamedCommands.registerCommand("DeployIntake", new DeployIntake(intake));
        NamedCommands.registerCommand("StopIntake", new IntakeStop(intake));
        NamedCommands.registerCommand("StartSpindexer", new SpindexerStart(spindexer));
        NamedCommands.registerCommand("StopSpindexer", new SpindexerStop(spindexer));
        NamedCommands.registerCommand("StartShooter", new ShooterStart(shooter));
        NamedCommands.registerCommand("StopShooter", new ShooterStop(shooter));
        NamedCommands.registerCommand("AimTurret", new AimTurret(turret));
        NamedCommands.registerCommand("XMode", new SwerveXMode(drivetrain));
        NamedCommands.registerCommand("resetHood", new HoodReset(hood));
        NamedCommands.registerCommand("resetTurret", new ResetTurret(turret));
        NamedCommands.registerCommand("wait", new WaitCommand(20));

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
            drive.withVelocityX((-testControls.getLeftY() * MaxSpeed)) // Drive forward with negative Y (forward)
                .withVelocityY((-testControls.getLeftX() * MaxSpeed)) // Drive left with negative X (left)
                .withRotationalRate((-testControls.getRightX() * MaxAngularRate)) // Drive counterclockwise with negative X (left)
        )
        ); 

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

        //start spindexer, start shooter
        driver.y()
            .onTrue(new SpindexerStart(spindexer))
            .onTrue(new ShooterStart(shooter));

        //stop spindexer, stop shooter
        driver.a()
            .onTrue(new SpindexerStop(spindexer))
            .onTrue(new ShooterStop(shooter));

        //X-mode
        driver.b()
            .whileTrue(new SwerveXMode(drivetrain));

        //reset hood
        driver.start()
            .onTrue(new HoodReset(hood));


        testControls.y()
            .whileTrue(drivetrain.sysIdDynamic(Direction.kForward));

        testControls.a()
            .whileTrue(drivetrain.sysIdDynamic(Direction.kReverse));

        testControls.x()
            .whileTrue(drivetrain.sysIdQuasistatic(Direction.kForward));

        testControls.b()
            .whileTrue(drivetrain.sysIdQuasistatic(Direction.kReverse));
    }

    /**************/
    /*** AUTONS ***/
    /**************/

    public void configureAutons() {
        autonChooser.setDefaultOption("Do Nothing", new DoNothingAuton());

        SmartDashboard.putData("Autonomous", autonChooser);
    }

    public void configureSysids() {
        SysIdRoutine turretSysid = turret.getSysIdRoutine();
        autonChooser.addOption("SysID Turret Dynamic Forward", turretSysid.dynamic(Direction.kForward));
        autonChooser.addOption("SysID Turret Dynamic Backwards", turretSysid.dynamic(Direction.kReverse));
        autonChooser.addOption("SysID Turret Quasi Forwards", turretSysid.quasistatic(Direction.kForward));
        autonChooser.addOption("SysID Turret Quasi Backwards", turretSysid.quasistatic(Direction.kReverse));
    }

    public Command getAutonomousCommand() {
        return autonChooser.getSelected();
    }
}

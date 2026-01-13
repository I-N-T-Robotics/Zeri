package com.stuypulse.robot.commands;

import com.stuypulse.robot.subsystems.Drivetrain;
import com.stuypulse.stuylib.streams.IStream;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class DrivetrainDriveDistance extends CommandBase{
    public final double distance;
    public final Drivetrain drivetrain;
    double rightSpeed;
    double leftSpeed;



public DrivetrainDriveDistance(Drivetrain drivetrain, double distance) {
    this.drivetrain = drivetrain; 
    this.distance = distance; 

    // this.speed = IStream.create((
    //     () -> driver.getRightY() - driver.getLeftY()))
    //     .filtered(
    //     x -> SLMath.

    //     )
    addRequirements(drivetrain); 
}

@Override
public void execute(){
drivetrain.tankDrive(1, 1);
}

@Override
public boolean isFinished(){
return (drivetrain.getLeftDistance() > Units.inchesToMeters(distance));
}
@Override 
public void end(boolean interrupted){
    drivetrain.stop();
}
}



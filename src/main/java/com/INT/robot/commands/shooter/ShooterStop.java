package com.INT.robot.commands.shooter;

import com.INT.robot.subsystems.Shooter.Shooter;

import edu.wpi.first.wpilibj2.command.Command;

public class ShooterStop extends Command {
    private final Shooter shooter;

    public ShooterStop(Shooter shooter) {
        this.shooter = shooter;
        addRequirements(shooter);
    }

    @Override
    public void execute() {
        shooter.setShooting(false);
    }

    @Override
    public boolean isFinished() {
        return shooter.getRightMotorRPM() < 5;
    }

    @Override
    public void end(boolean interrupted) {
    }
}
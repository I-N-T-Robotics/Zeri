package com.INT.robot.commands.shooter;

import com.INT.robot.subsystems.Shooter.Shooter;

import edu.wpi.first.wpilibj2.command.Command;

public class ShooterStart extends Command {
    private final Shooter shooter;

    public ShooterStart(Shooter shooter) {
        this.shooter = shooter;
        addRequirements(shooter);
    }

    @Override
    public void execute() {
        shooter.setShooting(true);
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void end(boolean interrupted) {
    }
}
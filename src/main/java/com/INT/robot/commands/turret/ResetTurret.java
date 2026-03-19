package com.INT.robot.commands.turret;

import com.INT.robot.subsystems.Turret.Turret;

import edu.wpi.first.wpilibj2.command.Command;

public class ResetTurret extends Command {
    private final Turret turret;
    
    public ResetTurret(Turret turret) {
        this.turret = turret;
        addRequirements(turret);
    }

    @Override
    public void execute() {
        turret.reset();
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void end(boolean interrupted) {
    }
}
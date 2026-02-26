package com.INT.robot.commands.turret;

import com.INT.robot.subsystems.Turret.Turret;

import edu.wpi.first.wpilibj2.command.Command;

public class AimTurret extends Command {
    private final Turret turret;

    public AimTurret(Turret turret) {
        this.turret = turret;
        addRequirements(turret);
    }

    @Override
    public void execute() {
        turret.setMagicPosition();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
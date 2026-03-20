package com.INT.robot.commands.spindexer;

import com.INT.robot.subsystems.Spindexer.Spindexer;
import com.INT.robot.subsystems.Turret.Turret;

import edu.wpi.first.wpilibj2.command.Command;

public class SpindexerStart extends Command {
    private final Spindexer spindexer;
    private final Turret turret;

    public SpindexerStart(Spindexer spindexer, Turret turret) {
        this.spindexer = spindexer;
        this.turret = turret;
        addRequirements(spindexer, turret);
    }

    @Override
    public void execute() {
        spindexer.startTransition();
        if (spindexer.transitionAtSpeed() && turret.atTarget()) {
            spindexer.startSpindexer();
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        spindexer.stopSpindexer();
        spindexer.stopTransition();
    }
}
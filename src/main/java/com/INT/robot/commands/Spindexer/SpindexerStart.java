package com.INT.robot.commands.spindexer;

import com.INT.robot.subsystems.Spindexer.Spindexer;

import edu.wpi.first.wpilibj2.command.Command;

public class SpindexerStart extends Command {
    private final Spindexer spindexer;

    public SpindexerStart(Spindexer spindexer) {
        this.spindexer = spindexer;
        addRequirements(spindexer);
    }

    @Override
    public void execute() {
        spindexer.startSpindexer();
        spindexer.startTransition();
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
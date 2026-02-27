package com.INT.robot.commands.Spindexer;

import com.INT.robot.subsystems.Spindexer.Spindexer;

import edu.wpi.first.wpilibj2.command.Command;

public class SpindexerStop extends Command {
    private final Spindexer spindexer;

    public SpindexerStop(Spindexer spindexer) {
        this.spindexer = spindexer;
        addRequirements(spindexer);
    }

    @Override
    public void execute() {
        spindexer.stopSpindexer();
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
    }
}
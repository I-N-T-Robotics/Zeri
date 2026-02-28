package com.INT.robot.commands.hood;

import com.INT.robot.subsystems.Hood.Hood;

import edu.wpi.first.wpilibj2.command.Command;

public class HoodReset extends Command {
    private final Hood hood;
    
    public HoodReset(Hood hood) {
        this.hood = hood;
        addRequirements(hood);
    }

    @Override
    public void execute() {
        hood.stowHood();
    }

    @Override
    public boolean isFinished() {
        return hood.hoodAtPosition();
    }

    @Override
    public void end(boolean interrupted) {
        hood.resetHood();
    }
}
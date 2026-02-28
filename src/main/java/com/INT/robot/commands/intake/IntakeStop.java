package com.INT.robot.commands.intake;

import com.INT.robot.subsystems.Intake.Intake;

import edu.wpi.first.wpilibj2.command.Command;

public class IntakeStop extends Command {
    private final Intake intake;

    public IntakeStop(Intake intake) {
        this.intake = intake;
        addRequirements(intake);
    }

    @Override
    public void execute() {
        intake.stopIntake();
    }

    @Override
    public void end(boolean interrupted) {
    }
}
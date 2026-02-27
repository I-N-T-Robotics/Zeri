package com.INT.robot.commands.intake;

import com.INT.robot.subsystems.Intake.Intake;

import edu.wpi.first.wpilibj2.command.Command;

public class IntakeIntake extends Command {
    private final Intake intake;

    public IntakeIntake(Intake intake) {
        this.intake = intake;
        addRequirements(intake);
    }

    @Override
    public void execute() {
        intake.intake();
    }

    @Override
    public void end(boolean interrupted) {
        intake.stopIntake();
    }
}
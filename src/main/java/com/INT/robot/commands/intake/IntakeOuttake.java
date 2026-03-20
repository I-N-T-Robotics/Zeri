package com.INT.robot.commands.intake;

import com.INT.robot.subsystems.Intake.Intake;
import com.INT.robot.subsystems.Spindexer.Spindexer;

import edu.wpi.first.wpilibj2.command.Command;

public class IntakeOuttake extends Command {
    private final Intake intake;
    private final Spindexer spindexer;

    public IntakeOuttake(Intake intake, Spindexer spindexer) {
        this.intake = intake;
        this.spindexer = spindexer;
        addRequirements(intake, spindexer);
    }

    @Override
    public void execute() {
        intake.outtake();
        spindexer.reverseSpindexer();
    }

    @Override
    public void end(boolean interrupted) {
        intake.stopIntake();
        spindexer.stopSpindexer();
    }
}
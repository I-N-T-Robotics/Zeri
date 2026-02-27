package com.INT.robot.commands.intake;

import com.INT.robot.subsystems.Intake.Intake;

import edu.wpi.first.wpilibj2.command.Command;

public class UndeployIntake extends Command {
    private final Intake intake;

    public UndeployIntake(Intake intake) {
        this.intake = intake;
        addRequirements(intake);
    }

    @Override
    public void execute() {
        intake.undeploy();
    }

    @Override
    public boolean isFinished() {
        return intake.intakeAtUndeployPosition();
    }

    @Override
    public void end(boolean interrupted) {
        intake.stopIntakePivot();
    }
}
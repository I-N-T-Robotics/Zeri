package com.INT.robot.commands.intake;

import com.INT.robot.subsystems.Intake.Intake;

import edu.wpi.first.wpilibj2.command.Command;

public class DeployIntake extends Command {
    private final Intake intake;

    public DeployIntake(Intake intake) {
        this.intake = intake;
        addRequirements(intake);
    }

    @Override
    public void execute() {
        intake.deploy();
    }

    @Override
    public boolean isFinished() {
        return intake.intakeAtDeployPosition();
    }

    @Override
    public void end(boolean interrupted) {
        intake.stopIntakePivot();
    }
}
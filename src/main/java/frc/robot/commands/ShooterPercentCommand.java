// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakeAndShooterSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class ShooterPercentCommand extends Command {
  /** Creates a new ShooterPercentCommand. */

private IntakeAndShooterSubsystem intakeAndShooterSubsystem;

private double percent = 0.9;

  public ShooterPercentCommand(IntakeAndShooterSubsystem intakeAndShooterSubsystem) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.intakeAndShooterSubsystem = intakeAndShooterSubsystem;
    addRequirements(intakeAndShooterSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    intakeAndShooterSubsystem.runShooterPercent(percent);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    intakeAndShooterSubsystem.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}

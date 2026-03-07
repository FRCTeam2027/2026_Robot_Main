// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakeAndShooterSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class ShooterVelocityCommand extends Command {
  /** Creates a new ShooterVelocityCommand. */
  private IntakeAndShooterSubsystem intakeAndShooterSubsystem = new IntakeAndShooterSubsystem();

  private double RPM = 3900;
  public ShooterVelocityCommand(IntakeAndShooterSubsystem intakeAndShooterSubsystem) {
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
    intakeAndShooterSubsystem.runShooterVelocity(RPM);
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

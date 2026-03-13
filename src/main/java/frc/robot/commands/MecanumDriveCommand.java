// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import com.ctre.phoenix6.hardware.Pigeon2;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.MecanumDriveSubsystem;
import java.util.function.DoubleSupplier;
import java.util.function.BooleanSupplier;
/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class MecanumDriveCommand extends Command {
  /** Creates a new MecanumDriveCommand. */
  
  private MecanumDriveSubsystem mecanumDriveSubsystem;
  
  //Suppler objects for the x, y and rotation to be used outside of the constricter. 
  private DoubleSupplier xSpeed;
  private DoubleSupplier ySpeed;
  private DoubleSupplier rotation;

  // Supplier objectss for the constants provided by the Limelight. 
  // These will be used to adjust the speed of the robot based on the distance from the target.
  private DoubleSupplier xTarget;
  private DoubleSupplier yTarget;
  private BooleanSupplier targettingButtonPressed;

  //DoubleSuppliers give constant updates, regular doubles do not
  public MecanumDriveCommand(MecanumDriveSubsystem mecanumDrive, 
    DoubleSupplier xSpeed, DoubleSupplier ySpeed, DoubleSupplier rotation,
    DoubleSupplier xTarget, DoubleSupplier yTarget, BooleanSupplier targettingButtonPressed) {
    // Use addRequirements() here to declare subsystem dependencies.
    mecanumDriveSubsystem = mecanumDrive;

    this.xSpeed = xSpeed;
    this.ySpeed = ySpeed;
    this.rotation = rotation;
    this.xTarget = xTarget;
    this.yTarget = yTarget;
    this.targettingButtonPressed = targettingButtonPressed;

    addRequirements(mecanumDriveSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled. (Every 20ms)
  @Override
  public void execute() {

    double xSpeedDeadband = MathUtil.applyDeadband(xSpeed.getAsDouble(), 0.07);
    double ySpeedDeadband = MathUtil.applyDeadband(ySpeed.getAsDouble(), 0.07);
    double rotationDeadband = MathUtil.applyDeadband(rotation.getAsDouble(), 0.07);

    double xTargetValue = xTarget.getAsDouble();
    double yTargetValue = yTarget.getAsDouble();

    if(targettingButtonPressed.getAsBoolean()) {
      // xTargetValue is the angle correction, yTargetValue is the distance correction. 
      // NOTE: This might be totally bogus and you might need to adjust the signs and coefficients based on testing
      mecanumDriveSubsystem.drive(0.0, yTargetValue, xTargetValue);
    } else {
      // If the targetting button isn't pressed, we should just drive based on the joystick inputs
      mecanumDriveSubsystem.drive(xSpeedDeadband, ySpeedDeadband, rotationDeadband);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
   mecanumDriveSubsystem.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}

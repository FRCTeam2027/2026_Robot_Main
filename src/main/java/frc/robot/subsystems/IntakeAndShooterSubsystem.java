// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.IntakeConstants;

public class IntakeAndShooterSubsystem extends SubsystemBase {
  /** Creates a new IntakeSubsystem. */
  private TalonFX shooterIntakeLeft = new TalonFX(IntakeConstants.shooterIntakeLeft);
  private TalonFX feederRight = new TalonFX(IntakeConstants.feederRight);

  private VelocityVoltage velocityRequest;

  public IntakeAndShooterSubsystem() {
    shooterIntakeLeft.getConfigurator().apply(IntakeConstants.configs);
    feederRight.getConfigurator().apply(IntakeConstants.configs);

    applyCurrentLimits();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  // Current Limiting for the motors

  public void applyCurrentLimits(){
    CurrentLimitsConfigs currentLimits = IntakeConstants.currentLimits;
    MotorOutputConfigs brakeConfigs = IntakeConstants.brakeValues;
    
    shooterIntakeLeft.getConfigurator().refresh(currentLimits);
    feederRight.getConfigurator().refresh(currentLimits);

    shooterIntakeLeft.getConfigurator().apply(currentLimits);
    feederRight.getConfigurator().apply(currentLimits);

    feederRight.getConfigurator().apply(brakeConfigs);
    shooterIntakeLeft.getConfigurator().apply(brakeConfigs);

    velocityRequest = new VelocityVoltage(0);
  }

  // functions for running motors

  public void runShooterVelocity(double RPS){
    shooterIntakeLeft.setControl(velocityRequest.withVelocity(-RPS*IntakeConstants.RPMtoRPS));
    feederRight.setControl(velocityRequest.withVelocity(RPS*IntakeConstants.RPMtoRPS));
  }

  public void runShooterPercent(double Percent){
    shooterIntakeLeft.set(-Percent);
    feederRight.set(Percent);
  }

  public void runIntakePercent(double Percent){
    shooterIntakeLeft.set(-Percent);
    feederRight.set(-Percent);
  }

  public void runIntakeVelocity(double RPM){
    shooterIntakeLeft.setControl(velocityRequest.withVelocity(RPM * IntakeConstants.RPMtoRPS));
    feederRight.setControl(velocityRequest.withVelocity(RPM * IntakeConstants.RPMtoRPS));
  }

  public void stop(){
    shooterIntakeLeft.stopMotor();
    feederRight.stopMotor();
  }

  //functions for printing/grabbing data

  public double getRPM(){
    return shooterIntakeLeft.getVelocity().getValueAsDouble() * IntakeConstants.RPStoRPM;
  }
}

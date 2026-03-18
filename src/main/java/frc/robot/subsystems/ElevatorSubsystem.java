// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVelocityVoltage;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.ElevatorConstants;
import frc.robot.Constants.IntakeConstants;

public class ElevatorSubsystem extends SubsystemBase {
  /** Creates a new ElevatorSubsystem. */

  private TalonFX elevatorMotor = new TalonFX(ElevatorConstants.elevatorMotor);
  private TalonFX clawAssist = new TalonFX(ElevatorConstants.clawAssist);
  private CANcoder clawEncoder = new CANcoder(ElevatorConstants.clawEncoder);
  DigitalInput topMagneticSwitch = new DigitalInput(ElevatorConstants.topMagneticSwitch);
  DigitalInput bottomBumperSwitch = new DigitalInput(ElevatorConstants.bottomBumperSwitch);
  private VelocityVoltage velocityRequest;
  private MotionMagicVelocityVoltage motionMagicVelocityRequest;
  private PositionVoltage positionRequest;

  public ElevatorSubsystem(){
    elevatorMotor.getConfigurator().apply(ElevatorConstants.configs);
    clawAssist.getConfigurator().apply(ElevatorConstants.configs);
    applyCurrentAndMotion();
  }

  public void applyCurrentAndMotion(){

    CurrentLimitsConfigs currentLimits = ElevatorConstants.currentLimits;
    MotorOutputConfigs brakeConfigs = ElevatorConstants.brakeValues;
    MotionMagicConfigs motionMagicConfigs = ElevatorConstants.clawMotionMagic;

    elevatorMotor.getConfigurator().refresh(currentLimits);
    clawAssist.getConfigurator().refresh(currentLimits);

    // elevatorMotor.getConfigurator().refresh(brakeConfigs);
    // clawAssist.getConfigurator().refresh(brakeConfigs);

    clawAssist.getConfigurator().refresh(motionMagicConfigs);

    elevatorMotor.getConfigurator().apply(currentLimits);
    clawAssist.getConfigurator().apply(currentLimits);

    clawAssist.getConfigurator().apply(brakeConfigs);
    elevatorMotor.getConfigurator().apply(brakeConfigs);

    clawAssist.getConfigurator().apply(motionMagicConfigs);

    velocityRequest = new VelocityVoltage(0);
    motionMagicVelocityRequest = new MotionMagicVelocityVoltage(0);
    positionRequest = new PositionVoltage(0);
  }
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }


  //negative returns to the up position, positive moves the elevator down
  public void elevatorControl(double RPS){
      
    if(topMagneticSwitch.get() == false && RPS > 0){
      stop();
    } else {
      elevatorMotor.setControl(velocityRequest.withVelocity(RPS*IntakeConstants.RPMtoRPS));
    }
  }

  public void elevatorRoutine(double RPS){
    while(topMagneticSwitch.get() == true){
      elevatorControl(RPS);
    } 

    elevatorMotor.setPosition(0.0);
    elevatorMotor.setControl(positionRequest.withPosition(-230));
  }
  public void stop(){
    elevatorMotor.stopMotor();
    clawAssist.stopMotor();
  }
}

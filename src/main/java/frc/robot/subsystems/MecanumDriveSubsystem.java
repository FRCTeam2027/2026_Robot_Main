// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.MecanumDriveConstants;
//import com.ctre.phoenix6.controls.Follower;

//import java.util.function.DoubleSupplier;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.hardware.TalonFX;

public class MecanumDriveSubsystem extends SubsystemBase {
  
  private TalonFX backLeft = new TalonFX(MecanumDriveConstants.backLeft);
  private TalonFX frontLeft = new TalonFX(MecanumDriveConstants.frontLeft);

  private TalonFX backRight = new TalonFX(MecanumDriveConstants.backRight);
  private TalonFX frontRight = new TalonFX(MecanumDriveConstants.frontRight);
  
  private final MecanumDrive m_robotDrive = 
                    new MecanumDrive(frontLeft::set, backLeft::set, frontRight::set, backRight::set);

  /** Creates a new MecanumDriveSubsystem. */
  public MecanumDriveSubsystem() {

    //Configures the motors
    frontLeft.getConfigurator().apply(MecanumDriveConstants.configs);
    backLeft.getConfigurator().apply(MecanumDriveConstants.configs);
    frontRight.getConfigurator().apply(MecanumDriveConstants.configs);
    backRight.getConfigurator().apply(MecanumDriveConstants.configs);
    
    //sets the current limits for the motors
    applyCurrentLimits();

    
  }



  public void applyCurrentLimits(){

    CurrentLimitsConfigs currentConfigs = MecanumDriveConstants.currentLimits;

    MotorOutputConfigs brakeConfigs = MecanumDriveConstants.brakeValues;

    frontLeft.getConfigurator().refresh(currentConfigs);
    backLeft.getConfigurator().refresh(currentConfigs);
    frontRight.getConfigurator().refresh(currentConfigs);
    backRight.getConfigurator().refresh(currentConfigs);

    frontLeft.getConfigurator().apply(currentConfigs);
    backLeft.getConfigurator().apply(currentConfigs);
    frontRight.getConfigurator().apply(currentConfigs);
    backRight.getConfigurator().apply(currentConfigs);

    backRight.getConfigurator().apply(brakeConfigs);
    frontLeft.getConfigurator().apply(brakeConfigs);


  }

public void drive(Double xSpeed, Double ySpeed, Double rotation){
  m_robotDrive.driveCartesian(xSpeed, ySpeed, rotation);
 }

 public void stop(){
  frontLeft.stopMotor();
  backLeft.stopMotor();
  frontRight.stopMotor();
  backRight.stopMotor();
 }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}

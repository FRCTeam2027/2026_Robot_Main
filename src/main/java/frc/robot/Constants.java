// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
//import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {

  public static class OperatorConstants {
    public static final int kDriverControllerPort = 0;
    public static final int kOperatorControllerPort = 1;
    public static final int kTestControllerPort = 2;

  }

  public static class MecanumDriveConstants {
    public static final int backRight = 1;
    public static final int backLeft = 2;
    public static final int frontLeft = 3;
    public static final int frontRight = 4;
    public static final int pidgeon2 = 8;

    public static final TalonFXConfiguration configs = new TalonFXConfiguration();

    public static final CurrentLimitsConfigs currentLimits = new CurrentLimitsConfigs()
                                .withStatorCurrentLimit(80)
                                .withStatorCurrentLimitEnable(true);

    public static final MotorOutputConfigs brakeValues = new MotorOutputConfigs()
                                .withNeutralMode(NeutralModeValue.Brake)
                                .withInverted(InvertedValue.Clockwise_Positive);
  }

  public static class IntakeConstants {
    public static final int shooterIntakeLeft = 6;
    public static final int feederRight = 5;

    public static final Slot0Configs PID_CONFIGS = new Slot0Configs()
                                .withKP(0)
                                .withKI(0)
                                .withKD(0)
                                .withKV(0.12);
                                
                              


    public static final TalonFXConfiguration configs = new TalonFXConfiguration()
                                .withSlot0(PID_CONFIGS);
    
    public static final CurrentLimitsConfigs currentLimits = new CurrentLimitsConfigs()
                                .withStatorCurrentLimit(80)
                                .withStatorCurrentLimitEnable(true);
    
    public static final MotorOutputConfigs brakeValues = new MotorOutputConfigs()
                                .withNeutralMode(NeutralModeValue.Brake)
                                .withInverted(InvertedValue.Clockwise_Positive);

    public static final double RPMtoRPS = 1/60.0;
    public static final double RPStoRPM = 1/RPMtoRPS;    
  }

  public static class LimelightConstants {
    public static final String name = "limelight-whsrd";

    // Set this to max control speed to the robot
    // haha 0.3
    public static final double maxSpeedValue = 0.3;
    
    public static final double maxRotationSpeedValue = 0.3;

    // Used to normalize tx and ty values from limelight, which are in degrees. 
    public static final double MAX_TX_VALUE = 29.8;
    public static final double MAX_TY_VALUE = 24.85;


    // TARGET OFFSET VALUES
    // Trying out using the target in Camera Space to position the robot where we want
    // Adjust these values based on testing
    

    // Positive value will position the robot further to the right of the target,
    // negative value will position it to the left.
    public static final double xTargetOffset = 0.0;

    // How far(in meters) from the robot to place the robot in front of the target
    // Always positive, since we want to be in front of the target, not behind it.
    public static final double zTargetOffset = 1.0;

    // 0 is looking directly at the target, 
    // positive values will position the robot further clockwise, negative values will position it further counterclockwise.
    public static double targetRotationOffset = 0.0;

    // END TARGET OFFSET VALUES


    // Derived from april tag field layout, may need to be adjusted based on 
    // actual field setup and which tags you want to target.
    public static final int[] goalAprilTagIds = new int[] {
      3, 4, 9, 10, 19, 20, 25, 26
    };

    public static final int[] climbAprilTagIds = new int[] {
      15, 16,  31, 32
    };
  }
}

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
    public static final String name = "";

    // Set this to max control speed to the robot
    // haha 0.3
    public static final double maxSpeedValue = 0.3;

    public static final double headingCoefficient = 0.1;
    public static final double distanceCoefficient = 0.1;

    // adjust this for stricter or looser targeting. 0.5 is a good starting point, 
    // but you might want to adjust it based on your testing.
    public static final double minConfidence = 0.5;


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

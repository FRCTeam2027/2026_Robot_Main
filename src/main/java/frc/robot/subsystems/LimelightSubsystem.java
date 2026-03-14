package frc.robot.subsystems;

import java.util.Arrays;

import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.LimelightHelpers;
import frc.robot.Constants.LimelightConstants;
import frc.robot.LimelightHelpers.LimelightResults;
import frc.robot.LimelightHelpers.LimelightTarget_Fiducial;
import frc.robot.LimelightHelpers.RawFiducial;
import java.lang.Math;

/**
 * This is essentially a wrapper for LimelightHelpers
 */
public class LimelightSubsystem extends SubsystemBase  {

    private double headingCorrection;
    private double distanceCorrection;
    
    public LimelightSubsystem() {
        // Might be helpful to figure out the offset of the camera
        /*
        LimelightHelpers.setCameraPose_RobotSpace(
            LimelightConstants.name, 
            0, 0, 0, 
            0, 0, 0);
        */
    }

    public static double clamp(double value, double min, double max){
        return Math.max(min, Math.min(max, value));
    }

    public void setTeleopTargets(){
        LimelightHelpers.SetFiducialIDFiltersOverride(
            LimelightConstants.name, 
            LimelightConstants.goalAprilTagIds);
    }

    public void setEndGameTargets() {
        LimelightHelpers.SetFiducialIDFiltersOverride(
            LimelightConstants.name, 
            LimelightConstants.climbAprilTagIds);
    }

    public double getHeadingCorrection() {
        return headingCorrection;
    }

    public double getDistanceCorrection() {
        return distanceCorrection;
    }

    @Override
    public void periodic() {
        DataLogManager.log("Limelight Periodic");
        LimelightResults results = LimelightHelpers.getLatestResults(LimelightConstants.name);
        LimelightTarget_Fiducial[] fiducials = results.targets_Fiducials;
        DataLogManager.log("" + fiducials.length);

        DataLogManager.log("wow");
        DataLogManager.log("Hello" + LimelightHelpers.getJSONDump(LimelightConstants.name));
        DataLogManager.log("TX: " + results.tx);
        DataLogManager.log("TY: " + results.ty);
        // Sort the april tags by area, so the biggest one is first. 
        // This is a good heuristic for which one is closest to us, and thus the one we want to target.
        Arrays.sort(fiducials, (a, b) -> Double.compare(b.ta, a.ta));

        // If we found any valid april tags, we can use the closest one with decent confidence as our target.
        LimelightTarget_Fiducial bestTag = null;
        if (fiducials.length > 0) {
            bestTag = fiducials[0];
        }

        if(bestTag != null) {
            DataLogManager.log("Best Tag: " + bestTag.fiducialID);
            DataLogManager.log("TX " + bestTag.tx);
            DataLogManager.log("TY: " + bestTag.ty);
            // We have a valid target, so we can use it to calculate our steering and turning values.
            headingCorrection = bestTag.tx * LimelightConstants.headingCoefficient;
            distanceCorrection = bestTag.ty * LimelightConstants.distanceCoefficient;

            // I have no idea what the range of txnc and tync is, so you might need to adjust 
            // the coefficients based on testing to get the behavior you want.
            headingCorrection = clamp(headingCorrection, 
                -LimelightConstants.maxSpeedValue, 
                LimelightConstants.maxSpeedValue);
            distanceCorrection = clamp(distanceCorrection, 
                -LimelightConstants.maxSpeedValue,  
                LimelightConstants.maxSpeedValue);
        } else {
            // No valid target, so we should probably just stop moving.
            headingCorrection = 0;
            distanceCorrection = 0;
        }
    }
}

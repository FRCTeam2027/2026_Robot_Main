package frc.robot.subsystems;

import java.util.Arrays;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.LimelightHelpers;
import frc.robot.Constants.LimelightConstants;
import frc.robot.LimelightHelpers.RawFiducial;

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
        RawFiducial[] aprilTags = LimelightHelpers.getRawFiducials(LimelightConstants.name);

        // Sort the april tags by area, so the biggest one is first. 
        // This is a good heuristic for which one is closest to us, and thus the one we want to target.
        Arrays.sort(aprilTags, (a, b) -> Double.compare(b.ta, a.ta));

        // If we found any valid april tags, we can use the closest one with decent confidence as our target.
        RawFiducial bestTag = null;
        if (aprilTags.length > 0) {
            // loop through the april tags in order of closeness, and find the first one that has decent confidence.
            for (RawFiducial tag : aprilTags) {

                // may need to double check the value of ambiguity, if it goes from 0 > 1 instead of 0 < 1
                if (tag.ambiguity >= LimelightConstants.minConfidence) {
                    bestTag = tag;
                    break;
                }
            }
        }

        if(bestTag != null) {
            // We have a valid target, so we can use it to calculate our steering and turning values.
            headingCorrection = bestTag.txnc * LimelightConstants.headingCoefficient;
            distanceCorrection = bestTag.tync * LimelightConstants.distanceCoefficient;

            // I have no idea what the range of txnc and tync is, so you might need to adjust 
            // the coefficients based on testing to get the behavior you want.
            headingCorrection = Math.clamp(headingCorrection, 
                -LimelightConstants.maxSpeedValue, 
                LimelightConstants.maxSpeedValue);
            distanceCorrection = Math.clamp(distanceCorrection, 
                -LimelightConstants.maxSpeedValue,  
                LimelightConstants.maxSpeedValue);
        } else {
            // No valid target, so we should probably just stop moving.
            headingCorrection = 0;
            distanceCorrection = 0;
        }
    }
}

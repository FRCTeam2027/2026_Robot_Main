package frc.robot.subsystems;

import java.util.Arrays;

import edu.wpi.first.math.geometry.Pose3d;
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

    private double xTarget;
    private double yTarget;
    private double rotationTarget;
    
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

    public double getXTarget() {
        return xTarget;
    }

    public double getYTarget() {
        return yTarget;
    }

    public double getRotationTarget() {
        return rotationTarget;
    }

    @Override
    public void periodic() {
        LimelightResults results = LimelightHelpers.getLatestResults(LimelightConstants.name);
        LimelightTarget_Fiducial[] fiducials = results.targets_Fiducials;

        // Sort the april tags by area, so the biggest one is first. 
        // This is a good heuristic for which one is closest to us, and thus the one we want to target.
        Arrays.sort(fiducials, (a, b) -> Double.compare(b.ta, a.ta));

        // Get the best target (the one with the largest area, which should be the closest one).
        LimelightTarget_Fiducial bestTag = null;
        if (fiducials.length > 0) {
            bestTag = fiducials[0];
        }

        // Comment one to try out
        CalculateTargetsViaOffsets(bestTag);
        //CalculateTargetsViaTargetSpace(bestTag);
    }

    private void CalculateTargetsViaOffsets(LimelightTarget_Fiducial bestTag) {
        // no rotation in this method
        rotationTarget = 0.0f;

        if(bestTag != null) {
            DataLogManager.log("Best Tag: " + bestTag.fiducialID);
            DataLogManager.log("TX " + bestTag.tx);
            DataLogManager.log("TY: " + bestTag.ty);

            // This will normalize the TX and TY values to be between -1 and 1, which makes it easier to work with.
            xTarget = clamp(bestTag.tx / LimelightConstants.MAX_TX_VALUE, -1.0, 1.0);
            yTarget = clamp(bestTag.ty / LimelightConstants.MAX_TY_VALUE, -1.0, 1.0);

            // scale to max speed value, with the above clamp this will make the speed between -maxSpeedValue and maxSpeedValue, which is what we want.
            xTarget *= LimelightConstants.maxSpeedValue;
            yTarget *= LimelightConstants.maxSpeedValue;
        } else {
            // No valid target, so we should probably just stop moving.
            xTarget = 0;
            yTarget = 0;
        }
    }

    private void CalculateTargetsViaTargetSpace(LimelightTarget_Fiducial bestTarget) {
        if(bestTarget != null) {

            // Camera pose values are described here:
            // https://docs.limelightvision.io/docs/docs-limelight/apis/json-results-specification#apriltagfiducial-results
            Pose3d targetSpace = bestTarget.getTargetPose_CameraSpace();
            double xTargetOffset = targetSpace.getTranslation().getX();
            double zTargetOffset = targetSpace.getTranslation().getZ();

            // DOCUMENTATION IS NOT CLEAR ON THIS
            // Website says this the 5th value in the transform array is the yaw,
            // but the comments in LightlimeHelpers say the 5th value is the pitch.
            // test and use the log to figure out which one it is, and adjust the code accordingly.
            double yawTargetOffset = targetSpace.getRotation().getY();

            DataLogManager.log("X Target: " + xTargetOffset);
            DataLogManager.log("Z Target: " + zTargetOffset);
            DataLogManager.log("Yaw Target: " + yawTargetOffset);

            // calculate X Target

            // This will adjust where want to "aim" the robot based on the target offsets defined in Constants.
            xTargetOffset -= LimelightConstants.xTargetOffset;

            // Any distance greater than abs(xTargetOffset) will have the have the robot move closer to the target
            // Any Distance less than abs(xTargetOffset) the robot  will move slower to avoid overshooting.
            xTarget = clamp(xTargetOffset, -LimelightConstants.maxSpeedValue, LimelightConstants.maxSpeedValue);
            // end X Target calculation

            // calculate Y Target
            
            // only check positive values...negative values don't mean anything
            if(zTargetOffset > 0) {
                // abs to account for user error :P 
                // This operation makes zTargetOffset 0 where we want to be
                zTargetOffset -= Math.abs(LimelightConstants.zTargetOffset);

                yTarget = clamp(zTargetOffset, -LimelightConstants.maxSpeedValue, LimelightConstants.maxSpeedValue);
            }
            // end Y Target calculation

            // calculate Rotation Target 

            yawTargetOffset -= LimelightConstants.targetRotationOffset;

            // It may be we need to negate yawTargetOffset, test and adjust as necessary
            yawTargetOffset *= -1;

            rotationTarget = clamp(yawTargetOffset, -LimelightConstants.maxRotationSpeedValue, LimelightConstants.maxRotationSpeedValue);
            // end Rotation Target calculation
        } else {
            // No valid target, so we should probably just stop moving.
            xTarget = 0;
            yTarget = 0;
            rotationTarget = 0;
        }
    }
}

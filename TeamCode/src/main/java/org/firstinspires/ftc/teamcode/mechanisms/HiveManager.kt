package org.firstinspires.ftc.teamcode.mechanisms

import com.pedropathing.math.Pose
import com.qualcomm.hardware.dfrobot.HuskyLens
import dev.nextftc.hardware.RobotController.hardwareMap
import dev.nextftc.robot.Mechanism
import org.firstinspires.ftc.teamcode.core.PoseManager

class HiveManager : Mechanism {
    val HIVE_PITCH_TOLERANCE = 5
    var hiveOnRight = true

    /*
     * Set these to the ID reported by the HuskyLens for the tag or object
     * you want to detect. HuskyLens IDs are integers, not FTC metadata names
     * such as "R" or "B".
     */
    private val targetTagId = if (PoseManager.redAlliance) RED_TARGET_ID else BLUE_TARGET_ID

    private val huskyLens = hardwareMap.get(HuskyLens::class.java, "huskylens")

    /*
     * HuskyLens reports a 320 x 240 image. Its x/y values are pixel centers,
     * with (0, 0) at the top-left. These start at zero until a matching
     * detection is read.
     */
    var targetHorizontalPixelOffset = 0
        private set

    var targetVerticalPixelOffset = 0
        private set

    var targetVisible = false
        private set

    init {
        huskyLens.selectAlgorithm(HuskyLens.Algorithm.TAG_RECOGNITION)
    }

    fun visionUpdate() = instant {
        val matchingBlock = huskyLens.blocks()
            .firstOrNull { block -> block.id == targetTagId }

        if (matchingBlock != null) {
            targetVisible = true

            // Center of the HuskyLens image is x=160, y=120.
            targetHorizontalPixelOffset = matchingBlock.x - IMAGE_CENTER_X
            targetVerticalPixelOffset = matchingBlock.y - IMAGE_CENTER_Y
        } else {
            targetVisible = false
            targetHorizontalPixelOffset = 0
            targetVerticalPixelOffset = 0
        }
    }

    fun setHiveRight() = instant {
        hiveOnRight = true
    }

    fun setHiveLeft() = instant {
        hiveOnRight = false
    }

    fun flipHive() = instant {
        hiveOnRight = !hiveOnRight
    }

    fun getTargetPose(): Pose {
        val targetX = 62.0
        val targetY = if (hiveOnRight) 56.0 else 144.0 - 56.0
        return Pose(targetX, targetY, 0.0)
    }

    companion object {
        /*
         * Replace these example IDs with the IDs you actually see in
         * HuskyLens telemetry. The ID depends on the sensor's algorithm
         * and what it has learned or recognized.
         */
        private const val RED_TARGET_ID = 1
        private const val BLUE_TARGET_ID = 2

        private const val IMAGE_CENTER_X = 160
        private const val IMAGE_CENTER_Y = 120
    }
}
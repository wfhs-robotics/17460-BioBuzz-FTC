package org.firstinspires.ftc.teamcode.mechanisms

import com.pedropathing.ivy.commands.Commands.instant
import com.pedropathing.math.Pose
import com.qualcomm.hardware.dfrobot.HuskyLens
import dev.nextftc.hardware.RobotController.hardwareMap
import dev.nextftc.robot.Mechanism

/** HuskyLens based hive/tag detection. Configure the I2C device as "huskylens". */
class HiveManager : Mechanism {
    val HIVE_PITCH_TOLERANCE = 5
    var hiveOnRight = true

    // HuskyLens reports numeric IDs rather than FTC AprilTag cluster metadata names.
    // Set these to IDs observed on your device; empty means accept any detected tag.
    var targetTagIds: Set<Int> = emptySet()

    private val huskyLens = hardwareMap.get(HuskyLens::class.java, "huskylens")

    var targetBlock: HuskyLens.Block? = null
        private set
    /** Pixel displacement from image center; positive x is right, positive y is down. */
    var horizontalOffsetPixels: Int? = null
        private set
    var verticalOffsetPixels: Int? = null
        private set

    init {
        huskyLens.selectAlgorithm(HuskyLens.Algorithm.TAG_RECOGNITION)
    }

    /** Use as a NextFTC command from robot code. */
    fun visionUpdate() = instant { updateVisionNow() }

    /** Read detections once. Call repeatedly from an OpMode loop for live updates. */
    fun updateVisionNow() {
        val blocks = huskyLens.blocks()
        val candidates = if (targetTagIds.isEmpty()) blocks.toList()
        else blocks.filter { it.id in targetTagIds }

        targetBlock = candidates.minByOrNull { kotlin.math.abs(it.x - IMAGE_CENTER_X) }
        horizontalOffsetPixels = targetBlock?.let { it.x - IMAGE_CENTER_X }
        verticalOffsetPixels = targetBlock?.let { it.y - IMAGE_CENTER_Y }
    }

    fun isConnected(): Boolean = huskyLens.knock()

    fun setHiveRight() = instant { hiveOnRight = true }
    fun setHiveLeft() = instant { hiveOnRight = false }
    fun flipHive() = instant { hiveOnRight = !hiveOnRight }

    fun getTargetPose(): Pose {
        val targetX = 62.0
        val targetY = if (hiveOnRight) 56.0 else 144.0 - 56.0
        return Pose(targetX, targetY, 0.0)
    }

    private companion object {
        // HuskyLens block coordinates are reported on its 320 x 240 image.
        const val IMAGE_CENTER_X = 160
        const val IMAGE_CENTER_Y = 120
    }
}

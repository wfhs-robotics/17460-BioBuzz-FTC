package org.firstinspires.ftc.teamcode.mechanisms

import com.pedropathing.math.Pose
import com.qualcomm.hardware.dfrobot.HuskyLens
import dev.nextftc.hardware.webcams.NextHuskyLens
import dev.nextftc.robot.Mechanism
import org.firstinspires.ftc.teamcode.core.Config
import org.firstinspires.ftc.teamcode.core.PoseManager
import org.firstinspires.ftc.teamcode.core.Dimensions

class HiveManager : Mechanism {
    val HIVE_PITCH_TOLERANCE = 5
    var hiveOnRight = true


    private val targetTagId: Int
        get() = if (PoseManager.redAlliance) RED_TARGET_ID else BLUE_TARGET_ID
    private val huskyLens = NextHuskyLens(Config.Cam.deviceName)


    var targetHorizontalPixelOffset = 0
        private set

    var targetVerticalPixelOffset = 0
        private set

    var targetVisible = false
        private set

    init {
        huskyLens.selectAlgorithm(HuskyLens.Algorithm.TAG_RECOGNITION)
    }

    fun updateVision() {
        val matchingBlock = huskyLens.blocks()
            .firstOrNull { it.id == targetTagId }

        if (matchingBlock != null) {
            targetVisible = true
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
        val targetY = if (hiveOnRight) 56.0 else Dimensions.fieldLength - 56.0
        return Pose(targetX, targetY, 0.0)
    }
    companion object {
        private const val RED_TARGET_ID = 1
        private const val BLUE_TARGET_ID = 2

        private const val IMAGE_CENTER_X = 160
        private const val IMAGE_CENTER_Y = 120
    }
}
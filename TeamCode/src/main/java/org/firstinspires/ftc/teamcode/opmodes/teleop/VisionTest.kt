package org.firstinspires.ftc.teamcode.opmodes.teleop

import dev.nextftc.robot.opmode.NextOpMode
import dev.nextftc.robot.opmode.NextTeleop
import org.firstinspires.ftc.teamcode.MyRobot
import org.firstinspires.ftc.teamcode.core.PoseManager

@NextTeleop("VisionTest")
class VisionTest(private val robot: MyRobot) : NextOpMode(robot) {
    private val hiveManager get() = robot.hiveManager

    override fun start() {
        telemetry.addLine("HuskyLens initialized")
        telemetry.addData("Alliance", if (PoseManager.redAlliance) "RED" else "BLUE")
        telemetry.addData("Hive Side", if (hiveManager.hiveOnRight) "RIGHT" else "LEFT")
        telemetry.addLine("Press START to read detections")
        telemetry.update()
    }

    override fun periodic() {
        hiveManager.updateVision()

            telemetry.addData("Target Visible", hiveManager.targetVisible)
            telemetry.addData("Hive Side", if (hiveManager.hiveOnRight) "RIGHT" else "LEFT")
            telemetry.addData(
                "Horizontal Offset (px)",
                hiveManager.targetHorizontalPixelOffset
            )
            telemetry.addData(
                "Vertical Offset (px)",
                hiveManager.targetVerticalPixelOffset
            )
            telemetry.update()
    }
}
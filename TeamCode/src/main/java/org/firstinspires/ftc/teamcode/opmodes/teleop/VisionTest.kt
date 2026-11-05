package org.firstinspires.ftc.teamcode.opmodes.teleop

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.core.PoseManager
import org.firstinspires.ftc.teamcode.mechanisms.HiveManager

@TeleOp(name = "Vision Test (HuskyLens)", group = "Vision")
class VisionTest : LinearOpMode() {
    override fun runOpMode() {
        val hiveManager = HiveManager()

        telemetry.addLine("HuskyLens initialized")
        telemetry.addData("Alliance", if (PoseManager.redAlliance) "RED" else "BLUE")
        telemetry.addData("Hive Side", if (hiveManager.hiveOnRight) "RIGHT" else "LEFT")
        telemetry.addLine("Press START to read detections")
        telemetry.update()

        waitForStart()

        while (opModeIsActive()) {
            hiveManager.visionUpdate()

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

            idle()
        }
    }
}
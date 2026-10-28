package org.firstinspires.ftc.teamcode.opModes.teleop

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.mechanisms.HiveManager

@TeleOp(name = "Vision Test (HuskyLens)", group = "Vision")
class VisionTest : LinearOpMode() {
    override fun runOpMode() {
        val hiveManager = HiveManager()
        telemetry.addData("HuskyLens connected", hiveManager.isConnected())
        telemetry.addLine("HuskyLens algorithm set to TAG_RECOGNITION")
        telemetry.addLine("Press start to view tag detections")
        telemetry.update()

        waitForStart()

        while (opModeIsActive()) {
            hiveManager.updateVisionNow()
            val block = hiveManager.targetBlock
            telemetry.addData("Target found", block != null)
            telemetry.addData("Target tag ID", block?.id ?: "none")
            telemetry.addData("Horizontal offset (px)", hiveManager.horizontalOffsetPixels ?: "none")
            telemetry.addData("Vertical offset (px)", hiveManager.verticalOffsetPixels ?: "none")
            telemetry.addData("Target filter IDs", if (hiveManager.targetTagIds.isEmpty()) "all" else hiveManager.targetTagIds)
            telemetry.update()
            idle()
        }
    }
}


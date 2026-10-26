package org.firstinspires.ftc.teamcode.opmodes.teleop

import dev.nextftc.robot.opmode.NextOpMode
import dev.nextftc.robot.opmode.NextTeleop
import org.firstinspires.ftc.teamcode.MyRobot
import org.firstinspires.ftc.teamcode.core.PoseManager
import kotlin.time.Clock.System.now

private const val TELEMETRY_INTERVAL: Int = 250
private var firstOnUpdate = true
private var lastLoopTime = 0.0
private var maxLoopTime = 0.0
private var minLoopTime = Double.MAX_VALUE
private var lastTelemetryTime = 0.0


@NextTeleop("MainTeleOp")
class MainTeleOp(val robot: MyRobot) : NextOpMode(robot) {

    override fun start() {
        robot.initializeFollower(hardwareMap)
        robot.drivetrain.startDrive(gamepad1).schedule()
    }

    override fun periodic() {
        robot.updateFollower()

        val now = System.nanoTime() / 1_000_000.0

        if (firstOnUpdate) {
            lastTelemetryTime = now
            lastLoopTime = now
            firstOnUpdate = false
            return
        }

        val loopTime = now - lastLoopTime
        lastLoopTime = now
        if (loopTime > maxLoopTime) maxLoopTime = loopTime
        if (loopTime < minLoopTime) minLoopTime = loopTime

        if (now - lastTelemetryTime > TELEMETRY_INTERVAL) {
            lastTelemetryTime = now

            val pose = robot.follower.pose()
            telemetry.addData("Robot X", "%.2f in", pose.x())
            telemetry.addData("Robot Y", "%.2f in", pose.y())
            telemetry.addData(
                "Heading",
                "%.1f°",
                Math.toDegrees(pose.heading())
            )
            telemetry.update()
        }
    }
}
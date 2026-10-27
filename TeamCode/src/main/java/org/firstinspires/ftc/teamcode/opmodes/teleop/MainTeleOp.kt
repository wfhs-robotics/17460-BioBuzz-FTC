package org.firstinspires.ftc.teamcode.opmodes.teleop

import com.pedropathing.follower.ManualDrive
import com.pedropathing.ivy.commands.Commands.instant
import com.pedropathing.math.Pose
import dev.nextftc.robot.opmode.NextOpMode
import dev.nextftc.robot.opmode.NextTeleop
import dev.nextftc.robot.triggers.CommandGamepad
import org.firstinspires.ftc.teamcode.MyRobot
import org.firstinspires.ftc.teamcode.core.PoseManager
import kotlin.time.Clock.System.now

private const val TELEMETRY_INTERVAL: Int = 250
private var firstOnUpdate = true
private var lastLoopTime = 0.0
private var maxLoopTime = 0.0
private var minLoopTime = Double.MAX_VALUE
private var lastTelemetryTime = 0.0
private var startSystemTime = 0.toLong()


@NextTeleop("MainTeleOp")
class MainTeleOp(val robot: MyRobot) : NextOpMode(robot) {
    private var driveScalar = 1.0

    override fun start() {
        robot.follower.setPose(PoseManager.EndPose)
        buildDriverControls()
        robot.drivetrain.startDrive(gamepad1).schedule()
        startSystemTime = System.currentTimeMillis()
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

            telemetry.addData("Robot Pos", robot.follower.pose())

            telemetry.addData("Shooter RPM", robot.shooter.motor.encoderVelocity.magnitude)

            telemetry.addData("Loop Time", "%.2f ms".format(loopTime))
            telemetry.addData("Min Loop Time", "%.2f ms".format(minLoopTime))
            telemetry.addData("Max Loop Time", "%.2f ms".format(maxLoopTime))
            telemetry.addData("Target Pos", robot.hiveManager.getTargetPose())

            telemetry.addData("Shooter", robot.shooter.targetVelocity + robot.shooter.manualOffset)
            telemetry.addData("Hood", robot.turrethood.targetPosition + robot.turrethood.manualOffset)

            telemetry.addData("Loop Hz", "%.2f".format(1000.0 / loopTime))
            telemetry.addLine(PoseManager.resetPoses.toString())
            telemetry.update()
        }
    }

    override fun end() {
        PoseManager.EndPose = robot.follower.pose()
        robot.shooter.stop().schedule()
    }

    fun buildDriverControls(){
        val driver = CommandGamepad(gamepad1)
        val operator = CommandGamepad(gamepad2)

        // Driver Controls
        driver.rightBumper
            .toggleOnTrue(robot.intake.intake())

        driver.leftBumper
            .toggleOnTrue(robot.intake.outtake())

        // Operator Controls

        operator.dpadUp.onTrue(instant { robot.shooter.manualOffset += 10 })
        operator.dpadDown.onTrue(instant { robot.shooter.manualOffset -= 10 })
        operator.dpadRight.onTrue(instant { robot.turrethood.manualOffset += .05 })
        operator.dpadLeft.onTrue(instant { robot.turrethood.manualOffset -= .05 })
    }
}
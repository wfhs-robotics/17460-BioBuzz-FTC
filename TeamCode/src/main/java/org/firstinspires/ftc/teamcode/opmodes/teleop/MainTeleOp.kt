package org.firstinspires.ftc.teamcode.opmodes.teleop

import com.pedropathing.follower.ManualDrive
import com.pedropathing.ivy.commands.Commands.instant
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


@NextTeleop("MainTeleOp")
class MainTeleOp(val robot: MyRobot) : NextOpMode(robot) {

    override fun start() {
        robot.initializeFollower(hardwareMap)
        buildDriverControls()
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
    fun buildDriverControls(){
        val driver = CommandGamepad(gamepad1)
        val operator = CommandGamepad(gamepad2)

        // Driver Controls
        driver.rightBumper
            .toggleOnTrue(robot.intake.intake())

        driver.leftBumper
            .toggleOnTrue(robot.intake.outtake())

        driver.rightTrigger.isOver(0.5).and(driver.leftTrigger.isOver(0.5))
            .toggleOnTrue(instant { robot.follower.setPose(PoseManager.masterResetPose) })

        driver.rightTrigger.isOver(0.5).and(driver.leftTrigger.isUnder(0.5))
            .toggleOnTrue(instant { robot.follower.setPose(PoseManager.getResetPose(robot.follower.pose())) })

        driver.y.onTrue(instant {driveScalar = 0.5})
        driver.y.onFalse(instant {driveScalar = 1.0})

        // Operator Controls

        operator.dpadUp.onTrue(instant { robot.shooter.manualOffset += 10 })
        operator.dpadDown.onTrue(instant { robot.shooter.manualOffset -= 10 })
        operator.dpadRight.onTrue(instant { robot.turrethood.manualOffset += .05 })
        operator.dpadLeft.onTrue(instant { robot.turrethood.manualOffset -= .05 })
    }
}
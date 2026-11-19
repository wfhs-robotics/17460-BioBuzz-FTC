package org.firstinspires.ftc.teamcode.opmodes.teleop

import com.pedropathing.ivy.commands.Commands
import com.pedropathing.ivy.commands.Commands.instant
import dev.nextftc.robot.drive.scalar
import dev.nextftc.robot.opmode.NextOpMode
import dev.nextftc.robot.opmode.NextTeleop
import dev.nextftc.robot.triggers.CommandGamepad
import org.firstinspires.ftc.teamcode.MyRobot
import org.firstinspires.ftc.teamcode.core.PoseManager
import org.firstinspires.ftc.teamcode.opmodes.OpModeStorage
import org.firstinspires.ftc.teamcode.utils.BiLinearShooter
import org.firstinspires.ftc.teamcode.utils.RobotLogger
import org.firstinspires.ftc.teamcode.utils.ZoneDetection

private const val TELEMETRY_INTERVAL: Int = 250
private var firstOnUpdate = true
private var lastLoopTime = 0.0
private var maxLoopTime = 0.0
private var minLoopTime = Double.MAX_VALUE
private var lastTelemetryTime = 0.0
private var startSystemTime = 0.toLong()
private val zoneDetection = ZoneDetection()


@NextTeleop("MainTeleOp")
class MainTeleOp(val robot: MyRobot) : NextOpMode(robot) {

    private var logger: RobotLogger? = null

    override fun start() {
        robot.updateFollower()
        firstOnUpdate = true
        lastLoopTime = 0.0
        maxLoopTime = 0.0
        minLoopTime = Double.MAX_VALUE
        lastTelemetryTime = 0.0

        robot.follower.setPose(
            OpModeStorage.autonomousEndPose ?: PoseManager.masterResetPose
        )
        buildDriverControls()
        logger = RobotLogger()
        startSystemTime = System.currentTimeMillis()
        robot.drivetrain.startDrive(gamepad1)
    }

    override fun periodic() {
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

        robot.updateFollower()

        robot.hiveManager.hiveOnRight = robot.follower.pose().y() <= 72.0

        val pose = robot.follower.pose()
        val inShootingZone =
            zoneDetection.poseInTriangle(pose, zoneDetection.scaledCloseShootingZone) ||
                    zoneDetection.poseInTriangle(pose, zoneDetection.scaledFarShootingZone)

        if (inShootingZone) {
            val shot = BiLinearShooter.getShot(
                pose.x(),
                pose.y(),
                robot.follower.velocity().toVector(),
                robot.hiveManager.hiveOnRight
            )
            BiLinearShooter.applyShot(shot, robot)
    }
        if (now - lastTelemetryTime > TELEMETRY_INTERVAL) {
            lastTelemetryTime = now
            val rpm = robot.shooter.motor.encoderVelocity.magnitude * 60.0 / (2.0 * Math.PI)

            telemetry.addData("Robot Pos", robot.follower.pose())

            telemetry.addData("Shooter RPM", rpm)

            telemetry.addData("Loop Time", "%.2f ms".format(loopTime))
            telemetry.addData("Min Loop Time", "%.2f ms".format(minLoopTime))
            telemetry.addData("Max Loop Time", "%.2f ms".format(maxLoopTime))
            telemetry.addData("Target Pos", robot.hiveManager.getTargetPose())

            telemetry.addData("Shooter target", robot.shooter.targetVelocity)
            telemetry.addData("Hood target", robot.turretHood.targetPosition)

            telemetry.addData("Loop Hz", "%.2f".format(1000.0 / loopTime))
            telemetry.update()

            logger?.log(
                System.currentTimeMillis() - startSystemTime,
                robot.follower.pose().x(),
                robot.follower.pose().y(),
                robot.follower.pose().heading(),
                BiLinearShooter.projectedX,
                BiLinearShooter.projectedY,
                robot.shooter.targetVelocity,
                robot.shooter.motor.encoderVelocity.magnitude,
                robot.shooter.motor.throttle
            )
        }
    }

    override fun end() {
        PoseManager.EndPose = robot.follower.pose()
        logger?.close()
        robot.shooter.stop().schedule()
    }

    fun buildDriverControls(){
        val driver = CommandGamepad(gamepad1)
        val operator = CommandGamepad(gamepad2)

        // Driver Controls
        driver.leftBumper.whileTrue(
            Commands.infinite { scalar = 0.5 }.setEnd { scalar = 1.0 },
        )

        driver.rightTrigger.isOver(0.5).and(driver.leftTrigger.isOver(0.5))
            .toggleOnTrue(instant { robot.follower.setPose(PoseManager.masterResetPose) })

        driver.rightTrigger.isOver(0.5).and(driver.leftTrigger.isUnder(0.5))
            .toggleOnTrue(instant { robot.follower.setPose(PoseManager.getResetPose(robot.follower.pose())) })

        // Operator Controls

        operator.rightBumper.whileTrue(robot.intake.intake())
        operator.rightBumper.whileFalse(robot.intake.stop())

        operator.leftBumper.whileTrue(robot.intake.outtake())
        operator.leftBumper.whileFalse(robot.intake.stop())

        operator.a.whileTrue(robot.indexer.feed())
        operator.a.whileFalse(robot.indexer.stop())

        operator.b.whileTrue(robot.indexer.reverse())
        operator.b.whileFalse(robot.indexer.stop())

        operator.dpadUp.onTrue(instant { robot.shooter.adjustManualOffset(10.0) })
        operator.dpadDown.onTrue(instant { robot.shooter.adjustManualOffset(-10.0) })
        operator.dpadRight.onTrue(instant { robot.turretHood.adjustManualOffset(0.05) })
        operator.dpadLeft.onTrue(instant { robot.turretHood.adjustManualOffset(-0.05) })
    }
}
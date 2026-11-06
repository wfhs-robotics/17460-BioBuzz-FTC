package org.firstinspires.ftc.teamcode.opmodes.teleop

import com.pedropathing.ivy.commands.Commands
import com.pedropathing.ivy.commands.Commands.instant
import dev.nextftc.robot.drive.scalar
import dev.nextftc.robot.opmode.NextOpMode
import dev.nextftc.robot.opmode.NextTeleop
import dev.nextftc.robot.triggers.CommandGamepad
import org.firstinspires.ftc.teamcode.MyRobot
import org.firstinspires.ftc.teamcode.OpModeStorage
import org.firstinspires.ftc.teamcode.core.PoseManager
import org.firstinspires.ftc.teamcode.utils.BiLinearShooter
import org.firstinspires.ftc.teamcode.utils.RobotLogger

private const val TELEMETRY_INTERVAL: Int = 250
private var firstOnUpdate = true
private var lastLoopTime = 0.0
private var maxLoopTime = 0.0
private var minLoopTime = Double.MAX_VALUE
private var lastTelemetryTime = 0.0
private var startSystemTime = 0.toLong()
private var FieldCentric = false


@NextTeleop("MainTeleOp")
class MainTeleOp(val robot: MyRobot) : NextOpMode(robot) {
    private lateinit var logger: RobotLogger

    override fun disabledPeriodic() {
        if (gamepad1.a) FieldCentric = false
        if (gamepad1.b) FieldCentric = true

        telemetry.addLine("A: Robot-centric   |   B: Field-centric")
        telemetry.addData("Drive mode", if (FieldCentric) "Field-centric" else "Robot-centric")
        telemetry.update()
    }

    override fun start() {
        robot.follower.setPose(OpModeStorage.autonomousEndPose)
        buildDriverControls()
        logger = RobotLogger()
        startSystemTime = System.currentTimeMillis()
        val driveCommand = if (FieldCentric) {
            robot.drivetrain.startFieldCentricDrive(gamepad1) {
                robot.follower.pose().heading()
            }
        } else {
            robot.drivetrain.startDrive(gamepad1)
        }
        driveCommand.schedule()
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

        if (robot.follower.pose().y() > 72.0) robot.hiveManager.setHiveLeft()
        else robot.hiveManager.setHiveRight()

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

            logger.log(
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
        logger.close()
        robot.shooter.stop().schedule()
    }

    fun buildDriverControls(){
        val driver = CommandGamepad(gamepad1)
        val operator = CommandGamepad(gamepad2)

        // Driver Controls
        driver.leftBumper.whileTrue(
            Commands.infinite { scalar = 0.5 }.setEnd { scalar = 1.0 },
        )
        // Operator Controls

        operator.rightBumper.toggleOnTrue(robot.intake.intake())

        operator.leftBumper.toggleOnTrue(robot.intake.outtake())

        operator.dpadUp.onTrue(instant { robot.shooter.manualOffset += 10 })
        operator.dpadDown.onTrue(instant { robot.shooter.manualOffset -= 10 })
        operator.dpadRight.onTrue(instant { robot.turrethood.manualOffset += .05 })
        operator.dpadLeft.onTrue(instant { robot.turrethood.manualOffset -= .05 })
    }
}
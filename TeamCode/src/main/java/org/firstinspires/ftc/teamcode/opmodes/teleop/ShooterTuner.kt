package org.firstinspires.ftc.teamcode.opmodes.teleop

import com.pedropathing.ivy.commands.Commands.instant
import dev.nextftc.robot.opmode.NextOpMode
import dev.nextftc.robot.opmode.NextTeleop
import dev.nextftc.robot.triggers.CommandGamepad
import org.firstinspires.ftc.teamcode.MyRobot

@NextTeleop(name = "Shooter Test & Tune")
class ShooterTuner(val myRobot: MyRobot) : NextOpMode(myRobot) {

    private var targetVel = 1600.0

    override fun start() {
        val driver = CommandGamepad(gamepad1)

        driver.dpadUp.onTrue(instant { targetVel += 50.0 })
        driver.dpadDown.onTrue(instant { targetVel -= 50.0 })

        driver.a.onTrue(myRobot.shooter.spinAtSpeed(targetVel))
        driver.b.onTrue(myRobot.shooter.stop())
    }

    override fun periodic() {
        if (myRobot.shooter.active) {
            myRobot.shooter.targetVelocity = targetVel
        }

        val currentVelocity = myRobot.shooter.motor.encoderVelocity.magnitude
        val power = myRobot.shooter.motor.throttle

        telemetry.addData("Target Velocity", targetVel)
        telemetry.addData("Current Velocity", currentVelocity)
        telemetry.addData("Error", targetVel - currentVelocity)
        telemetry.addData("Motor Power", "%.3f".format(power))
        telemetry.update()
    }
}
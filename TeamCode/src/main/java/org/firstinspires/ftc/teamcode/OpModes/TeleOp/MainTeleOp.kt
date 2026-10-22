package org.firstinspires.ftc.teamcode.OpModes.TeleOp

import dev.nextftc.robot.opmode.NextOpMode
import dev.nextftc.robot.opmode.NextTeleop
import org.firstinspires.ftc.teamcode.MyRobot

@NextTeleop
class DrivetrainTeleOp(
    val robot: MyRobot
) : NextOpMode(robot) {

    override fun start() {
        robot.drivetrain.startDrive(gamepad1)
    }
}
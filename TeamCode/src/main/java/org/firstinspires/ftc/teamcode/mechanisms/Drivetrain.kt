
package org.firstinspires.ftc.teamcode.mechanisms

import com.pedropathing.ivy.Command
import com.qualcomm.robotcore.hardware.Gamepad
import dev.nextftc.hardware.actuators.NextMotor
import dev.nextftc.robot.Mechanism
import dev.nextftc.robot.drive.mecanumDrive
import dev.nextftc.robot.drive.mecanumDriveFieldCentric
import org.firstinspires.ftc.teamcode.core.Config


class Drivetrain : Mechanism {

    val frontLeft = NextMotor(Config.frontLeft.deviceName)
    val frontRight = NextMotor(Config.frontRight.deviceName)
    val backLeft = NextMotor(Config.backLeft.deviceName)
    val backRight = NextMotor(Config.backRight.deviceName)

    fun startDrive(gamepad: Gamepad): Command = mecanumDrive(frontLeft, frontRight, backLeft, backRight, gamepad)
}
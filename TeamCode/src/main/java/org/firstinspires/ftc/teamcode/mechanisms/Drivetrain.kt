
package org.firstinspires.ftc.teamcode.mechanisms

import com.qualcomm.robotcore.hardware.Gamepad
import dev.nextftc.hardware.actuators.NextMotor
import dev.nextftc.robot.Mechanism
import dev.nextftc.robot.drive.mecanumDrive
import org.firstinspires.ftc.teamcode.data.Config

class Drivetrain : Mechanism {

    val frontLeft = NextMotor(Config.frontLeft.deviceName)
    val frontRight = NextMotor(Config.frontRight.deviceName)
    val backLeft = NextMotor(Config.backLeft.deviceName)
    val backRight = NextMotor(Config.backRight.deviceName)

    init {
        frontRight.direction = NextMotor.Direction.REVERSE
        backRight.direction = NextMotor.Direction.REVERSE
    }

    fun startDrive(gamepad: Gamepad) {
        mecanumDrive(
            frontLeft,
            frontRight,
            backLeft,
            backRight,
            gamepad
        ).schedule()
    }
}

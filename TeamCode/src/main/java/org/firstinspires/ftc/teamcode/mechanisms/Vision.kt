package org.firstinspires.ftc.teamcode.mechanisms

import dev.nextftc.hardware.RobotController.expansionHub
import dev.nextftc.hardware.actuators.NextMotor
import dev.nextftc.hardware.actuators.NextServo
import dev.nextftc.hardware.webcams.NextHuskyLens
import dev.nextftc.robot.Mechanism
import org.firstinspires.ftc.teamcode.data.Config

class Vision : Mechanism {
    val turretCam = NextHuskyLens(Config.turretCam.deviceName)
    }
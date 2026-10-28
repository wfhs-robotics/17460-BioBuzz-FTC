package org.firstinspires.ftc.teamcode.mechanisms

import dev.nextftc.hardware.webcams.NextHuskyLens
import dev.nextftc.robot.Mechanism
import org.firstinspires.ftc.teamcode.core.Config

class Vision : Mechanism {
    val Cam = NextHuskyLens(Config.Cam.deviceName)
    }
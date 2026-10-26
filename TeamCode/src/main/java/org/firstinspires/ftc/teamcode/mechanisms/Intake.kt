package org.firstinspires.ftc.teamcode.mechanisms

import com.pedropathing.ivy.Command
import dev.nextftc.hardware.actuators.NextMotor
import dev.nextftc.robot.Mechanism
import org.firstinspires.ftc.teamcode.core.Config
import kotlin.math.abs

class Intake : Mechanism {
    val intakeMotor = NextMotor(Config.intake.deviceName)

    val isRunning: Boolean
        get() = abs(intakeMotor.throttle) > 0.1

    init {
        intakeMotor.direction = NextMotor.Direction.FORWARD
    }

    fun intake(): Command = infinite {
        intakeMotor.throttle = 1.0
    }

    fun outtake(): Command = infinite {
        intakeMotor.throttle = -1.0
    }

    fun stop(): Command = instant {
        intakeMotor.throttle = 0.0
    }
}
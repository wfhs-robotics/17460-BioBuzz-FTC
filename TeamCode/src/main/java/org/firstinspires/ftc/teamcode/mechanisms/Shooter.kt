package org.firstinspires.ftc.teamcode.mechanisms

import com.pedropathing.ivy.Command
import com.qualcomm.robotcore.util.ElapsedTime
import dev.nextftc.hardware.actuators.NextMotor
import dev.nextftc.robot.Mechanism
import dev.nextftc.units.radiansPerSecond
import org.firstinspires.ftc.teamcode.core.Config
import kotlin.math.abs

class Shooter : Mechanism {
    val motor = NextMotor(Config.launcher.deviceName)

    @JvmField var targetVelocity = 0.0
    @JvmField var manualOffset = 0.0
    val MAX_SPEED = 2200.0

    @JvmField var kP = 0.001
    @JvmField var kS = 0.061
    @JvmField var kV = 0.000385

    var active = false
    var ready = false
    private val timer = ElapsedTime()

    init {
        motor.direction = NextMotor.Direction.REVERSE
        motor.zeroPowerBehavior = NextMotor.ZeroPowerBehavior.FLOAT
    }

    override fun periodic() {

        motor.velocityConstants.apply {
            this.kP = this@Shooter.kP
            this.kS = this@Shooter.kS
            this.kV = this@Shooter.kV
        }

        if (active) {
            val currentVel = motor.encoderVelocity.magnitude
            val error = targetVelocity - currentVel

            //bang-bang controller
            if (error > 100.0) {
                motor.throttle = 1.0
            } else if (error < -100.0) {
                motor.throttle = 0.0
            } else {
                motor.setVelocitySetpoint(targetVelocity.radiansPerSecond)
            }

            // Check if shooter has stabilized
            ready = abs(error) < 21.0

        } else {
            motor.throttle = 0.0
            ready = false
        }
    }

    fun spinAtSpeed(speed: Double): Command = instant {
        setTargetSpeed(speed)
    }.requiring(this)

    fun stop(): Command = instant {
        active = false
        targetVelocity = 0.0
        ready = false
    }.requiring(this)

    fun adjustSpeed(adj: Double) {
        targetVelocity = (targetVelocity + adj).coerceIn(0.0, MAX_SPEED)
    }

    fun setTargetSpeed(speed: Double) {
        val newTarget = speed.coerceIn(0.0, MAX_SPEED)

        if (abs(newTarget - targetVelocity) > 1.0) {
            targetVelocity = newTarget
            ready = false
        }

        active = true
    }

    fun adjustManualOffset(change: Double) {
        manualOffset = (manualOffset + change).coerceIn(-300.0, 300.0)
    }
}
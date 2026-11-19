package org.firstinspires.ftc.teamcode.mechanisms

import com.pedropathing.ivy.Command
import dev.nextftc.hardware.actuators.NextMotor
import dev.nextftc.hardware.sensors.NextColorDistanceSensor
import dev.nextftc.robot.Mechanism
import org.firstinspires.ftc.teamcode.core.Config
import kotlin.math.abs

class Indexer : Mechanism {
    private val indexer1 = NextMotor(Config.indexer1.deviceName)
    private val indexer2 = NextMotor(Config.indexer2.deviceName)

    @JvmField var feedPower = 1.0
    @JvmField var reversePower = -1.0



    val isRunning: Boolean
        get() =
            abs(indexer1.throttle) > 0.1 ||
                    abs(indexer2.throttle) > 0.1

    init {
        indexer1.direction = NextMotor.Direction.FORWARD
        indexer2.direction = NextMotor.Direction.FORWARD
    }
    fun feed(): Command = infinite {
        indexer1.throttle = feedPower
        indexer2.throttle = feedPower
    }.requiring(this)

    fun reverse(): Command = infinite {
        indexer1.throttle = reversePower
        indexer2.throttle = reversePower
    }.requiring(this)

    fun stop(): Command = instant {
        indexer1.throttle = 0.0
        indexer2.throttle = 0.0
    }.requiring(this)
}
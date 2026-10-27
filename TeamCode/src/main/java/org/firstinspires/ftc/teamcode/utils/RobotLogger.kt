package org.firstinspires.ftc.teamcode.utils

import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

/**
 * CSV logging of robot data to SD card
 */
class RobotLogger {
    private val writer: BufferedWriter

    init {
        val file = File("/sdcard/FIRST/log.csv")
        writer = BufferedWriter(FileWriter(file))

        // CSV header
        writer.write("Time,X,Y,Heading,ProjectedX,ProjectedY,ShooterTarget,ShooterActual,ShooterPower")
        writer.newLine()
    }

    fun log(
        time: Long,
        x: Double,
        y: Double,
        heading: Double,
        projectedX: Double,
        projectedY: Double,
        shooterTarget: Double,
        shooterActual: Double,
        shooterPower: Double
    ) {
        writer.write("$time,$x,$y,$heading,$projectedX,$projectedY,$shooterTarget,$shooterActual,$shooterPower")
        writer.newLine()
    }

    fun close() {
        writer.flush()
        writer.close()
    }
}
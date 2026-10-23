package org.firstinspires.ftc.teamcode

import dev.nextftc.robot.NextRobot
import dev.nextftc.robot.Mechanism
import org.firstinspires.ftc.teamcode.mechanisms.Drivetrain
import org.firstinspires.ftc.teamcode.mechanisms.Intake
import org.firstinspires.ftc.teamcode.mechanisms.TurretHood
import org.firstinspires.ftc.teamcode.mechanisms.Vision

class MyRobot : NextRobot {

    val drivetrain = Drivetrain()
    val intake = Intake()
    val turrethood = TurretHood()
    val vision = Vision()

    override val mechanisms: Set<Mechanism>
        get() = setOf(drivetrain)
}
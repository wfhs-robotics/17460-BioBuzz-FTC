package org.firstinspires.ftc.teamcode

import com.pedropathing.follower.Follower
import com.qualcomm.robotcore.hardware.HardwareMap
import dev.nextftc.hardware.RobotController
import dev.nextftc.robot.NextRobot
import dev.nextftc.robot.Mechanism
import org.firstinspires.ftc.teamcode.mechanisms.Drivetrain
import org.firstinspires.ftc.teamcode.mechanisms.Intake
import org.firstinspires.ftc.teamcode.mechanisms.Shooter
import org.firstinspires.ftc.teamcode.mechanisms.TurretHood
import org.firstinspires.ftc.teamcode.mechanisms.Vision
import org.firstinspires.ftc.teamcode.pedro.Constants

class MyRobot : NextRobot {

    val drivetrain = Drivetrain()
    val intake = Intake()
    val turrethood = TurretHood()
    val vision = Vision()
    val shooter = Shooter()

    override val mechanisms: Set<Mechanism>
        get() = setOf(drivetrain, intake, turrethood, vision, shooter)

    private var _follower: Follower? = null

    val follower: Follower
        get() {
            if (_follower == null) {
                _follower = Constants.create(RobotController.hardwareMap)
            }
            return _follower!!
        }

    fun updateFollower() {
        follower.update()
    }
}

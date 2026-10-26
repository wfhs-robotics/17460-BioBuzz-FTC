package org.firstinspires.ftc.teamcode

import com.pedropathing.follower.Follower
import dev.nextftc.hardware.RobotController
import dev.nextftc.robot.NextRobot
import org.firstinspires.ftc.teamcode.mechanisms.Drivetrain
import org.firstinspires.ftc.teamcode.mechanisms.Intake
import org.firstinspires.ftc.teamcode.mechanisms.Shooter
import org.firstinspires.ftc.teamcode.mechanisms.TurretHood
import org.firstinspires.ftc.teamcode.pedro.Constants
import org.firstinspires.ftc.teamcode.mechanisms.HiveManager

class MyRobot : NextRobot {
    val hiveManager by lazy { HiveManager() }
    val drivetrain = Drivetrain()
    val intake = Intake()
    val turrethood = TurretHood()
    val shooter = Shooter()

    override val mechanisms by lazy { setOf(hiveManager, intake, shooter, turrethood, drivetrain) }


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

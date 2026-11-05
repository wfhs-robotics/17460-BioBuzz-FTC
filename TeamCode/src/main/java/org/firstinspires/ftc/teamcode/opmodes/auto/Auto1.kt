package org.firstinspires.ftc.teamcode.autos.ExampleAuto

import com.pedropathing.api.PoseFactory
import com.pedropathing.follower.Follower
import com.pedropathing.math.Pose
import dev.nextftc.hardware.RobotController
import dev.nextftc.robot.opmode.NextAutonomous
import dev.nextftc.robot.opmode.NextOpMode
import org.firstinspires.ftc.teamcode.MyRobot
import org.firstinspires.ftc.teamcode.pedro.Constants

@NextAutonomous("Auto1","Auto","MainTeleOp")
class Auto1(val robot: MyRobot) : NextOpMode(robot){
    private var _follower: Follower? = null
    private val p: PoseFactory = PoseFactory.degrees()

    private val startPose: Pose? = p.of(24.0, 24.0, 0.0)
    private val park: Pose? = p.of(48.0, 48.0, 90.0)

    val follower: Follower
        get() {
            if (_follower == null) {
                _follower = Constants.create(RobotController.hardwareMap)
            }
            return _follower!!
        }

    override fun disabledPeriodic() {
        follower.update()
        follower.setPose(startPose);
    }

    override fun start() {

    }

    override fun periodic() {
    }
}
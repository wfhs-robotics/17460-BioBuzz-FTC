package org.firstinspires.ftc.teamcode

import com.pedropathing.api.Paths
import com.pedropathing.api.PoseFactory
import com.pedropathing.follower.Follower
import com.pedropathing.paths.Path
import com.pedropathing.ivy.Command
import com.pedropathing.ivy.Scheduler
import com.pedropathing.ivy.Scheduler.schedule
import com.pedropathing.ivy.groups.Groups.sequential
import com.pedropathing.ivy.pedro.PedroCommands.follow
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.pedropathing.paths.interpolator.Interpolator
import org.firstinspires.ftc.teamcode.pedro.Constants

@Autonomous(name = "AutoPath", group = "Autonomous")
class AutoPath : LinearOpMode() {

    private lateinit var follower: Follower

    private val poseFactory = PoseFactory.degrees()

    private val start = poseFactory.of(56.0, 8.0, 90.0)
    private val path1 = poseFactory.of(56.0, 36.0, 90.0)
    private val point2 = poseFactory.of(20.1628, 7.5948, 180.0)
    private val point3 = poseFactory.of(9.3613, 7.5948, 180.0)
    private val point4 = poseFactory.of(56.0, 36.0, 90.0)
    private val point4Segment1Start = poseFactory.of(56.0, 36.0, 180.0)
    private val point4Segment1End = poseFactory.of(56.0, 36.0, 90.0)
    private val point4Segment2Heading = poseFactory.of(56.0, 36.0, 90.0)
    private val point5 = poseFactory.of(9.1453, 41.0796, 90.0)
    private val point6 = poseFactory.of(56.0, 36.0, 90.0)
    private val point7 = poseFactory.of(11.5216, 90.1185, 90.0)
    private val point7Control1 = poseFactory.of(10.0519, 37.9023, 0.0)

    // Autonomous routine
    fun autoRoutine(): Command = sequential(
        follow(follower, path1()),
        follow(follower, path2()),
        follow(follower, path3()),
        follow(follower, path4()),
        follow(follower, path5()),
        follow(follower, path6()),
        follow(follower, path7())
    )

    override fun runOpMode() {
        Scheduler.reset()
        follower = Constants.create(hardwareMap)
        follower.setPose(start)
        follower.update()

        waitForStart()
        schedule(autoRoutine())

        while (opModeIsActive()) {
            follower.update()
            Scheduler.execute()

            telemetry.addData("x", follower.pose().x())
            telemetry.addData("y", follower.pose().y())
            telemetry.addData("heading", follower.pose().heading())

            if (follower.currentPath() != null) {
                telemetry.addData("Current path distance remaining", follower.distanceToEndpoint())
                telemetry.addData("Path number", follower.pathIndex())
            }

            telemetry.update()
        }
    }

    fun path1(): Path = Paths.line(start, path1).constant(path1)

    fun path2(): Path = Paths.line(path1, point2).constant(point2)

    fun path3(): Path = Paths.line(point2, point3).tangent()

    fun path4(): Path = Paths.line(point3, point4).heading(Interpolator.piecewise().until(0.5, Interpolator.linear(point4Segment1Start, point4Segment1End)).until(1.0, Interpolator.constant(point4Segment2Heading)))

    fun path5(): Path = Paths.line(point4, point5).constant(point5)

    fun path6(): Path = Paths.line(point5, point6).constant(point6)

    fun path7(): Path = Paths.curve(point6, point7Control1, point7).constant(point7)
}

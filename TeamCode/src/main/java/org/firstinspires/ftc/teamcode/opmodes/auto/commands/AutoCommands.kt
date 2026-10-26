package org.firstinspires.ftc.teamcode.opmodes.auto.commands

import com.pedropathing.follower.Follower
import com.pedropathing.ivy.CommandBuilder
import com.pedropathing.ivy.groups.Groups.parallel
import com.pedropathing.ivy.pedro.PedroCommands.follow
import com.pedropathing.paths.Path
import org.firstinspires.ftc.teamcode.MyRobot
import org.firstinspires.ftc.teamcode.opmodes.auto.paths.PathsAndPoses

class AutoCommands(
    private val follower: Follower,
    private val paths: PathsAndPoses,
    private val robot: MyRobot
) {
    fun runPath(path: Path): CommandBuilder =
        follow(follower, path)

    fun intakePath(path: Path): CommandBuilder =
        parallel(
            runPath(path),
            robot.intake.intake()
        )
}
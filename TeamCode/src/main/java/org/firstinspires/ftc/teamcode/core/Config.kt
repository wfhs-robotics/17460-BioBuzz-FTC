package org.firstinspires.ftc.teamcode.core

enum class Config(val deviceName: String) {
    // Drivetrain
    frontRight("frontRightMotor"),
    backRight("backRightMotor"),
    frontLeft("frontLeftMotor"),
    backLeft("backLeftMotor"),
    // Motors

    intake("intakeMotor"),
    // Turret
    launcher("launchMotor"),
    hood("hoodServo"),
    //Odometry
    pinpoint("pinpoint"),
    // Cam
    Cam("huskylens"),
}
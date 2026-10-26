package org.firstinspires.ftc.teamcode.core

enum class Config(val deviceName: String) {
    // Drivetrain
    frontRight("frontRightMotor"),
    backRight("backRightMotor"),
    frontLeft("frontLeftMotor"),
    backLeft("backLeftMotor"),
    // Motors
    launcher("launchMotor"),
    intake("intakeMotor"),
    // Turrel
    turret("turretServo"),
    hood("hoodServo"),

    pinpoint("pinpoint"),

    turretCam("HuskyLens"),
}
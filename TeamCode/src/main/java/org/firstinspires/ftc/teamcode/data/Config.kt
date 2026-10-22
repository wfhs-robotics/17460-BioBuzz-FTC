package org.firstinspires.ftc.teamcode.data

enum class Config(val deviceName: String) {
    frontRight("frontRightMotor"),
    backRight("backRightMotor"),
    frontLeft("frontLeftMotor"),
    backLeft("backLeftMotor"),
    launcher("launchMotor"),
    intake("intakeMotor"),

    turret("turretServo"),
    hood("hoodServo"),

    pinpoint("pinpoint"),

    turretCam("HuskyLens"),
}
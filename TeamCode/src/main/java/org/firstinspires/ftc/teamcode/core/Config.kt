package org.firstinspires.ftc.teamcode.core

enum class Config(val deviceName: String) {
    // Drivetrain
    frontRight("frontRightMotor"),
    backRight("backRightMotor"),
    frontLeft("frontLeftMotor"),
    backLeft("backLeftMotor"),
    // Motors
    indexer1("indexer1Motor"),
    indexer2("indexer2Motor"),
    intake("intakeMotor"),
    // Turret
    launcher("launchMotor"),
    hood("hoodServo"),
    //Odometry
    pinpoint("pinpoint"),
    // Sensor

    // Cam
    Cam("huskylens"),
}
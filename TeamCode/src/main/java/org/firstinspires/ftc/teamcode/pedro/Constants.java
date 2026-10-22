
package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Vector2D;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Constants {

    public static MecanumConfig drivetrainConfig =
            new MecanumConfig(c -> {
                c.frontLeftName.set("frontLeft");
                c.frontRightName.set("frontRight");
                c.backLeftName.set("backLeft");
                c.backRightName.set("backRight");

                // Verify these using the Mecanum tuner.
                c.frontLeftDirection.set(DcMotorSimple.Direction.FORWARD);
                c.backLeftDirection.set(DcMotorSimple.Direction.FORWARD);
                c.frontRightDirection.set(DcMotorSimple.Direction.REVERSE);
                c.backRightDirection.set(DcMotorSimple.Direction.REVERSE);

                c.manualBrakeMode.set(true);
            });

    public static PinpointConfig localizerConfig =
            new PinpointConfig(c -> {
                c.name.set("pinpoint");

                // Temporary values: replace with Pinpoint tuner results.
                c.xPodOffset.set(0.0);
                c.yPodOffset.set(0.0);

                c.xPodDirection.set(
                        GoBildaPinpointDriver.EncoderDirection.FORWARD);
                c.yPodDirection.set(
                        GoBildaPinpointDriver.EncoderDirection.FORWARD);
            });

    public static ForesightConfig foresightConfig =
            new ForesightConfig(c -> {
                // Temporary starter controller values.
                Controller primaryForward = Controller.proportional(0.3);
                Controller secondaryForward = Controller.proportional(0.1);
                Controller primaryStrafe = Controller.proportional(0.3);
                Controller secondaryStrafe = Controller.proportional(0.1);

                c.forwardTranslational.set(
                        Controller.piecewise(secondaryForward)
                                .put(2.5, primaryForward));

                c.strafeTranslational.set(
                        Controller.piecewise(secondaryStrafe)
                                .put(2.5, primaryStrafe));

                // These are placeholders, not tuned robot values.
                c.coast.set(Controller.proportionalFeedforward(0.01));
                c.brake.set(Controller.proportionalFeedforward(0.01));
                c.headingFeedback.set(Controller.proportional(5.0));

                c.headingBrakeCoefficients.set(
                        Vector2D.cartesian(0.05, 0.006));

                c.linearBrakeCoefficients.set(
                        Matrix.diag(0.1, 0.1));

                c.quadraticBrakeCoefficients.set(
                        Matrix.diag(0.001, 0.001));

                c.maxAchievableForwardVelocity.set(40.0);
                c.maxAchievableStrafeVelocity.set(30.0);
                c.naturalForwardDeceleration.set(30.0);
                c.naturalStrafeDeceleration.set(30.0);
            });

    public static Follower create(HardwareMap h) {
        return new Follower(
                new PinpointLocalizer(h, localizerConfig),
                new Mecanum(h, drivetrainConfig),
                new Foresight(foresightConfig)
        );
    }
}

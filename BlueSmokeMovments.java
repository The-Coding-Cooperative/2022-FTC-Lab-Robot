package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;


@TeleOp(name="Trak Moves", group="Greg")
public class BlueSmokeMovments extends OpMode{
    HardwareBlueSmoke         greg = new HardwareBlueSmoke();   // Use a Pushbot's hardware
    private ElapsedTime     runtime = new ElapsedTime();
    double pos1 = 0;
    double mod1 = 0;
    double rate = 0.1;

    //    Color sensor
    public NormalizedColorSensor colorSensor = null;
    View relativeLayout;

    final float[] hsvValues = new float[3];


    @Override
    public void init() {
        telemetry.addData("Status", "Init Started");
        telemetry.addData("Say", "I know Kung Fu");
        telemetry.update();

        greg.init(hardwareMap);

        colorSensor = hardwareMap.get(NormalizedColorSensor.class, "sensor_color");

        telemetry.addData("Status", "Init Complete");
        runtime.reset();
        telemetry.update();

        greg.wheel1.setPower(0);
        greg.wheel2.setPower(0);
        greg.motor2.setPower(0);
        greg.wheel1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        greg.wheel2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        greg.motor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    @Override
    public void loop() {
            if (gamepad1.a) {
                while(gamepad1.a){
                    double wheel1pos = greg.wheel1.getCurrentPosition();
                    double wheel2pos = greg.wheel2.getCurrentPosition();
                    double motor1pos = greg.motor1.getCurrentPosition();
                    double motor2pos = greg.motor2.getCurrentPosition();

                    telemetry.addData("Pos: ", "Wheel1 %s:", wheel1pos);
                    telemetry.addData("Pos: ", "Wheel2 %s:", wheel2pos);
                    telemetry.addData("Pos: ", "Motor1 %S:", motor1pos);
                    telemetry.addData("Pos: ", "Motor2 %s:", motor2pos);

                    NormalizedRGBA colors = colorSensor.getNormalizedColors();
                    Color.colorToHSV(colors.toColor(), hsvValues);
                    telemetry.addData("Alpha: ", " Motor %S", colors.alpha);

                    telemetry.update();
                }

                greg.wheel1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                greg.wheel2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                greg.motor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                greg.motor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

                greg.wheel1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                greg.wheel2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                greg.motor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                greg.motor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


            }
            else {
                double drive_l_y = gamepad1.left_stick_x;
                double drive_l_x = -gamepad1.left_stick_y;

                double leftPower = Range.clip(drive_l_y + drive_l_x, -1.0, 1.0);
                double rightPower = Range.clip(drive_l_y - drive_l_x, -1.0, 1.0);

                greg.wheel1.setPower(leftPower);
                greg.wheel2.setPower(rightPower);

                double drive_r_y = gamepad1.right_stick_y;
                double drive_r_x = gamepad1.right_stick_x;

                greg.motor1.setPower(drive_r_y/ 4);
                greg.motor2.setPower(drive_r_x / 4);

                // Show the elapsed game time and wheel power.

                if (gamepad1.left_bumper) {
                    mod1 = rate;
                } else if (gamepad1.right_bumper) {
                    mod1 = -rate;
                }

                pos1 = Range.clip(pos1 + mod1, 0, 1);

                greg.servo1.setPosition(pos1);
                telemetry.update();
            }

    }

    public void encoderDrive(double speed,
                                     double motor1count, double motor2count,
                                     double wheel1count, double wheel2count,
                                     double timeoutS) {
            double w1t = greg.wheel1.getCurrentPosition() + (int)wheel1count;
            double w2t = greg.wheel2.getCurrentPosition() + (int)wheel2count;
//                double m1t = greg.motor1.getCurrentPosition() + (int)motor1count;
//                double m2t = greg.motor2.getCurrentPosition() + (int)motor2count;
            double m1t = (int)motor1count;
            double m2t = (int)motor2count;

            greg.motor1.setTargetPosition((int) m1t);
            greg.motor2.setTargetPosition((int) m2t);
            greg.wheel1.setTargetPosition((int) w1t);
            greg.wheel2.setTargetPosition((int) w2t);

            // Turn On RUN_TO_POSITION
            greg.wheel1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            greg.wheel2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            greg.motor1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            greg.motor2.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
//                runtime.reset();
            greg.wheel2.setPower(Math.abs(speed));
            greg.wheel1.setPower(Math.abs(speed));
            greg.motor1.setPower(Math.abs(speed));
            greg.motor2.setPower(Math.abs(speed));

            while ((greg.wheel1.isBusy() || greg.wheel2.isBusy())) {
                telemetry.addData("Path1",  "Running to %7d :%7d", (int)w1t,  (int)w2t);
                telemetry.addData("Path2",  "Running at %7d :%7d",
                        (int)greg.wheel1.getCurrentPosition(),
                        (int)greg.wheel2.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            greg.wheel2.setPower(0);
            greg.wheel1.setPower(0);
            greg.motor1.setPower(0);
            greg.motor2.setPower(0);
            // Turn off RUN_TO_POSITION
            greg.wheel2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            greg.wheel1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            greg.motor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            greg.motor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            //  sleep(250);   // optional pause after each move
        }
    }



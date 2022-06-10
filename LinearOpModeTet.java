package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="Ted: Linear Op", group = "Tet")
public class LinearOpModeTet extends OpMode{
    HardwareInit         greg = new HardwareInit();   // Use a push hardware
    private ElapsedTime runtime = new ElapsedTime();
    double pos1 = 0;
    double pos2 = 0;
    double pos3 = 0;
    double pos4 = 0;

    double mod1 = 0;
    double mod2 = 0;
    double mod3 = 0;

    double rate = 0.1;

    @Override
    public void init() {
        telemetry.addData("Status", "Init Started");
        telemetry.addData("Say", "I know Kung Fu");
        telemetry.update();

        greg.init(hardwareMap);

        telemetry.addData("Status", "Init Complete");
        runtime.reset();
        telemetry.update();

        greg.wheel1.setPower(0);
        greg.wheel2.setPower(0);
        greg.motor2.setPower(0);
    }

    @Override
    public void loop() {
        double drive_l_y = gamepad1.left_stick_x;
        double drive_l_x = -gamepad1.left_stick_y;

        double leftPower    = Range.clip(drive_l_y + drive_l_x, -1.0, 1.0) ;
        double rightPower   = Range.clip(drive_l_y - drive_l_x, -1.0, 1.0) ;

        greg.wheel1.setPower(leftPower);
        greg.wheel2.setPower(rightPower);

        double drive_r_y = gamepad1.right_stick_y;
        double drive_r_x = gamepad1.right_stick_x;

//        greg.motor1.setPower(drive_r_y);
        greg.motor2.setPower(drive_r_x);
//        pos3 = Range.clip(pos3 + (drive_r_y/2+1)/10, 0, 1);
//        greg.servo3.setPosition(pos3);
        // Show the elapsed game time and wheel power.
        telemetry.addData("Status", drive_r_y + "Run Time: " + pos3 + " "+ gamepad1.a + " " + gamepad1.b);
        telemetry.update();

        if (gamepad1.left_bumper){
            mod1 = rate;
        }
        else if (gamepad1.right_bumper) {
            mod1 = -rate;
        }

        if (gamepad1.x){
            mod2 = pos2 + 0.3;
        }
        else if (gamepad1.y){
            mod2 = pos2 - 0.3;
        }

        if (gamepad1.a){
            mod3 = pos3 + 0.01;
        }
        else if (gamepad1.b){
            mod3 = pos3 - 0.01;
        }

        pos1 = Range.clip(pos1 + mod1, 0,1);
        pos2 = Range.clip(pos2 + mod2, 0, 1);
        pos3 = Range.clip(mod3, 0, 1);

        greg.servo1.setPosition(pos1);
//        greg.servo3.setPosition(pos2);
        greg.servo3.setPosition(pos3);

    }
}

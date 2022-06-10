//package org.firstinspires.ftc.teamcode;
//
//import com.qualcomm.robotcore.eventloop.opmode.Disabled;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import com.qualcomm.robotcore.util.ElapsedTime;
//import com.qualcomm.robotcore.util.Range;
//
//@TeleOp(name="T1", group="T1")
//@Disabled
//public class t1 extends OpMode{
//    HardwareInit         greg = new HardwareInit();   // Use a Pushbot's hardware
//    private ElapsedTime     runtime = new ElapsedTime();
//
//    @Override
//    public void init() {
//        telemetry.addData("Status", "Init Started");
//        telemetry.addData("Say", "I know Kung Fu");
//        telemetry.update();
//
//        greg.init(hardwareMap);
//
//        telemetry.addData("Status", "Init Complete");
//        runtime.reset();
//        telemetry.update();
//
//        double pos1 = 0;
//        double pos2 = 0;
//        double pos3 = 0;
//    }
//    // https://first-tech-challenge.github.io/SkyStone/
//
//    @Override
//    public void loop() {
//        double drive_l_y = -gamepad1.left_stick_y;
//        double drive_l_x = gamepad1.left_stick_x;
//
//        double leftPower    = Range.clip(drive_l_y + drive_l_x, -1.0, 1.0) ;
//        double rightPower   = Range.clip(drive_l_y - drive_l_x, -1.0, 1.0) ;
//
//        greg.wheel1.setPower(leftPower);
//        greg.wheel2.setPower(rightPower);
//
//        double drive_r_y = gamepad1.right_stick_y;
//        double drive_r_x = gamepad1.right_stick_x;
//
//        greg.motor1.setPower(drive_r_y);
//        // greg.motor2.setPower(drive_r_x);
//
//        // Show the elapsed game time and wheel power.
//        telemetry.addData("Status", "Run Time: " + runtime.toString());
//        telemetry.update();
//        if (gamepad1.dpad_down){
//            pos1 = 1;
//        }
//        else if (gamepad1.dpad_up){
//            pos1 = -1;
//        }
//        else {
//            pos1 = 0;
//        }
//
//        if (gamepad1.dpad_left){
//            pos2 = 1;
//        }
//        else if (gamepad1.dpad_right){
//            pos2 = -1;
//        }
//        else {
//            pos2 = 0;
//        }
//
//        if (gamepad1.left_bumper){
//            pos3 = 1;
//        }
//        else if (gamepad1.right_bumper){
//            pos3 = -1;
//        }
//        else {
//            pos3 = 0;
//        }
//
//        gerg.moter3 = pos1;
//        greg.servo1 = pos2;
//        greg.servo2 = pos3;
//
//
//
//    }
//}
//package org.firstinspires.ftc.teamcode;
//
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import com.qualcomm.robotcore.eventloop.opmode.Disabled;
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.util.ElapsedTime;
//import com.qualcomm.robotcore.util.Range;
//
//@TeleOp(name="testOP", group = "test")
//@Disabled
//public class BasicTestOpMode extends OpMode{
//
//    HardwareTest hwt = new HardwareTest();
//    private  ElapsedTime  runtime  = new ElapsedTime();
//
//    @Override
//    public void init() {
//        hwt.init(hardwareMap);
//        telemetry.addData("Status", "Init done");
//        telemetry.addData("Say", "I know Kungfu");
//        telemetry.update();
//        runtime.reset();
//    }
//
//    @Override
//    public void loop() {
//        double drive_x = gamepad1.left_stick_x;
////        double s1 = gamepad1.left_stick_y;
////        double s2 = gamepad1.right_stick_y;
//        boolean s3 = gamepad1.left_bumper;
//        boolean s1 = gamepad1.right_bumper;
//        boolean s2 = gamepad1.dpad_down;
//        double s3pos;
//        double s2pos;
//        double s1pos;
//
//
//        hwt.motor2.setPower(Range.clip(drive_x, -1.0, 1.0));
////        hwt.servo1.setPosition(Range.clip(s1, 0, 1.0));
////        hwt.servo2.setPosition(Range.clip(s2, 0, 1.0));
//
//        if (s1 == true) {
//            s1pos = 1.0;
//        }
//        else {
//            s1pos = 0;
//        }
//        hwt.servo1.setPosition(s1pos);
//
//        if (s2 == true) {
//            s2pos = 1.0;
//        }
//        else {
//            s2pos = 0;
//        }
//        hwt.servo2.setPosition(s2pos);
//
//        if (s3 == true){
//            s3pos = 1.0;
//        }
//        else {
//            s3pos = 0.0;
//        }
//        hwt.servo3.setPosition(s3pos);
//
//        telemetry.addData("Status", "Run Time: " + runtime.toString());
//        telemetry.addData("Drive_x", Range.clip(drive_x, -1.0, 1.0));
//        telemetry.addData("left_bumper", s3pos);
//        telemetry.addData("s2", s2pos);
//        telemetry.addData("s1", s1pos);
//
////        telemetry.addData("Drive_x", Range.clip(drive_y, -1.0, 1.0));
//
//        telemetry.update();
//    }
//}
//
//

 package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class HardwareBlueSmoke {
    HardwareMap hwMap = null;
    private ElapsedTime period = new ElapsedTime();

    public DcMotor wheel1 = null;
    public DcMotor wheel2 = null;
    public DcMotor motor1 = null;
    public DcMotor motor2 = null;

    public Servo servo1 = null;
//    public Servo servo2 = null;
//    public Servo servo3 = null;

    public void init(HardwareMap ahwMap) {
        hwMap = ahwMap;

        wheel1 = hwMap.get(DcMotor.class, "wheel1");
        wheel2 = hwMap.get(DcMotor.class, "wheel2");
        motor1 = hwMap.get(DcMotor.class, "motor1");
        motor2 = hwMap.get(DcMotor.class, "motor2");

        wheel1.setDirection(DcMotor.Direction.FORWARD);
        wheel2.setDirection(DcMotor.Direction.FORWARD);
        motor1.setDirection(DcMotor.Direction.FORWARD);
        motor2.setDirection(DcMotor.Direction.FORWARD);

        wheel1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        wheel2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        wheel1.setPower(0.0);
        wheel2.setPower(0.0);
        motor1.setPower(0);
        motor2.setPower(0);

        servo1 = hwMap.get(Servo.class, "servo1");
//        servo2 = hwMap.get(Servo.class, "servo2");
//        servo3 = hwMap.get(Servo.class, "servo3");

        servo1.setDirection(Servo.Direction.FORWARD);
//        servo2.setDirection(Servo.Direction.FORWARD);
//        servo3.setDirection(Servo.Direction.FORWARD);

        servo1.setPosition(0);
//        servo2.setPosition(0);
//        servo3.setPosition(0);
    }
}
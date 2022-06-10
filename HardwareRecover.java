package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class HardwareRecover {
    /* Public OpMode members. */
    public DcMotor  lDrive   = null;
    public DcMotor  rDrive  = null;
    public DcMotor  boxmotor     = null;
    public DcMotor  clawMotor     = null;
    public Servo    rotateServo    = null;
    public Servo    clawServo   = null;
    public Servo    rampServo   = null;


    public static final double MID_SERVO       =  0.5 ;
    public static final double ARM_UP_POWER    =  0.45 ;
    public static final double ARM_DOWN_POWER  = -0.45 ;

    /* local OpMode members. */
    HardwareMap hwMap           =  null;
    private ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    public HardwareRecover(){

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        clawMotor  = hwMap.get(DcMotor.class, "claw_motor");
        boxmotor  = hwMap.get(DcMotor.class, "box_motor");

        //rDrive = hwMap.get(DcMotor.class, "r_drive");

        //xDrive = hwMap.get(DcMotor.class, "x_drive");
        //zDrive = hwMap.get(DcMotor.class, "z_Drive");

        clawMotor.setDirection(DcMotor.Direction.FORWARD);
        boxmotor.setDirection(DcMotor.Direction.FORWARD);
        //rDrive.setDirection(DcMotor.Direction.REVERSE);

        //xDrive.setDirection(DcMotor.Direction.FORWARD);
        //zDrive.setDirection(DcMotor.Direction.REVERSE);
        // Set all motors to zero power
        clawMotor.setPower(0);
        boxmotor.setPower(0);
        //rDrive.setPower(0);
        //xDrive.setPower(0);
        //zDrive.setPower(0);

        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        clawMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        boxmotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //xDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //zDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        // Define and initialize ALL installed servos.
//        rotateServo = hwMap.get(Servo.class, "rotate_servo");
//        clawServo = hwMap.get(Servo.class, "claw_servo");
//        rampServo = hwMap.get(Servo.class, "ramp_servo");
//
//        rotateServo.setDirection(Servo.Direction.FORWARD);
//        clawServo.setDirection(Servo.Direction.FORWARD);
//        rampServo.setDirection(Servo.Direction.FORWARD);
//
//        rotateServo.setPosition(MID_SERVO);
//        clawServo.setPosition(MID_SERVO);
//        rampServo.setPosition(MID_SERVO);

    }
}
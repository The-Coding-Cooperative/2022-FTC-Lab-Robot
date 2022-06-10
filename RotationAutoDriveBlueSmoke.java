package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

@Autonomous(name="Role out: Greg", group="Greg")
public class RotationAutoDriveBlueSmoke extends LinearOpMode {

    /* Declare OpMode members. */
    HardwareBlueSmoke         greg   = new HardwareBlueSmoke();   // Use a Pushbot's hardware
    private ElapsedTime     runtime = new ElapsedTime();

    static final double TOTAL_HEIGHT = 14.5;
    double current_height = 0;
    static final double     COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 5.3 ;     // Fzr figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     DRIVE_SPEED             = 0.6;
    static final double     TURN_SPEED              = 0.5;
    public NormalizedColorSensor colorSensor = null;

    @Override
    public void runOpMode() {
        greg.init(hardwareMap);
        colorSensor = hardwareMap.get(NormalizedColorSensor.class, "sensor_color");
        // Send telemetry message to signify greg waiting;
        telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();

        greg.wheel1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        greg.wheel2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        greg.wheel1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        greg.wheel2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Status: ",  "Waiting for start");
        telemetry.update();
        waitForStart();

//      Start - wobble
        int duck_index = find_duck();

//        encoderDrive(DRIVE_SPEED, 0, 0, 4839, -4516, 30);
        encoderDrive(DRIVE_SPEED, 0, 0, -1175, -1171, 30);
        encoderDrive(DRIVE_SPEED, 0, 0, 1659, -1659, 30);

        if(duck_index == 3){
            encoderDrive(1,13146+9355,0,0,0,0);
        }
        else if(duck_index == 2){
            encoderDrive(1,13146,0,0,0,0);
        }
        else{
            encoderDrive(1,0,0,0,0,0);
        }

        sleep(1000);     // pause for servos to move

        encoderDrive(1,0,0,-733,750,0);
        encoderDrive(1,0,0,-1195,-1177,0);
        encoderDrive(1,0,0,-4145,4170,0);
        encoderDrive(1,0,0,335,4170,0);

        telemetry.addData("Path", "Complete");
        telemetry.update();
    }

    public int find_duck(){
//        Drive to POS 1
        encoderDrive(1,0, 0, 2313, -2243, 30);
        NormalizedRGBA color = colorSensor.getNormalizedColors();
        int out = 0;
        if (color.alpha > 0.5){
//            Inverse move
            out = 1;
        }
        //        Drive to POS
        encoderDrive(1,0, 0, -253, 1218, 30);
        encoderDrive(1,0, 0, 565, -413, 30);

        color = colorSensor.getNormalizedColors();
        if(color.alpha > 0.5){
            out = 2;
        }
        else {
            out =  3;
        }
        return out;
    }
    public void z_update(double update){
        current_height += update;
        move_y_axis(1.0, update, 5.0);
    }

    public void move_y_axis(double speed,
                            double z_update,
                            double timeoutS) {

        int newZupdate;
        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newZupdate = greg.wheel1.getCurrentPosition() + (int) (z_update * COUNTS_PER_INCH);
            greg.motor1.setTargetPosition(newZupdate);

            // Turn On RUN_TO_POSITION
            greg.motor1.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();// TODO Look into what this is
            greg.motor1.setPower(Math.abs(speed));

            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (greg.motor1.isBusy())) {

                telemetry.update();
            }

            // Stop all motion;
            greg.motor1.setPower(0);

            // Turn off RUN_TO_POSITION
            greg.motor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //  sleep(250);   // optional pause after each move
        }
    }
        public void encoderDrive(double speed,
        double motor1count, double motor2count,
        double wheel1count, double wheel2count,
        double timeoutS) {
            if (opModeIsActive()) {
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

                while (opModeIsActive() &&
                        (greg.wheel1.isBusy() || greg.wheel2.isBusy())) {
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
    }

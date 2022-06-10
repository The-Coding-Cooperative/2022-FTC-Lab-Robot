package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="Autobots Role out: Greg", group="Greg")
public class AutoDriveByEncoderBlueSmoke extends LinearOpMode {

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

    @Override
    public void runOpMode() {
        greg.init(hardwareMap);

        // Send telemetry message to signify greg waiting;
        telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();

        greg.wheel1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        greg.wheel2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        greg.wheel1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        greg.wheel2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Path0",  "Starting at %7d :%7d",
                greg.wheel1.getCurrentPosition(),
                greg.wheel2.getCurrentPosition());
        telemetry.update();
        waitForStart();

//        encoderDrive(DRIVE_SPEED, 10, 10, 8.0);
        encoderDrive(DRIVE_SPEED, (14*4), (-14*4), 8.0);

        sleep(1000);     // pause for servos to move

        telemetry.addData("Path", "Complete");
        telemetry.update();
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
        double leftInches, double rightInches,
        double timeoutS) {
            int newLeftTarget;
            int newRightTarget;

            // Ensure that the opmode is still active
            if (opModeIsActive()) {
                leftInches = leftInches * 1.2;
                rightInches = rightInches * 1.2;

                // Determine new target position, and pass to motor controller
                newLeftTarget = greg.wheel1.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
                newRightTarget = greg.wheel2.getCurrentPosition() + (int)(-rightInches * COUNTS_PER_INCH);
                greg.wheel1.setTargetPosition(newLeftTarget);
                greg.wheel2.setTargetPosition(newRightTarget);

                // Turn On RUN_TO_POSITION
                greg.wheel1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                greg.wheel2.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                // reset the timeout time and start motion.
                runtime.reset();// TODO Look into what this is
                greg.wheel2.setPower(Math.abs(speed));
                greg.wheel1.setPower(Math.abs(speed));

                while (opModeIsActive() &&
                        (runtime.seconds() < timeoutS) &&
                        (greg.wheel1.isBusy() && greg.wheel2.isBusy())) {

                    // Display it for the driver.
                    telemetry.addData("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
                    telemetry.addData("Path2",  "Running at %7d :%7d",
                            greg.wheel2.getCurrentPosition(),
                            greg.wheel1.getCurrentPosition());

                    telemetry.update();
                }

                // Stop all motion;
                greg.wheel2.setPower(0);
                greg.wheel1.setPower(0);

                // Turn off RUN_TO_POSITION
                greg.wheel2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                greg.wheel1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

                //  sleep(250);   // optional pause after each move
            }
        }
    }

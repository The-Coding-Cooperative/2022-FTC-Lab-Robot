/* Copyright (c) 2019 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

/**
 * This 2020-2021 OpMode illustrates the basics of using the TensorFlow Object Detection API to
 * determine the position of the Freight Frenzy game elements.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list.
 *
 * IMPORTANT: In order to use this OpMode, you need to obtain your own Vuforia license key as
 * is explained below.
 */
@TeleOp(name = "Concept: TensorFlow Object Detection", group = "opMode")
public class BlueSmokeAuto extends LinearOpMode {
    private static final String TFOD_MODEL_ASSET = "FreightFrenzy_BCDM.tflite";
    private static final String[] LABELS = {
      "Ball",
      "Cube",
      "Duck",
      "Marker"
    };

    private static final String VUFORIA_KEY =
            "AfhjTET/////AAABmRh+dBblDE04ttZpN+NCE+h9YtY1Xs/1HM5trf3TPFFFPJ36lT+AiQsbSDN0+B5a3bSBYNyuMDppNZ+kqtGXYXcw/nkBO+VH2Pfm/lP6wU+BdpIdRO1tD0C3SftMo0B597clqMJzJe1SqJmzPH4PZa69EY+ntUFCph5wvmND/KU3T9yTDl4zzGVWByKWw/QfoaoCC/6AMZ7rfBRu4mpo/IoWjKPcGj3Jw4MvYCVgq1HzQVuat95T73y86SflNglMcuczUFr5XEvQH1KDixOQG6jPE8Yzd0TkJQaUtncckFzE465/Qrx3YlI546Zlsz4tT2rldl9OyU6L1MT4y7/HIIJweHW1a0qSwpCnHmC9BPr2";
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;
    public int sector = 0;

//    Autodirve

    static final double TOTAL_HEIGHT = 14.5;
    double current_height = 0;
    static final double     COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 5.3 ;     // Fzr figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     DRIVE_SPEED             = 0.6;
    static final double     TURN_SPEED              = 0.5;

// Hardware
    public HardwareBlueSmoke greg   = new HardwareBlueSmoke();   // Use a Pushbot's hardware
    private ElapsedTime runtime = new ElapsedTime();

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

        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.
        initVuforia();
        initTfod();

        /**
         * Activate TensorFlow Object Detection before we wait for the start command.
         * Do it here so that the Camera Stream window will have the TensorFlow annotations visible.
         **/
        if (tfod != null) {
            tfod.activate();
            tfod.setZoom(1, 16.0/9.0);
        }

        /** Wait for the game to begin */
        telemetry.addData(">", "Press Play to start op mode");
        telemetry.update();
        waitForStart();

        if (opModeIsActive()) {
            while (opModeIsActive()) {
                if (tfod != null && sector == 0) {
                    // getUpdatedRecognitions() will return null if no new information is available since
                    // the last time that call was made.
                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                    if (updatedRecognitions != null) {
                      telemetry.addData("# Object Detected", updatedRecognitions.size());

                      // step through the list of recognitions and display boundary info.
                      int i = 0;
                        for (Recognition recognition : updatedRecognitions) {
                          if ("duck" == recognition.getLabel()) {
                              double midPoint = recognition.getLeft() + ((recognition.getRight() - recognition.getLeft()) / 2);
                              if (midPoint < recognition.getWidth() / 3) {
                                  this.sector = 1;
                              } else if (midPoint < (recognition.getWidth() / 3) * 2) {
                                  sector = 2;
                              } else {
                                  sector = 3;
                              }
                          }
                      }
                    }
                }
                if (sector == 1){
                    encoderDrive(1, 10, 10, 5);
                }
                else if (sector == 2){
                    encoderDrive(1, -10, -10, 5);
                }
                else{
                    encoderDrive(1, -10, -10, 5);
                }
            }
        }
    }

    /**
     * Initialize the Vuforia localization engine.
     */
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = CameraDirection.BACK;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }

    /**
     * Initialize the TensorFlow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
            "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.9f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 320;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);
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

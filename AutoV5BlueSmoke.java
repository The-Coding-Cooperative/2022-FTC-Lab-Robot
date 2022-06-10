package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

@TeleOp(name="V5", group="Greg")
public class AutoV5BlueSmoke extends LinearOpMode {

    /* Declare OpMode members. */
    HardwareBlueSmoke         greg   = new HardwareBlueSmoke();   // Use a Pushbot's hardware
    private ElapsedTime     runtime = new ElapsedTime();

    static final double     DRIVE_SPEED             = 0.6;
    static final int Y_STEP = -5000;

//    AI
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


    @Override
    public void runOpMode() {
        initVuforia();
        initTfod();
        if (tfod != null){
            tfod.activate();
            tfod.setZoom(1, 16.0/9.0);

        }
        greg.init(hardwareMap);

        boolean duckDetected = false;
        int sector;
        int yupdate = 0;
        double recogAvg;

        telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();

        greg.wheel1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        greg.wheel2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        greg.wheel1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        greg.wheel2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Send telemetry message to indicate successful Encoder reset
        waitForStart();

        telemetry.addData("Duck Shearch Started", "Duck Shearch Started: ");
        telemetry.update();

        if (opModeIsActive()) {
            while (!duckDetected) {
                List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                telemetry.addData("No Detection", "");
                if (updatedRecognitions != null) {
                    int i = 0;
                    for (Recognition recognition : updatedRecognitions) {
                        telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                        telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                                recognition.getLeft(), recognition.getTop());
                        telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                                recognition.getRight(), recognition.getBottom());
                        i++;
                        if (recognition.getLabel().equals("Duck")) {
                            recogAvg = recognition.getLeft() + (recognition.getRight() - recognition.getLeft()) / 2;
                            sector = (int) Math.round(recogAvg / (recognition.getImageWidth() / 3));
                            if (sector == 0) {
                                sector = 1;
                            }
                            yupdate = sector * Y_STEP;
                            duckDetected = true;
                            telemetry.addData("Section", " %d", sector);
                            telemetry.addData("Yup: ", "%d", yupdate);
//                        }
                        }
                    }
                }
                sleep(30000);
                telemetry.update();
            }
        }

//      Bot movement
//      Our start position will be on 27.75

//      Start - wobble
            encoderDrive(DRIVE_SPEED, 0, 0, 3488, -6488, 30);

//      Y movement up/down
            encoderDrive(DRIVE_SPEED, yupdate, 0, 0, 0, 5);


            telemetry.addData("Path", "Complete");

        }
    public void encoderDrive(double speed,
        double motor1count, double motor2count,
        double wheel1count, double wheel2count,
        double timeoutS) {
            if (opModeIsActive()) {
                double w1t = greg.wheel1.getCurrentPosition() + (int)wheel1count;
                double w2t = greg.wheel2.getCurrentPosition() + (int)wheel2count;
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
                runtime.reset();
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

    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }

    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.8f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 320;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);
    }

}

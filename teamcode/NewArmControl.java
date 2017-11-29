package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
    //TODO setJoystickDeadzone(float deadzone)
    // joystickDeadzone
@TeleOp
public class NewArmControl extends LinearOpMode{
    private NewArmMath3 am = new NewArmMath3(true);
    public void runOpMode() throws InterruptedException {
        initializeHardware();
        waitForStart();
        
        boolean[] lastDown = new boolean[4];
        boolean drive = true;
        while (opModeIsActive()) {
            boolean[] down = {gamepad2.y, gamepad2.b, gamepad2.a, gamepad2.x};
            boolean[] pressed = new boolean[4];
            for(int i = 0; i < 4; i++){pressed[i] = down[i] && ! lastDown[i];}
            lastDown = down;
            double[] input = {gamepad2.left_stick_x, gamepad2.left_stick_y, 
                            gamepad2.right_stick_x, gamepad2.right_stick_y};
                
            if (pressed[0])
                drive = !drive;
            if (pressed[1])
                am.resetWrist();
            if (pressed[2])
                am.zeroJoystick(input);
            if (pressed[3])
                am.resetPosition();
            
            if(drive){
                leftDrive.setPower(-gamepad2.left_stick_y);
                rightDrive.setPower(-gamepad2.right_stick_y);
            }else{
                am.update(input);
                waist.setTargetPosition((int)Math.round(am.waist));
                shoulder.setTargetPosition((int)Math.round(am.shoulder));
                elbow.setTargetPosition((int)Math.round(am.elbow));
                wrist.setPosition(am.wrist);
                leftDrive.setPower(0);
                rightDrive.setPower(0);
                
                telemetry.addData("current", waist.getCurrentPosition()+", "+shoulder.getCurrentPosition()+", "+elbow.getCurrentPosition());
                telemetry.addData("target", waist.getTargetPosition()+", "+shoulder.getTargetPosition()+", "+elbow.getTargetPosition());
                telemetry.update();
            }
            //telemetry.addData("pos", gamepad2.right_stick_x);
            //telemetry.update();
            if(gamepad2.right_trigger > .8){
                clawLeft.setPosition(1);
                clawRight.setPosition(1);
            }else if(gamepad2.right_trigger > .05){
                clawLeft.setPosition(.65);
                clawRight.setPosition(.65);
            }else{
                clawLeft.setPosition(.3);
                clawRight.setPosition(.3);
            }
        }
    }
    
    private DcMotor shoulder;
    private DcMotor elbow;
    private DcMotor waist;
    private DcMotor leftDrive;
    private DcMotor rightDrive;
    private Servo wrist;
    private Servo clawLeft;
    private Servo clawRight;
    private void initializeHardware()
    {
        waist = hardwareMap.get(DcMotor.class, "hor");
        shoulder = hardwareMap.get(DcMotor.class, "shoulder");
        elbow = hardwareMap.get(DcMotor.class, "elbow");
        System.out.println("H");
        waist.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shoulder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        elbow.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        waist.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        waist.setDirection(DcMotor.Direction.REVERSE);
        shoulder.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        shoulder.setDirection(DcMotor.Direction.REVERSE);
        elbow.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        elbow.setDirection(DcMotor.Direction.REVERSE);
        waist.setPower(0);
        shoulder.setPower(0);
        elbow.setPower(0);
        
        leftDrive = hardwareMap.get(DcMotor.class, "leftDrive");
        rightDrive = hardwareMap.get(DcMotor.class, "rightDrive");
        rightDrive.setDirection(DcMotor.Direction.REVERSE);
        wrist = hardwareMap.get(Servo.class, "wrist");
        clawLeft = hardwareMap.get(Servo.class, "clawLeft");
        clawRight = hardwareMap.get(Servo.class, "clawRight");
        clawRight.setDirection(Servo.Direction.REVERSE);
    }
}
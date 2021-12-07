package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@TeleOp(name="Java Teleop Program", group="Iterative OpMode")

public class Teleop2021 extends OpMode{
    
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor rearLeft;
    private DcMotor rearRight;
    
    private DcMotor elevator;
    
    private Servo bucketTilt;
    
    private DcMotor intake;
    
    private DcMotor spinner;
    
    private final int elevatorTop = -900;
    private final int elevatorLevelTwo = -517;
    private final int elevatorLevelOne = -259;
    private final int elevatorBottom = 0;
    
    private double spinnerSpeed = 0;
    private final double spinnerLowSpeed = 0.05;
    private final double spinnerHighSpeed = 0.5;
    private final double spinnerSpeedIncrease = 0.025;
    
    private final double maxSpeed = 0.75;
    
    @Override
    public void init(){
        
        frontLeft  = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        rearLeft = hardwareMap.get(DcMotor.class, "rearLeft");
        rearRight = hardwareMap.get(DcMotor.class, "rearRight");
        
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        rearLeft.setDirection(DcMotor.Direction.REVERSE);
        rearRight.setDirection(DcMotor.Direction.FORWARD);
        
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
        intake = hardwareMap.get(DcMotor.class, "intake");
        intake.setDirection(DcMotor.Direction.REVERSE);
        
        spinner = hardwareMap.get(DcMotor.class, "spinner");
        spinner.setDirection(DcMotor.Direction.FORWARD);
        
        elevator = hardwareMap.get(DcMotor.class, "elevator");
        elevator.setDirection(DcMotor.Direction.FORWARD);
        elevator.setPower(0);        
        elevator.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        
        bucketTilt = hardwareMap.get(Servo.class, "bucketTilt");
        bucketTilt.setDirection(Servo.Direction.FORWARD);
        
        telemetry.addData("Status", "Initialized");
    }
    
    @Override
    public void start(){
    }
    
    @Override
    public void loop(){
        
    telemetry.addData("Elevator Position", elevator.getCurrentPosition());     
    telemetry.update();
    
    double drive = -gamepad1.left_stick_y;
    double strafe = gamepad1.left_stick_x;
    double turn = gamepad1.right_stick_x;
        
    drive = drive * drive * Math.signum(drive);
    strafe = strafe * strafe * Math.signum(strafe);
    turn = turn * turn * Math.signum(turn);
        
    double denominator = Math.max(Math.abs(drive) + Math.abs(strafe) + Math.abs(turn), 1);
    frontLeft.setPower(maxSpeed*(drive + strafe + turn)/denominator);
    frontRight.setPower(maxSpeed*(drive - strafe - turn)/denominator);
    rearLeft.setPower(maxSpeed*(drive - strafe + turn)/denominator);
    rearRight.setPower(maxSpeed*(drive + strafe - turn)/denominator);
        
    if(gamepad1.left_trigger > 0.5) {
        intake.setPower(1);
    } else {
        intake.setPower(0);
    }
    
    if(gamepad1.left_bumper) {
        intake.setPower(-1);
    } else {
        intake.setPower(0);
    }
        
    if(gamepad2.b){
        if((spinnerSpeed >= spinnerLowSpeed) && (spinnerSpeed < spinnerHighSpeed)){
            spinnerSpeed = spinnerSpeed + spinnerSpeedIncrease;
        } else if (spinnerSpeed >= spinnerHighSpeed) {
            spinnerSpeed = spinnerHighSpeed;
        } else {
            spinnerSpeed = spinnerLowSpeed;
        }
        spinner.setPower(spinnerSpeed);
    } else {
        spinner.setPower(0);
    }
  
    if(gamepad2.a) {
            bucketTilt.setPosition(0.85);
    } else {
            bucketTilt.setPosition(0.47);
    }

    if(gamepad2.right_trigger > 0.5) {
        elevator.setTargetPosition(elevatorTop);
        elevator.setPower(0.25);
        elevator.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    } else if(gamepad2.y) {
        elevator.setTargetPosition(elevatorLevelTwo);
        elevator.setPower(0.25);
        elevator.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    } else if(gamepad2.x) {
        elevator.setTargetPosition(elevatorLevelOne);
        elevator.setPower(0.25);
        elevator.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    } else if(gamepad2.right_bumper) {
        elevator.setTargetPosition(elevatorBottom);
        elevator.setPower(0.25);
        elevator.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
    }
}
        


/*
 * @author:Matthew Stafford
 * @email:matthewstafford29@gmail.com
 * @home phone:(585)889-1744
 * @cell phone:(585)690-5034
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;
import java.io.IOException;

/**
 * Class Drive
 */
public class Drive {

    private static final double PID_OUTPUT_RANGE_MIN = 0xffffffff;
    private static final double PID_OUTPUT_RANGE_MAX = 0x1;
    //
    // Fields
    //
    private RobotOut rOut;
    private RobotIn rIn;
    private DSIn dsIn;
    private AutoBalance balance;
    private RobotDrive robotDrive;
    private Joystick joystick;
    // PID Control properties
    private PIDController leftFrontPID;
    private PIDController rightFrontPID;
    private PIDController leftRearPID;
    private PIDController rightRearPID;
    private final double kP = 0.1;
    private final double kI = 0.001;
    private final double kD = 0.0;
    private double PIDTolerance = 0xa;
    private final double maxCurrent = 0x28;
    private final double speedScaler = 0xfa;
    private final double deadzone = .2;
    private boolean sensitive = false;
    private double magnitude = 0x0;
    private boolean isEncoderEnable = false; //False = Voltage Control, True = Encoder
    private boolean isCurrentControlOn = false;
    private boolean isFirstManual = true;
    private boolean isFirstTimedGoLoop = true;

    //
    // Constructors
    //
    /**
     *
     * @param myrOut
     * @param myDsIn
     * @param myrIn
     */
    public Drive(RobotOut rOut, DSIn dsIn, RobotIn rIn, AutoBalance balance) {
        this.dsIn = dsIn;
        this.rOut = rOut;
        this.rIn = rIn;
        this.balance = balance;

        rIn.leftDriveEnc.setPIDSourceParameter(Encoder.PIDSourceParameter.kRate);
        rIn.rightDriveEnc.setPIDSourceParameter(Encoder.PIDSourceParameter.kRate);

        rightFrontPID = new PIDController(kP, kI, kD, rIn.rightDriveEnc, rOut.rightJaguarFront);
        rightRearPID = new PIDController(kP, kI, kD, rIn.rightDriveEnc, rOut.rightJaguarBack);
        leftFrontPID = new PIDController(kP, kI, kD, rIn.leftDriveEnc, rOut.leftJaguarFront);
        leftRearPID = new PIDController(kP, kI, kD, rIn.leftDriveEnc, rOut.leftJaguarBack);

        leftFrontPID.setTolerance(PIDTolerance);
        leftRearPID.setTolerance(PIDTolerance);
        rightFrontPID.setTolerance(PIDTolerance);
        rightRearPID.setTolerance(PIDTolerance);
    }

    //
    // Methods
    //
    /**
     *
     */
    public void humanControl() {
        try
        {
//            rOut.leftJaguarBack.configNeutralMode(CANJaguar.NeutralMode.kCoast);
//            rOut.rightJaguarBack.configNeutralMode(CANJaguar.NeutralMode.kCoast);
//            rOut.rightJaguarFront.configNeutralMode(CANJaguar.NeutralMode.kCoast);
//            rOut.leftJaguarFront.configNeutralMode(CANJaguar.NeutralMode.kCoast); 
        }
        catch (Exception e)
        {
            System.out.println("error when changing to coast mode");           
            e.printStackTrace();            
        }
        
        if (isFirstManual == true) {
            this.currentControl();
            this.encoderControl();
            isFirstManual = false;
        }
        
        //what does this do? it looks important but very cryptic. ROB?
        magnitude = Math.sqrt((dsIn.getArcadeJoyXRightDriver() * dsIn.getArcadeJoyXRightDriver()
                + (dsIn.getArcadeJoyYRightDriver() * dsIn.getArcadeJoyYRightDriver())));
        
//        if(dsIn.getAutoBalance() == true)
//        {
//            //this.arcadeDrive(balance.getTiltCmdVal(), 0.0, sensitive);
//            balance.prnVals();
//        }
//        else
//        {    
            if (magnitude > deadzone) {
                this.arcadeDrive((dsIn.getArcadeJoyYRightDriver() * 0.5), (dsIn.getArcadeJoyXRightDriver() * 0.7), true);
            } else {
                this.arcadeDrive(dsIn.getArcadeJoyYLeftDriver(), dsIn.getArcadeJoyXLeftDriver(), true);
            }
//        }
        shiftControl();

    }
/**
 * 
 * @param speed
 * @param dir
 * @param goTime
 * @param MyTimer
 * @return if time is up 
 */
    public boolean TimedGo(double speed, double dir, double goTime, Timer MyTimer) {
        
        if(isFirstTimedGoLoop == true){
            MyTimer.reset();
            MyTimer.start();
            this.arcadeDrive(speed, dir,true);
            isFirstTimedGoLoop = false;
            System.out.println(" Going ");
            return false;
        } else if(MyTimer.get()<= goTime){
            this.arcadeDrive(speed, dir,false);
            System.out.println(" Going ");
            return false;

        } else {
            MyTimer.stop();
            MyTimer.reset();
            this.arcadeDrive(0, 0, true);
            isFirstTimedGoLoop = true;
            System.out.println(" stop ");
            return true;
        }
        
        //return done;
    }


    /**
     *
     */
    private void currentControl() {
        if (isCurrentControlOn == true) {
            try {
//            rOut.leftJaguarBack.disableControl();
//                rOut.leftJaguarBack.changeControlMode(CANJaguar.ControlMode.kCurrent);
//                rOut.leftJaguarBack.enableControl();
//            rOut.leftJaguarFront.disableControl();
//                rOut.leftJaguarFront.changeControlMode(CANJaguar.ControlMode.kCurrent);
//                rOut.leftJaguarFront.enableControl();
//            rOut.rightJaguarBack.disableControl();
//                rOut.rightJaguarBack.changeControlMode(CANJaguar.ControlMode.kCurrent);
//                rOut.rightJaguarBack.enableControl();
//            rOut.rightJaguarFront.disableControl();
//                rOut.rightJaguarFront.changeControlMode(CANJaguar.ControlMode.kCurrent);
//                rOut.rightJaguarFront.enableControl();
            } catch (Exception e) {
                //System.out.println("Exception on Enable current PID enable");
            }
            leftFrontPID.setOutputRange(-maxCurrent, maxCurrent);
            leftRearPID.setOutputRange(-maxCurrent, maxCurrent);
            rightFrontPID.setOutputRange(-maxCurrent, maxCurrent);
            rightRearPID.setOutputRange(-maxCurrent, maxCurrent);
        } else {
            try {
//                rOut.leftJaguarBack.disableControl();
//                rOut.leftJaguarBack.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
//                rOut.leftJaguarFront.disableControl();
//                rOut.leftJaguarFront.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
//                rOut.rightJaguarBack.disableControl();
//                rOut.rightJaguarBack.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
//                rOut.rightJaguarFront.disableControl();
//                rOut.rightJaguarFront.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            } catch (Exception e) {
                //System.out.println("Exception on Enable voltage PID enable");
            }
            leftFrontPID.setOutputRange(PID_OUTPUT_RANGE_MIN, PID_OUTPUT_RANGE_MAX);
            leftRearPID.setOutputRange(PID_OUTPUT_RANGE_MIN, PID_OUTPUT_RANGE_MAX);
            rightFrontPID.setOutputRange(PID_OUTPUT_RANGE_MIN, PID_OUTPUT_RANGE_MAX);
            rightRearPID.setOutputRange(PID_OUTPUT_RANGE_MIN, PID_OUTPUT_RANGE_MAX);
        }
    }


    /**
     * This method converts joystick inputs, or scaled joystick inputs to values
     * that can be applied left or right motors
     *
     * @param moveValue
     * @param rotateValue
     * @param squaredInputs
     */
    public void arcadeDrive(double moveValue, double rotateValue, boolean squaredInputs) {
        // local variables to hold the computed PWM values for the motors
        double leftMotorSpeed;
        double rightMotorSpeed;

        if (squaredInputs) {
            // square the inputs (while preserving the sign) to increase fine control while permitting full power
            try
            {
//                rOut.leftJaguarBack.configNeutralMode(CANJaguar.NeutralMode.kBrake);
//                rOut.rightJaguarBack.configNeutralMode(CANJaguar.NeutralMode.kBrake);
//                rOut.rightJaguarFront.configNeutralMode(CANJaguar.NeutralMode.kBrake);
//                rOut.leftJaguarFront.configNeutralMode(CANJaguar.NeutralMode.kBrake); 
            }
            catch (Exception e)
            {
                //System.out.println("Exception on public void humanControl");           
            }            
            if (moveValue >= 0.0) {
                moveValue = (moveValue * moveValue);
            } else {
                moveValue = -(moveValue * moveValue);
            }
            if (rotateValue >= 0.0) {
                rotateValue = (rotateValue * rotateValue);
            } else {
                rotateValue = -(rotateValue * rotateValue);
            }
        }
        else
        {
            try
            {
//                rOut.leftJaguarBack.configNeutralMode(CANJaguar.NeutralMode.kCoast);
//                rOut.rightJaguarBack.configNeutralMode(CANJaguar.NeutralMode.kCoast);
//                rOut.rightJaguarFront.configNeutralMode(CANJaguar.NeutralMode.kCoast);
//                rOut.leftJaguarFront.configNeutralMode(CANJaguar.NeutralMode.kCoast); 
            }
            catch (Exception e)
            {
                System.out.println("Error setting to coast in arcadeDrive()");
                e.printStackTrace();           
            }            
        }

        if (moveValue > 0.0) {
            
            if (rotateValue > 0.0) {
                leftMotorSpeed = moveValue - rotateValue;
                rightMotorSpeed = Math.max(moveValue, rotateValue);
            } else {
                leftMotorSpeed = Math.max(moveValue, -rotateValue);
                rightMotorSpeed = moveValue + rotateValue;
            }
        } else {
            if (rotateValue > 0.0) {
                leftMotorSpeed = -Math.max(-moveValue, rotateValue);
                rightMotorSpeed = moveValue + rotateValue;
            } else {
                leftMotorSpeed = moveValue - rotateValue;
                rightMotorSpeed = -Math.max(-moveValue, -rotateValue);
            }
        }
        this.setLeftRightMotorOutputs(leftMotorSpeed, -rightMotorSpeed); //rightMotorSpeed negated.
    }

    private void setLeftRightMotorOutputs(double leftOutput, double rightOutput) {
        if (isEncoderEnable == true) {
            rightFrontPID.setSetpoint(rightOutput * speedScaler);
            rightRearPID.setSetpoint(rightOutput * speedScaler);
            leftFrontPID.setSetpoint(leftOutput * speedScaler);
            leftRearPID.setSetpoint(leftOutput * speedScaler);
          //  System.out.println("1. rightOutput = " + rightOutput);
        } else {
                rOut.leftJaguarBack.set(leftOutput);
                rOut.leftJaguarFront.set(leftOutput);
                rOut.rightJaguarBack.set(rightOutput);
                rOut.rightJaguarFront.set(rightOutput);
            //    System.out.println("2. rightOutput = " + rightOutput);
        }
    }    

    private void encoderControl() {
        if (isEncoderEnable == true) {
            rightFrontPID.enable();
            leftFrontPID.enable();
            rightRearPID.enable();
            leftRearPID.enable();
        } else {
            isEncoderEnable = false;
            rightFrontPID.disable();
            leftFrontPID.disable();
            rightRearPID.disable();
            leftRearPID.disable();
        }
    }


    public void shiftControl() {
        if (dsIn.getShifterValue() == true) {
            this.shiftHigh();
        } else if (dsIn.getShifterValue() == false) {
            this.shiftLow();
        }
    }


    private void shiftHigh() {
        rOut.setShiftActuator(true);
        //System.out.println("shiftHigh");
    }

    private void shiftLow() {
        rOut.setShiftActuator(false);
        //System.out.println("shiftLow");
    }
}

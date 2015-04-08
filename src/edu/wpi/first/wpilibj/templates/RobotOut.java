/*
 * 
 * 
 * 
 * 
 * 
 * 
 * @author:Adam Audycki
 * @email:aaudycki@gmail.com
 * @home phone:(585)594-3081
 * @cell phone:(585)520-1246
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

/**
 * Class RobotOut
 */
public class RobotOut {

    private static final double JAGUAR_TIMEOUT = 0.75;
    //
    // Fields
    //
    public Victor harvesterRoller; //port 1
    public Jaguar armDrive;//port 2
    public Jaguar rightJaguarFront; //port 3
    public Jaguar rightJaguarBack; //port 4
    public Jaguar leftJaguarFront; //port 5
    public Jaguar leftJaguarBack;  //port 6
    public DoubleSolenoid harvesterTilt; //channel 1,2
    public Solenoid gripperControl; //channel 3,4
    private Compressor compressor;// channel digital-14,replay-1
    public Solenoid shiftActuator;
    public DoubleSolenoid activeFloor; //the weird solenoid thing that is under the gripper.
    public Solenoid stingerControl;
    
    private boolean competitionRobot = true;
    
    public Solenoid activeFloorSingle;
    public Solenoid harvesterTiltSingle;    
    //TODO Changed activeFloor, harvesterTilt, gripperControl back to private
    /*
     * Motor Values
     */
    private final double BALL_PROCESS_ON = 1.0;
    private final double BALL_PROCESS_OFF = 0.0;
    private final double BALL_PROCESS_REVERSE = -1.0;
    public int harvesterState;
    // Constructors
    //

    public RobotOut() {

        harvesterRoller = new Victor(1);

        try {

            armDrive = new Jaguar(2);
            rightJaguarFront = new Jaguar(3);
            rightJaguarBack = new Jaguar(4);
            leftJaguarFront = new Jaguar(5);
            leftJaguarBack = new Jaguar(6);

            // Adjust default timout - pre Rob (gs)
            leftJaguarFront.setExpiration(JAGUAR_TIMEOUT);
            leftJaguarBack.setExpiration(JAGUAR_TIMEOUT);
            rightJaguarFront.setExpiration(JAGUAR_TIMEOUT);
            rightJaguarBack.setExpiration(JAGUAR_TIMEOUT);
        } catch (Exception e) {
            System.err.println("Jaguar(s) failed on instantiation!");
            DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Jaguar failed instantiation!");
            DriverStationLCD.getInstance().updateLCD();
        }

        if(competitionRobot == true)
        {    
        harvesterTilt = new DoubleSolenoid(5,7); //CompBot: 5,7 PracBot: 1,2
        gripperControl = new Solenoid(6); //CompBot: 6
        shiftActuator = new Solenoid(8); //CompBot: 8
        stingerControl = new Solenoid(3);
        activeFloor = new DoubleSolenoid(1, 2); //CompBot: 1,2
        }
        else if(competitionRobot == false)
        {    

//        gripperControl = new Solenoid(6);
//        shiftActuator = new Solenoid(8);
        }
        //activeFloorSingle = new Solenoid(5);        
        //harvesterTiltSingle = new Solenoid(5);
        
        compressor = new Compressor(14, 8); //Pressure Switch, Relay
        compressor.start();
    }

    //
    // Methods
    //
    /**
     * Opens the ball cage.
     *
     * @param none
     * @return void
     */
    
    public void stopCompressor()
    {
        compressor.stop();
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser2, 1, "Compressor Stopped");
        DriverStationLCD.getInstance().updateLCD();
    }
    public void startCompressor()
    {
        compressor.start();
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser2, 1, "Compressor Started");
        DriverStationLCD.getInstance().updateLCD();
    }
    
    public void openGripper() {
        gripperControl.set(false);
        System.out.println("Ball cage open");
    }

    /**
     * Closes the ball cage.
     *
     * @param none
     * @return void
     */
    public void closeGripper() {
        gripperControl.set(true);
        System.out.println("Ball cage closed");
    }
    /*
     * set the ball motor on.
     */

    public void setRollerMotorOn() {
        harvesterRoller.set(BALL_PROCESS_ON);
    }
    /*
     * turn ball motor off.
     */

    public void setRollerMotorOff() {
        harvesterRoller.set(BALL_PROCESS_OFF);
    }

    /*
     * reverse the ball motor.
     */
    public void setRollerMotorReverse() {
        harvesterRoller.set(BALL_PROCESS_REVERSE);
    }

    /**
     * Sets an int to determine if the ball processor is deployed for BallScorer
     * logic.
     */
    public void determineHarvesterState() {
        if (harvesterTilt.get() == Value.kReverse) /*harvesterTiltSingle.get() == false)*/{
            harvesterState = 0; //Out
        } else {
            harvesterState = 1; //Inside
        }
    }

    /**
     *
     */
    public void setActiveFloorUp() {
        activeFloor.set(DoubleSolenoid.Value.kForward);
        //activeFloorSingle.set(true);
    }

    /**
     *
     */
    public void setActiveFloorDn() {
        activeFloor.set(DoubleSolenoid.Value.kReverse);
        //activeFloorSingle.set(false);
    }

    /*
     * tilt out
     */
    public void deployHarvester() {
        harvesterTilt.set(Value.kReverse);
        //harvesterTiltSingle.set(false);        
    }
    /*
     * retracts ballprocessor
     */

    public void retractHarvester() {
        harvesterTilt.set(Value.kForward);
        //harvesterTiltSingle.set(true);
    }
    
    /*
     * deploy stinger
     */
    public void deployStinger() {
        stingerControl.set(true);
        System.out.println("Deploy Stinger");
    }
    /*
     * retract stinger
     */

    public void retractStinger() {
        stingerControl.set(false);
        System.out.println("Retract Stinger");
    }    

    //
    // Other methods
    //

    /*
     * get Jaguar Value.
     */
    public double getLeftJagFront() {
            return leftJaguarFront.get();
    }
    
    public double getArmJag() {
            return armDrive.get();
    }

    public double getLeftJagBack() {
            return leftJaguarBack.get();
    }

    public double getRightJagFront() {
            return rightJaguarFront.get();
    }

    public double getRightJagBack() {
            return rightJaguarBack.get();
    }

    public void setLeftDrive(double speed) {
            leftJaguarFront.set(speed);
            leftJaguarBack.set(speed);
    }

    public void setRightDrive(double speed) {
            rightJaguarFront.set(speed);
            rightJaguarBack.set(speed);
    }

    /**
     * @param val
     */
    public void setArmMotorValue(double val) {
            armDrive.set(val);
    }

    /**
     *
     * @return
     */
    public boolean getCompressorState() {
        return compressor.getPressureSwitchValue();
    }

    /**
     * true is low gear
     * false is high gear
     * @param state 
     */
    public void setShiftActuator(boolean state) {
        if (state) {
            shiftActuator.set(true);
        } else {
            shiftActuator.set(false);
        }
    }

    /**
     *
     * @return
     */
    public boolean isHighGear() {
        if (shiftActuator.get() == false) {
            return false;
        }
        return true;
    }
}

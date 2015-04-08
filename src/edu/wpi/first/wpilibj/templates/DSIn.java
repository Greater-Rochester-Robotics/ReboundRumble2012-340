package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Class DSIn
 *
 */
public class DSIn {

    private static final int JOY_X_LEFT = 1;
    private static final int JOY_X_RIGHT = 4;
    private static final int JOY_Y_LEFT = 2;
    private static final int JOY_Y_RIGHT = 5;
    private static final int TRIGGERS = 3;
    private static final double TRIGGER_DEADZONE = .05;
    public static boolean highGear = true; //Default of True = high gear

    /**
     *
     */
    public Joystick driverXBox;
    /**
     *
     */
    public Joystick coDriverXBox;
    private static DriverStationEnhancedIO osBox;
    private SmartDashboard smartDash = new SmartDashboard();
    /**
     *
     */
    //DSEIO digital inputs
    private static boolean armUpButton;
    private static boolean armDnButton;
    private static boolean gripperOpenButton;
    private static boolean gripperClosedButton;
    private static boolean harvestInButton;
    private static boolean harvestOutButton;
    private static boolean rollerOnButton;
    private static boolean rollerOffButton;
    /**
     *
     */
    public DSIn() {
        driverXBox = new Joystick(1);
        coDriverXBox = new Joystick(2);
        osBox = DriverStation.getInstance().getEnhancedIO();
        smartDash.putBoolean("has_target", false);
        smartDash.putDouble("X_Position", 0);
        smartDash.putDouble("Y_Position", 0);
        smartDash.putDouble("Distance", 0);
        smartDash.putDouble("Offset", 0);
        smartDash.putInt("AutonomousInt", 0);
        try {
            armUpButton = osBox.getDigital(2);
            armDnButton = osBox.getDigital(1);
            gripperOpenButton = osBox.getDigital(3);
            gripperClosedButton = osBox.getDigital(4);
            harvestInButton = osBox.getDigital(7);
            harvestOutButton = osBox.getDigital(8);
            rollerOnButton = osBox.getDigital(5);
            rollerOffButton = osBox.getDigital(6);
        } catch (DriverStationEnhancedIO.EnhancedIOException enhancedIOException) {
            enhancedIOException.printStackTrace();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
/********************************Driver Buttons********************************/
    public boolean harvestButtonDriver()
    {
        return driverXBox.getRawButton(1); //A Button
    }
    
    public boolean stopHarvestButtonDriver()
    {
        return driverXBox.getRawButton(2); //B Button
    }
    
    public boolean startCompressorButtonDriver()
    {
        return driverXBox.getRawButton(3); //X
    }
    public boolean stopCompressorButtonDriver()
    {
        return driverXBox.getRawButton(4); //Y
    }

    public boolean getShifterValue() 
    {
         if (driverXBox.getRawAxis(TRIGGERS) > TRIGGER_DEADZONE) 
         {
            highGear = true;
         }
         else if (driverXBox.getRawAxis(TRIGGERS) < -TRIGGER_DEADZONE) 
         {
            highGear = false;
         }
        return highGear;            
    }
    public double getArcadeJoyYLeftDriver() 
    {
        return driverXBox.getRawAxis(2); //Doesn't need to be multiplied by -1
    }
    public double getArcadeJoyYRightDriver()
    {
        return driverXBox.getRawAxis(5); //Doesn't need to be multiplied by -1
    }
    public double getArcadeJoyXLeftDriver() 
    {
        return driverXBox.getRawAxis(JOY_X_LEFT);
    }
    public double getArcadeJoyXRightDriver()
    {
        return driverXBox.getRawAxis(JOY_X_RIGHT);
    }
    public double getArcadeJoyYLeftCoDriver()
    {
        return (coDriverXBox.getRawAxis(JOY_Y_LEFT) * -1);
    }
    public double getArcadeJoyYRightCoDriver()
    {
        return (coDriverXBox.getRawAxis(JOY_Y_RIGHT) * -1);
    }
    public double getArcadeJoyXLeftCoDriver()
    {
        return coDriverXBox.getRawAxis(JOY_X_LEFT);
    }
    public double getArcadeJoyXRightCoDriver()
    {
        return coDriverXBox.getRawAxis(JOY_X_RIGHT);
    }    
    public boolean getAutoBalance()
    {
        if(driverXBox.getRawButton(5) && driverXBox.getRawButton(6))
        {
            return true;
        }
        else
        {
            return false;
        }    
    }        
/******************************Co-Driver Buttons*******************************/    
    public boolean harvestButtonCoDriver()
    {
        return coDriverXBox.getRawButton(1); //A Button
    }
    public boolean stopHarvestButtonCoDriver()
    {
        return coDriverXBox.getRawButton(2); //B Button
    }
    public boolean scoringButtonCoDriver()
    {
        return coDriverXBox.getRawButton(6);
    }
    public boolean sendArmDownButtonCoDriver()
    {
        return coDriverXBox.getRawButton(5); 
    }
    public boolean openGripperButtonCoDriver()
    {
        return coDriverXBox.getRawButton(3); //X Button
    }
    public boolean armToLow2PosCoDriver()
    {
        return coDriverXBox.getRawButton(4); //Y Button
    }        
    public boolean cameraFrontButtonCoDriver()
    {
        return coDriverXBox.getRawButton(8); //Start Button
    }
    public boolean cameraBackButtonCoDriver()
    {
        return coDriverXBox.getRawButton(7); //Back Button
    }
/****************************Manual Override Buttons***************************/
    public boolean getArmUpButton()
    {
        try{
            return  !osBox.getDigital(1);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public boolean getArmDnButton()
    {
        try{
            return  !osBox.getDigital(2);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public boolean getGripperOpenButton()
    {
        try{
            return  !osBox.getDigital(3);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public boolean getGripperCloseButton()
    {
        try{
            return  !osBox.getDigital(4);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public boolean getHarvestInButton()
    {
        try{
            return  !osBox.getDigital(7);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public boolean getHarvestOutButton()
    {
        try{
            return  !osBox.getDigital(8);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public boolean getRollerOnButton()
    {
        try{
            return  !osBox.getDigital(6);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public boolean getRollerOffButton()
    {
        try{
            return  !osBox.getDigital(5);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    
//////////// SmartDashboard feed back
    public boolean getHasTarget(){
        try{
            return smartDash.getBoolean("has_target");
        }catch(Exception e){
            return false;
        }
    }
    public double getCameraXPosition(){
        try{
            return smartDash.getDouble("X_Position");
        }catch(Exception e){
            return 0.0;
        }
    }
    /*
     * this returns the autonomous state that the dashboard thinks it is in.
     * numbers could range from 0 to 4
     */
    public int getAutonomousState(){
         try{
            return smartDash.getInt("AutonomousInt");
        }catch(Exception e){
            return 0;
        }
    }   
}
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    //    public boolean getShootButton2() {
//        return coDriverXBox.getRawButton(1);
//    }
//
//    /**
//     * @return boolean
//     */
//    public boolean getBallSuckerButton2() {
//        return coDriverXBox.getRawButton(2);
//    }
//
//    /**
//     * @return boolean
//     */
//    public boolean getBridgeTilterButton2() {
//        return coDriverXBox.getRawButton(3);
//    }
//
//    /**
//     * @return boolean
//     */
//    public boolean getArmUpButton2() {
//        return coDriverXBox.getRawButton(4);
//    }
//
//    /**
//     * @return boolean
//     */
//    public boolean getSuckerUpButton2() {
//        return coDriverXBox.getRawButton(5);
//    }
//
//    //BallScorer Buttons
//    /**
//     *
//     * @return
//     */
//    public boolean armUpButton() {
//        return driverXBox.getRawButton(1); //Button A
//    }
//
//    /**
//     *
//     * @return
//     */
//    public boolean armDownButton() {
//        return driverXBox.getRawButton(2); //Button B
//    }
//
//    /**
//     *
//     * @return
//     */
//    public boolean armMidButton() {
//        return driverXBox.getRawButton(3); //Button X
//    }
//
//    /**
//     *
//     * @return
//     */
//    public boolean openCage() {
//        return driverXBox.getRawButton(4); //Button Y
//    }
//
//    /**
//     *
//     * @return
//     */
//    public boolean closeCage() {
//        return driverXBox.getRawButton(5); //Top Left Trigger Button
//    }
//
//    //BallProcessor Buttons
//    /**
//     *
//     * @return
//     */
//    public boolean deployProcessor() {
//        return driverXBox.getRawButton(6); //Top Right Trigger Button
//    }
//
//    /**
//     *
//     * @return
//     */
//    public boolean retractProcessor() {
//        return driverXBox.getRawButton(7); //Back Button
//    }
//
//    boolean getAutoSwitch() {
//        return autoSwitch1;
//    }


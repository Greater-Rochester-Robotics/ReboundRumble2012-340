/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.IterativeRobot;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends IterativeRobot {

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    private final int samples = 8;
    private int itr;
    
    public RobotIn rIn = new RobotIn();
    public RobotOut rOut = new RobotOut();
    public DSIn dsIn = new DSIn();
    public AutoBalance balance = new AutoBalance(rIn, samples);    
    public Drive drive = new Drive(rOut, dsIn, rIn, balance);
    public BallHandler handler = new BallHandler(rIn, rOut, dsIn);
    public optonomousPrime optPrime = new optonomousPrime(rOut, rIn, dsIn, drive, handler);    
    public DSOut dsOut = new DSOut(rIn, rOut,dsIn, optPrime, balance);
    
    public void robotInit() {
        System.out.print("robot init running");
        itr = 0;
    }
    public void disabledPeriodic(){
        //System.out.print("disabled periodic  running");
        dsOut.updateDash();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {

        optPrime.runAutonomous(optPrime.stage);
          System.out.println("\t3 Rob Switch: " + dsIn.getAutonomousState());        
        dsOut.updateDash();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
//        if(itr < samples)
//        {
//            balance.getVals();
//            itr++;
//        }
//        else
//        {
//            balance.getVals();
//        }
        
        
//        if(rOut.stingerControl.get() == true) //Deployed
//        {
//            balance.areWeBalanced();    
//        }
//        else
//        {
//            //Do Nothing
//        }
        
//        if(balance.areWeBalanced() == false)
//        {
//            if(dsIn.driverXBox.getRawButton(1))
//            {
//                balance.getTiltCmdVal();
//            }    
//        }
//        else
//        {
//                rOut.setLeftDrive(0.0);
//                rOut.setRightDrive(0.0);
//        }
        if(dsIn.stopCompressorButtonDriver() == true)
        {
            rOut.stopCompressor();
        }
        if(dsIn.startCompressorButtonDriver() == true)
        {
            rOut.startCompressor();
        }
        
        if(dsIn.coDriverXBox.getRawButton(8) == true)
        {
            rOut.openGripper();
            rOut.setActiveFloorDn();
        }
        

        drive.humanControl();
        handler.manualOverride();
        drive.shiftControl();
        dsOut.updateDash();
        //balance.prnVals();

        System.out.println("Arm Pot: " + rIn.getArmPositionPotValue());
        //Open/Close gripper buttons on the driver gamepad.
        
        if(dsIn.coDriverXBox.getRawButton(3) && (rOut.gripperControl.get() == true))
        {
            rOut.openGripper();
        }
        else if(dsIn.coDriverXBox.getRawButton(3) && (rOut.gripperControl.get() == false))
        {
            rOut.closeGripper();
        }
        
//        if(dsIn.coDriverXBox.getRawButton(4) && (rOut.harvesterState == 0))
//        {
//            rOut.retractHarvester();
//        }
//        else if(dsIn.coDriverXBox.getRawButton(4) && (rOut.harvesterState == 1))
//        {
//            rOut.deployHarvester();
//        }
        
        if(dsIn.driverXBox.getRawButton(8))
        {
            rOut.deployStinger();
        }
        else if(dsIn.driverXBox.getRawButton(7))
        {
            rOut.retractStinger();        
        }        
        
    }
}

/**
 * @author: Ben Meyers
 * @email: meyers.bs@gmail.com
 * @iPod Number: 1 (585) 420-6635 (Texting)
 * @Phone: 1 (585) 576-6180 (Don't call unless you are dying,
 *         or dead, because I have extremely limited minutes)
 * 
 * @author: Adam Audycki
 * @email: aaudycki@gmail.com
 * @HomePhone: 1 (585) 594-3081
 * @CellPhone: 1 (585) 520-1246
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Timer;


public class BallHandler
{
    //Class Aliases
    public RobotIn rIn;
    public RobotOut rOut;
    public DSIn dsIn;
    
    //Speed Constants
    private final double SPEED_UP               =  1.0; //Speed Up
    private final double SPEED_UP_PRAC          =  1.00;//Speed Up Practice
    private final double SPEED_DN_PRAC          = -0.75;//Speed Down Practice        
    private final double SPEED_DN               = -0.50; //Speed Down
    private final double SPEED_DN_MAN           = -0.67;//Speed Down Manual
    private final double SPEED_ST               =  0.00; //Speed Stop
    private final double SPEED_UP_SL            =  0.50; //Speed Up Slow
    private final double SPEED_DN_SL            = -0.2; // -0.1 Speed Dn Slow
    
    //Potentiometer Constants For Competition Bot
    //TODO: Recalibrate
    private final double GRD_POS                =  4.88; //April 26 4.98 Feb 19
    private final double LOW_POS                =  4.35; //4.70 Aug 27 - 4.85 Feb 19
    private final double FEED_POS               =  3.20; //Guess 6-14-12
    private final double MIDDLE_POS             =  2.10; //2.35 Aug 27 - 2.40 Feb 19
    private final double TOP_POS                =  1.97; //1.99 Feb 19
    private final double SLOW_BEFORE_BOT_LIMIT  =  4.10; //4.20 Aug 27 - 4.85 Feb 19
    private final double SLOW_BEFORE_TOP_LIMIT  =  2.20; //2.29 Feb 19
    
    //Potentiometer Constants For Practice Bot
//    private final double GRD_POS                = 3.80;
//    private final double LOW_POS                = 3.28;
//    private final double FEED_POS               = 3.00; //GUESS    
//    private final double MIDDLE_POS             = 0.72;
//    private final double TOP_POS                = 0.30;
//    private final double SLOW_BEFORE_BOT_LIMIT  = 3.28;
//    private final double SLOW_BEFORE_TOP_LIMIT  = 0.45;   
    
    //Timer constants (Seconds)
    private final double DEPLOY_HARVESTER_DELAY = 0.25;
    private final double CLOSE_GRIP_DELAY       = 0.50;
    private final double RAISE_FLOOR_DELAY      = 0.50;
    private final double STOP_ARM_DELAY         = 3.00;
    private final double RETRACT_HARVESTER_DELAY= 2.50;
    
    //Switch States
    private int lastState                       = 0;
    private int currState                       = 0;
    
    //Special Booleans
    private boolean done                        = true;
    private boolean isHarvInBeforeGoToMid       = false; //(Was starWars)
    
    //Run Method Cases
    private final int Idle                  = 0;
    private final int harvest               = 1;
    private final int stopHarvester         = 2;
    private final int goToMid               = 3;
    private final int armDown               = 4;
    private final int gripperControl        = 5;
    private final int armToFeedPos          = 6;
    private final int case9                 = 9;
    private final int closeGripper          = 11;
    private final int armLow2               = 12;
    
    //Manual Override Method Cases
    private int manualState = 0;
    private final int harvestIn = 1;
    private final int harvestOut = 2;
    private final int rollerOn = 3;
    private final int rollerOff = 4;
    private final int openGrip = 5;
    private final int closeGrip = 6;
    
    //Objects
    public Timer myTimer;
    
    
    /**
     * Handles Aliases.
     * @param rin
     * @param rout
     * @param dsin
     * @param dsout 
     */
    public BallHandler(RobotIn rin, RobotOut rout, DSIn dsin)
    {
        this.rIn = rin;
        this.rOut = rout;
        this.dsIn = dsin;
        
        myTimer = new Timer();
        myTimer.start();
    }
    
    /**
     * Depending on the buttons pushed we will go to a certain case and do
     * stuff.If out state changes we will reset the timer.This handles all of
     * the code that has anything to do with balls.
     * @param none
     * @return void
     */
    public void gamepadControl()
    {
        if(this.done == true) //If we are done doing our last process.
        {
            //If no buttons are pressed.
            if(!dsIn.harvestButtonCoDriver() && !dsIn.stopHarvestButtonCoDriver() &&
                !dsIn.scoringButtonCoDriver() && !dsIn.sendArmDownButtonCoDriver() &&
                !dsIn.openGripperButtonCoDriver() && !dsIn.armToLow2PosCoDriver())
            {
                currState = Idle;
                this.done = true;
                System.out.println("No buttons pressed");
            }
            
            //If the harvestButtonCoDriver OR the deployProcessor button is the only one pressed.
            else if( (dsIn.harvestButtonCoDriver() || dsIn.harvestButtonDriver())
                    && !dsIn.stopHarvestButtonCoDriver() && !dsIn.scoringButtonCoDriver()
                    && !dsIn.sendArmDownButtonCoDriver() && !dsIn.openGripperButtonCoDriver()
                    && !dsIn.armToLow2PosCoDriver())
            {
                currState = harvest;
                this.done = false;
                System.out.println("harvestButton pressed");
            }
            
            //If the stopHarvestButtonCoDriver OR the retractProcessor button is the only one pressed.
            else if((dsIn.stopHarvestButtonCoDriver() ||
                    dsIn.stopHarvestButtonDriver()) && !dsIn.harvestButtonCoDriver()
                    && !dsIn.scoringButtonCoDriver() && !dsIn.sendArmDownButtonCoDriver()
                    && !dsIn.openGripperButtonCoDriver() && !dsIn.armToLow2PosCoDriver())
            {
                currState = stopHarvester;
                this.done = false;
                System.out.println("stopHarvestButton pressed");
            }
            
            //If the scoringButtonCoDriver is the only one pressed.
            else if(dsIn.scoringButtonCoDriver() && !dsIn.harvestButtonCoDriver() &&
                !dsIn.stopHarvestButtonCoDriver() && !dsIn.sendArmDownButtonCoDriver()
                && !dsIn.openGripperButtonCoDriver() && !dsIn.armToLow2PosCoDriver())
            {
                currState = goToMid;
                this.done = false;
                System.out.println("scoringButton pressed");
            }
            
            //If the sendArmDownButtonCoDriver is the only one pressed.
            else if(dsIn.sendArmDownButtonCoDriver() && !dsIn.harvestButtonCoDriver() &&
                !dsIn.scoringButtonCoDriver() && !dsIn.stopHarvestButtonCoDriver() &&
                !dsIn.openGripperButtonCoDriver() && !dsIn.armToLow2PosCoDriver())
            {
                currState = armDown;
                this.done = false;
                System.out.println("sendArmDownButton pressed");
            }
            else if(!dsIn.sendArmDownButtonCoDriver() && !dsIn.harvestButtonCoDriver() &&
                !dsIn.scoringButtonCoDriver() && !dsIn.stopHarvestButtonCoDriver() &&
                dsIn.openGripperButtonCoDriver() && !dsIn.armToLow2PosCoDriver())
            {
                    currState = gripperControl;
                    this.done = false;    
            }
//            else if(!dsIn.sendArmDownButtonCoDriver() && !dsIn.harvestButtonCoDriver() &&
//                !dsIn.scoringButtonCoDriver() && !dsIn.stopHarvestButtonCoDriver() &&
//                !dsIn.openGripperButtonCoDriver() && dsIn.armToLow2PosCoDriver())
//            {
//                currState = armToFeedPos;
//                this.done = false;
//                System.out.println("armToLow2Pos");
//            }          
            //If anything else happens.
            else
            {
                currState = Idle;
                this.done = true;
            }    
        }
        
        //If our state changes, reset the timer.
        if(currState != lastState)
        {
            System.out.println("currState = " + currState);
            System.out.println("lastState = " + lastState);
            myTimer.reset();
        }
        rOut.determineHarvesterState();
        
        switch(currState)
        {
            case Idle:
                System.out.println("State: Idle");
                this.done = true;
                this.stopArm();
                break;
            case harvest:
                System.out.println("State: harvest");
                rOut.openGripper();
                if(this.moveArmToGround())
                {
                    rOut.setActiveFloorDn();
                    if((myTimer.get() >= DEPLOY_HARVESTER_DELAY) && (rIn.getArmBotLimit() == true))
                    {
                        rOut.deployHarvester();
                        rOut.setRollerMotorOn();
                        this.stopArm();
                        this.done = true;
                    }                    
                }    
                break;
            case stopHarvester:
                System.out.println("State: stopHarvester");
                rOut.setRollerMotorReverse();                  
//                if(myTimer.get() >= 1)
//                {
                    rOut.retractHarvester();                  
                    if(myTimer.get() >= RAISE_FLOOR_DELAY)
                    {    
                        rOut.setActiveFloorUp();
                    }
                    if(myTimer.get() >= (RAISE_FLOOR_DELAY + CLOSE_GRIP_DELAY))
                    {   
                        rOut.closeGripper();
                        rOut.setRollerMotorOff();
                        this.done = true;
                    }    
//                } 
                break;
            case goToMid:
                System.out.println("State: goToMid");
                if(rOut.harvesterState == 0) //Harvester is out
                {
                    this.isHarvInBeforeGoToMid = true;
                    rOut.setActiveFloorUp();
                    if(myTimer.get() >= CLOSE_GRIP_DELAY)
                    {    
                        rOut.closeGripper();
                        rOut.setRollerMotorOff();                        
                        rOut.retractHarvester();
                    }
                }
                else if((rOut.harvesterState == 1) && !this.isHarvInBeforeGoToMid) //Harvester is in
                {
                    rOut.setActiveFloorDn();
                    if((rIn.getArmPositionPotValue() > (MIDDLE_POS + 0.1)))
                    {   
                        rOut.closeGripper();
                        this.moveArmUpPastMid();
                    }
                }
                else if((rOut.harvesterState == 1) && this.isHarvInBeforeGoToMid)
                {
                    if(myTimer.get() >= (RETRACT_HARVESTER_DELAY + CLOSE_GRIP_DELAY))
                    {
                        this.isHarvInBeforeGoToMid = false;
                    }    
                }
                if((rIn.getArmPositionPotValue() <= (MIDDLE_POS + 0.1)) && !this.isHarvInBeforeGoToMid)
                {
                    this.stopArm();                        
                    if(!dsIn.scoringButtonCoDriver())
                    {
                        this.done = true;
                    }
                    else if(dsIn.scoringButtonCoDriver())
                    {
                        currState = case9;
                    }
                }
                break;
            case armDown:
                System.out.println("State: Arm Down");
                rOut.setActiveFloorDn();
                if(rIn.getArmBotLimit() == false)
                {
                    this.moveArmToGround();
                    myTimer.reset();
                }
                else if ((rIn.getArmBotLimit() == true) || (myTimer.get() > STOP_ARM_DELAY))
                {
                    this.stopArm();
                    currState = closeGripper;    
                }    
                break;
            case closeGripper:
                System.out.println("State: Close Gripper");
                rOut.setActiveFloorUp();
                if(myTimer.get() >= CLOSE_GRIP_DELAY)
                {
                    rOut.closeGripper();
                    this.done = true;
                }
                break;
            case case9:
                System.out.println("State: case9");
                this.moveArmToTop();
                if((rIn.getArmTopLimit() == true))
                {
                    rOut.openGripper();
                    this.done = true;
                }
                break;
            case gripperControl:
                System.out.println("State: gripperControl");
                if((rOut.gripperControl.get() == false) && (this.lastState != this.currState)) //open
                {
                    rOut.setActiveFloorUp();
                    if(myTimer.get() >= RAISE_FLOOR_DELAY)
                    {    
                    rOut.closeGripper();
                    }
                }
                else if((rOut.gripperControl.get() == true) && (this.lastState != this.currState)) //closed
                {
                    rOut.setActiveFloorDn();
                    rOut.openGripper();
                }
                this.done = true;
                break;
//            case armToFeedPos:
//                System.out.println("State: armToFeedPos");
//                this.moveArmToFeedPosition();
//                if(rIn.getArmPositionPotValue() < FEED_POS - 0.1)
//                {
//                    this.done = true;
//                }    
//                break;
        }
        lastState = currState;
        //System.out.println("lastState = currState");
    }        
/******************************************************************************/
  public void manualOverride()
  {
      if(dsIn.getArmDnButton())
          {
              this.moveArmDownSlow();
              //this.manualDown();
          }
          else if(dsIn.getArmUpButton())
          {
              this.moveArmUp();
              //this.manualUp();
          }
          else
          {
              this.stopArm();
          }
      System.out.println("Bottom Limit: " + rIn.getArmBotLimit());
      if(dsIn.getGripperCloseButton() || dsIn.getArmDnButton() || dsIn.getArmUpButton()
              || dsIn.getGripperOpenButton() || dsIn.getHarvestInButton() ||
              dsIn.getHarvestOutButton() || dsIn.getRollerOffButton() ||
              dsIn.getRollerOnButton())
      {
          
          if(dsIn.getGripperOpenButton())
          {
              manualState = openGrip;
          }
          else if(dsIn.getGripperCloseButton())
          {
              manualState = closeGrip;
          }    
          
          if(dsIn.getHarvestOutButton())
          {
              manualState = harvestOut;        
          }
          else if(dsIn.getHarvestInButton())
          {
              manualState = harvestIn;
          }
          if(dsIn.getRollerOnButton())
          {
              manualState = rollerOn;
          }
          else if(dsIn.getRollerOffButton())
          {
              manualState = rollerOff;
          }
          switch(manualState)
          {
              case harvestOut:
                  System.out.println("harvest out");
                  rOut.deployHarvester();
                  break;
              case harvestIn:
                  System.out.println("harvest in");
                  rOut.retractHarvester();
                  break;
              case rollerOn:
                  System.out.println("roller on");
                  rOut.setRollerMotorOn();                  
                  break;
              case rollerOff:
                  System.out.println("roller off");
                  rOut.setRollerMotorOff();                                                     
                  break;
              case openGrip:
                  System.out.println("open gripper");
                  rOut.setActiveFloorDn();
                  rOut.openGripper();
                  break;
              case closeGrip:
                  System.out.println("close gripper");
                  rOut.setActiveFloorUp();
                  rOut.closeGripper();
                  break;
          }    
      }
      else if(!dsIn.getGripperCloseButton())
      {
          this.gamepadControl();
      }    
  }
/******************************************************************************/  
//DO NOT MESS WITH GREATER THAN OR LESS THAN SIGNS
  
    /**
   * Moves arm up.Slows down the speed at a certain point.
   * @param none
   * @return void
   */
  public void moveArmUp()
  {
      rOut.setArmMotorValue(SPEED_UP); //SPEED_UP
      if(rIn.getArmPositionPotValue() <= SLOW_BEFORE_TOP_LIMIT)
      {
          rOut.setArmMotorValue(SPEED_UP_SL); //SPEED_UP_SL
          System.out.println("Arm Up Slow: " + SPEED_UP_SL); //SPEED_UP_SL
      }
      else
      {
          System.out.println("Arm Up Fast: " + SPEED_UP); //SPEED_UP
      }    
  }
  
  /**
   * Move arm up until the top limit is hit.
   * @param none
   * @return void
   */
  public void moveArmToTop()
  {
      this.moveArmUp();
      if(((rIn.getArmPositionPotValue() >= (TOP_POS - 0.1)) && (rIn.getArmPositionPotValue() <= (TOP_POS - 0.1))) ||
        (rIn.getArmTopLimit() == true))
      {
          this.stopArm();
      }    
  }         
  
  /**
   * Moves arm down until bottom limit is hit.
   * @param none
   * @return void
   */
  public boolean moveArmToGround()
  {
      this.moveArmDown();
      return rIn.getArmBotLimit();
  }
  
  /**
   * Moves arm down.Slows down the speed at a certain point.
   * @param none
   * @return void
   */
  public void moveArmDown()
  {
      if(rIn.getArmPositionPotValue() >= 4.00)
      {
          rOut.setArmMotorValue(-0.25);
      }      
      else if(rIn.getArmPositionPotValue() >= LOW_POS)
      {
          rOut.setArmMotorValue(SPEED_DN_SL); //SPEED_DN_SL
      }
      else if(rIn.getArmBotLimit() != true)
      {
          rOut.setArmMotorValue(SPEED_DN_PRAC); //SPEED_DN
      }
      else if(rIn.getArmBotLimit() == true)
      {
          rOut.setArmMotorValue(SPEED_ST);
      }    
  }
  
    public void moveArmDownSlow()
  {    
      if(rIn.getArmPositionPotValue() >= LOW_POS)
      {
          rOut.setArmMotorValue(SPEED_DN_SL);
      }
      else if(rIn.getArmBotLimit() != true)
      {
          rOut.setArmMotorValue(SPEED_DN_MAN);
      }
      else if(rIn.getArmBotLimit() == true)
      {
          rOut.setArmMotorValue(SPEED_ST);
      }    
  }
    
//    public void moveArmToFeedPosition()
//    {
//        if(rIn.getArmPositionPotValue() > FEED_POS + 0.1)
//        {
//            rOut.setArmMotorValue(SPEED_UP);
//        }
//        else if(rIn.getArmPositionPotValue() > FEED_POS)
//        {
//            rOut.setArmMotorValue(SPEED_UP_SL);
//            rOut.openGripper();
//        }        
//        else if(rIn.getArmPositionPotValue() < FEED_POS - 0.1)
//        {
//            rOut.setArmMotorValue(SPEED_ST);
//        }    
//    }        
  
  /**
   * Moves arm to the middle position.
   * @param none
   * @return void
   */
  public boolean moveArmUpPastMid()
  {    
      if(rIn.getArmPositionPotValue() > (MIDDLE_POS - 0.1))
      {
          this.moveArmUp();
          return false;
      }
      else if((rIn.getArmPositionPotValue() <= (MIDDLE_POS - 0.1)))
      {
          this.stopArm();
          return true;
      }
      return false;
  }        
  
  /**
   * Turns off the arm motor.
   * @param none
   * @return void
   */
  public void stopArm()
  {
      rOut.setArmMotorValue(SPEED_ST);
      System.out.println("Arm stopped");
  }
  /**********manualOverrideControlForPracticeBot*********/
  
  public void manualDown()
  {
      if(rIn.getArmBotLimit() == true)
      {
          this.stopArm();
      }
      else
      {
          rOut.setArmMotorValue(SPEED_DN);
      }    
  }
  public void manualUp()
  {
      if(rIn.getArmTopLimit() == true)
      {
          this.stopArm();
      }
      else
      {
          rOut.setArmMotorValue(SPEED_UP);
      }    
  }        
}

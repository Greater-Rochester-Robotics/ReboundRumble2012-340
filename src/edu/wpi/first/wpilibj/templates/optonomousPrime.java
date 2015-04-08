/**
 * @author Tyler Pawlaczyk
 * @phone (585) 802-5039
 * @email tylerpawlaczyk@ymail.com (not checked often, text/call me if you want to be replied to)
 * 
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Timer;

/**
 * Class optonomousPrime
 */
public class optonomousPrime {

  private RobotOut rOut;
  private RobotIn rIn;
  private DSIn dsIn;
  private BallHandler handler;
  private Drive myDrive;
  private Timer MyTimer;
  
  private final int maxNumValues = 6;
  private int nextSampleLocation = 0;
  private int numSamples = 0;
  private double[] ultraAverage = new double[maxNumValues];
  private int itr;
  private double calcDist = 0;
  
  public byte stage = 1;
  
  //Stolen from last year's code.
  public static int autoStateA;
  public static int autoStateB;
  public static int autoStateC;
  public static int autoStateD;
  private static boolean start;
  private static boolean done;
  private static double timeValue, distValue;  
  
  //AutonomousA States
  private static final int StartA                       = 0;
  private static final int GoForwardTimedA              = 1;
  private static final int GoForwardDistFastA           = 2;
  private static final int GoForwardDistSlowA           = 3;
  private static final int ArmToTopA                    = 4;
  private static final int ReleaseBallsA                = 5;
  private static final int ArmToBottomA                 = 6;
  private static final int ArmToMidA                    = 7;
  
  //AutonomousB States
  private static final int StartB                       = 10;
  private static final int GoForwardTimedB              = 11;
  private static final int GoForwardDistFastB           = 12;
  private static final int GoForwardDistSlowB           = 13;
  private static final int ArmToTopB                    = 14;
  private static final int ReleaseBallsB                = 15;
  private static final int ArmToBottomB                 = 16;
  private static final int ArmToMidB                    = 17;  
  private static final int Turn45TowardAllianceBridgeB  = 18;
  private static final int GoForwardTowardBridgeB       = 19;
  
  //AutonomousC States
  private static final int StartC                       = 30;
  private static final int GoForwardTimedC              = 31;
  private static final int GoForwardDistFastC           = 32;
  private static final int GoForwardDistSlowC           = 33;
  private static final int ArmToTopC                    = 34;
  private static final int ReleaseBallsC                = 35;
  private static final int ArmToBottomC                 = 36;
  private static final int ArmToMidC                    = 37;
  private static final int Turn45TowardAllianceBridgeC  = 38;
  private static final int GoForwardTowardBridgeC       = 39;
  private static final int TurnTowardCoOpBridgeC        = 40;
  private static final int GoForwardTowardCoOpBridgeC   = 41;
  
  //AutonomousD States
  private static final int StartD                       = 50;
  
  private static final int Stop                         = 99;

  
  
  
  //Finals for AutonomousA
  private final double FORWARD_VALUE_A_1    = 19.0;
  private final double FORWARD_VALUE_A_2    = 1.0;
  private final double BACKWARD_VALUE_A     = 1.0;
  //Finals for AutonomousB
  private final double BACKWARD_VALUE_B     = 1.0;
  private final double FORWARD_VALUE_B      = 1.0;
  
/**********Begin stuff stolen from last year's auto code***********************/  
  private final int LEFT_MULT = 1;
  private final int RIGHT_MULT = -1;
  
  public static final int arraySize = 5;   // Change this as needed to get a good average value
  public static       int     myPtr = 0;   // Pointer to where to write the value in the array
  public static int           myItr;       // Iterator to retrieve the individual values in the array
  public static double        mySum;       // Temporary storage of the sum
  public static boolean       myAvg1st = true;  // Indicates the first runthrough of the average routene
  public static double[]      avgArray = new double[arraySize]; // default values to 0
  
  private static double ultraSonicValue;
/******************************************************************************/  
  
  /**
   * Passes in aliases for other classes.
   * @param rout
   * @param rin
   * @param din
   * @param mydrive
   * @param handler
   * @return void
   */
  public optonomousPrime(RobotOut rout, RobotIn rin, DSIn din, Drive mydrive, BallHandler handler)
  {
      this.rIn = rin;
      this.rOut = rout;
      this.dsIn = din;
      this.myDrive = mydrive;
      this.handler = handler;
      this.MyTimer = new Timer();
     
      autoStateA     = StartA;
      autoStateB     = StartB;
      autoStateC     = StartC;
      autoStateD     = StartD;
     
      this.start = false;
      this.done = false;
      
  }
  /**
   * Decides which autonomous mode will run.
   * @param stage
   * @return int
   */
  public byte runAutonomous(byte stage)
  {     
     if(dsIn.getAutonomousState() == 1)
     {
         this.stage = 1;
         this.AutonomousA();
     }
     else if(dsIn.getAutonomousState() == 3)
     {
         this.stage = 3;
         this.AutonomousB();
     }
     else if(dsIn.getAutonomousState() == 2)
     {
         this.stage = 2;
         this.AutonomousC();
     }
     else if(dsIn.getAutonomousState() == 4)
     {    
         this.stage = 4;
         this.AutonomousD();
     }    
     else
     {
         this.stage = 1; //1
         this.AutonomousA(); //A
     }    
     return this.stage;
  }
  /**
   * Normal 2-Ball-Scoring stuff, deploys harvester at end.
   * @param none
   * @return void
   */
  public void AutonomousA()
  {
      try
      {
//        rOut.leftJaguarBack.configNeutralMode(CANJaguar.NeutralMode.kBrake);
//        rOut.rightJaguarBack.configNeutralMode(CANJaguar.NeutralMode.kBrake);
//        rOut.rightJaguarFront.configNeutralMode(CANJaguar.NeutralMode.kBrake);
//        rOut.leftJaguarFront.configNeutralMode(CANJaguar.NeutralMode.kBrake); 
      }
      catch (Exception e)
      {
          e.printStackTrace();          
      }
      System.out.println("in auto A");
      System.out.println(autoStateA);
      if(this.stage == 1)
      {
          System.out.println("AutoA");
          this.getUltrasonicVals();
          switch(autoStateA)
          {
              case StartA:
                System.out.println("AutonomousA Start State");
                rOut.setActiveFloorUp();
                rOut.closeGripper();
                this.MyTimer.start();
                autoStateA = GoForwardTimedA;
                break;
                  
              case GoForwardTimedA:
                System.out.println("Going Forward");
                rOut.setActiveFloorDn();

                if (myDrive.TimedGo(0.75, 0.0, 0.5, MyTimer)) { //null fixed, doesn't need MyTimer.                      
                    rOut.setLeftDrive(0.0);
                    rOut.setRightDrive(0.0);
                    //MyTimer.stop();
                    //MyTimer.reset();
                    autoStateA = GoForwardDistFastA;
                }
                
                break;
              case GoForwardDistFastA:
                System.out.println("Going Forward Dist");
                rOut.setActiveFloorDn();

                if (this.timeOrDistLessThanGo(0.50, 0.0, 7.0, 25)) {                    
                    rOut.setLeftDrive(0.0);
                    rOut.setRightDrive(0.0);
                    MyTimer.stop();
                    MyTimer.reset();
                    autoStateA = GoForwardDistSlowA;
                }
                
                break;
              case GoForwardDistSlowA:
                System.out.println("Going Forward Dist");
                rOut.setActiveFloorDn();

                if (this.timeOrDistLessThanGo(0.25, 0.0, 1.0, 15)) {                      
                    rOut.setLeftDrive(0.125);
                    rOut.setRightDrive(-0.125);
                    try{
//                        rOut.leftJaguarBack.configNeutralMode(CANJaguar.NeutralMode.kBrake);
//                        rOut.rightJaguarBack.configNeutralMode(CANJaguar.NeutralMode.kBrake);
//                        rOut.rightJaguarFront.configNeutralMode(CANJaguar.NeutralMode.kBrake);
//                        rOut.leftJaguarFront.configNeutralMode(CANJaguar.NeutralMode.kBrake); 
                    }catch (Exception e){          
                    }
                    MyTimer.stop();
                    MyTimer.reset();
                    autoStateA = ArmToMidA;
                }
                break;  
              case ArmToMidA:
                System.out.println("Arm Going Up");
//                rOut.setLeftDrive(0.125);
//                rOut.setRightDrive(-0.125);                
                if(handler.moveArmUpPastMid() == true)
                {    
                    autoStateA = ArmToTopA;
                }    
                break;
                  
              case ArmToTopA:
                System.out.println("Arm Going Up");
                if(rIn.getArmTopLimit() == false) {
                    handler.moveArmToTop();
                } else if(rIn.getArmTopLimit() == true){
                    handler.stopArm();
                    MyTimer.start();                    
                    autoStateA = ReleaseBallsA;
                }
                break;   
                  
              case ReleaseBallsA:
                System.out.println("Releasing Balls");
                //if(myDrive.TimedGo(0.0, 0.0, 0.5, MyTimer))
                //{    
                rOut.openGripper();
                //}
                if(myDrive.TimedGo(0.0, 0.0, 2.0, MyTimer))
                {    
                    //MyTimer.stop();
                    //MyTimer.reset();
                    autoStateA = ArmToBottomA;
                }
                break;
                  
              case ArmToBottomA:
                System.out.println("Arm Going Down");
                    handler.moveArmToGround();
                if(rIn.getArmBotLimit() == true){
                    handler.stopArm();
                    rOut.deployHarvester();
                    //MyTimer.start();
                    autoStateA = Stop;
                }
                break;
                  
              case Stop:
                //System.out.println("STOP!");
                handler.stopArm();
                rOut.setLeftDrive(0.0);
                rOut.setRightDrive(0.0);
                autoStateA = Stop;
                break;
        }
    }
    else
      {
          //Do Nothing
      }      
}
  
    /**
     * Normal 2-Ball-Scoring stuff, go to the alliance bridge.
     */  
    public void AutonomousB()
    {
        try
        {
//            rOut.leftJaguarBack.configNeutralMode(CANJaguar.NeutralMode.kBrake);
//            rOut.rightJaguarBack.configNeutralMode(CANJaguar.NeutralMode.kBrake);
//            rOut.rightJaguarFront.configNeutralMode(CANJaguar.NeutralMode.kBrake);
//            rOut.leftJaguarFront.configNeutralMode(CANJaguar.NeutralMode.kBrake); 
        }
        catch (Exception e)
        {
            e.printStackTrace();          
        }
        System.out.println("in auto B");
        System.out.println(autoStateB);
        if(this.stage == 3)
        {
            System.out.println("AutoB");
            this.getUltrasonicVals();
            switch(autoStateB)
            {
              case StartB:
                System.out.println("AutonomousB Start State");
                rOut.setActiveFloorUp();
                rOut.closeGripper();
                this.MyTimer.start();
                autoStateB = GoForwardTimedB;
                break;
                  
              case GoForwardTimedB:
                System.out.println("Going Forward");
                rOut.setActiveFloorDn();

                if (myDrive.TimedGo(0.75, 0.0, 0.5, MyTimer)) { //null fixed, doesn't need MyTimer.                      
                    rOut.setLeftDrive(0.0);
                    rOut.setRightDrive(0.0);
                    //MyTimer.stop();
                    //MyTimer.reset();
                    autoStateB = GoForwardDistFastB;
                }
                
                break;
              case GoForwardDistFastB:
                System.out.println("Going Forward Dist");
                rOut.setActiveFloorDn();

                if (this.timeOrDistLessThanGo(0.50, 0.0, 7.0, 25)) {                    
                    rOut.setLeftDrive(0.0);
                    rOut.setRightDrive(0.0);
                    MyTimer.stop();
                    MyTimer.reset();
                    autoStateB = GoForwardDistSlowB;
                }
                
                break;
              case GoForwardDistSlowB:
                System.out.println("Going Forward Dist");
                rOut.setActiveFloorDn();

                if (this.timeOrDistLessThanGo(0.25, 0.0, 1.0, 15)) {                      
                    rOut.setLeftDrive(0.125);
                    rOut.setRightDrive(-0.125);
                    try{
//                        rOut.leftJaguarBack.configNeutralMode(CANJaguar.NeutralMode.kBrake);
//                        rOut.rightJaguarBack.configNeutralMode(CANJaguar.NeutralMode.kBrake);
//                        rOut.rightJaguarFront.configNeutralMode(CANJaguar.NeutralMode.kBrake);
//                        rOut.leftJaguarFront.configNeutralMode(CANJaguar.NeutralMode.kBrake); 
                    }catch (Exception e){          
                    }
                    MyTimer.stop();
                    MyTimer.reset();
                    autoStateB = ArmToMidB;
                }
                break;  
              case ArmToMidB:
                System.out.println("Arm Going Up");
//                rOut.setLeftDrive(0.125);
//                rOut.setRightDrive(-0.125);                
                if(handler.moveArmUpPastMid() == true)
                {    
                    autoStateB = ArmToTopB;
                }    
                break;
                  
              case ArmToTopB:
                System.out.println("Arm Going Up");
                if(rIn.getArmTopLimit() == false) {
                    handler.moveArmToTop();
                } else if(rIn.getArmTopLimit() == true){
                    handler.stopArm();
                    MyTimer.start();                    
                    autoStateB = ReleaseBallsB;
                }
                break;   
                  
              case ReleaseBallsB:
                System.out.println("Releasing Balls");
                //if(myDrive.TimedGo(0.0, 0.0, 0.5, MyTimer))
                //{    
                rOut.openGripper();
                //}
                if(myDrive.TimedGo(0.0, 0.0, 2.0, MyTimer))
                {    
                    MyTimer.stop();
                    //MyTimer.reset();
                    autoStateB = ArmToBottomB;
                }
                break;
                  
              case ArmToBottomB:
                System.out.println("Arm Going Down");
                    handler.moveArmToGround();
                if(rIn.getArmBotLimit() == true){
                    handler.stopArm();
                    MyTimer.reset();
                    MyTimer.start();
                    autoStateB = Turn45TowardAllianceBridgeB;
                }
                break;
                    
              case Turn45TowardAllianceBridgeB:
                System.out.println("Turning");
                rOut.setRightDrive(1.0);
                rOut.setLeftDrive(0.0);
                if(MyTimer.get() >= 0.5)
                {
                    rOut.setRightDrive(0.0);
                    rOut.setLeftDrive(0.0); 
                    MyTimer.stop();
                    MyTimer.reset();
                    MyTimer.start();
                    autoStateB = GoForwardTowardBridgeB;
                }       
                break;
                    
              case GoForwardTowardBridgeB:
                System.out.println("Going Forward Timed");
                if(myDrive.TimedGo(-0.75, 0.0, 2.0, MyTimer)) 
                {
                    rOut.setLeftDrive(0.0);
                    rOut.setRightDrive(0.0);  
                    autoStateB = Stop;  
                }    
                break;

              case Stop:
                //System.out.println("STOP!");
                handler.stopArm();
                rOut.setLeftDrive(0.0);
                rOut.setRightDrive(0.0);
                autoStateB = Stop;
                break;
            }
        }
        else
        {
            //Do Nothing
        }      
    }
  
  /**
   * Normal 2-Ball-Scoring stuff, doesn't deploy harvester at the end.
   */
  public void AutonomousC()
  {
        try
        {
//            rOut.leftJaguarBack.configNeutralMode(CANJaguar.NeutralMode.kBrake);
//            rOut.rightJaguarBack.configNeutralMode(CANJaguar.NeutralMode.kBrake);
//            rOut.rightJaguarFront.configNeutralMode(CANJaguar.NeutralMode.kBrake);
//            rOut.leftJaguarFront.configNeutralMode(CANJaguar.NeutralMode.kBrake); 
        }
        catch (Exception e)
        {
            e.printStackTrace();          
        }
        System.out.println("in auto C");
        System.out.println(autoStateC);
        if(this.stage == 2)
        {
            System.out.println("AutoC");
            this.getUltrasonicVals();
            switch(autoStateC)
            {
              case StartC:
                System.out.println("AutonomousC Start State");
                rOut.setActiveFloorUp();
                rOut.closeGripper();
                this.MyTimer.start();
                autoStateC = GoForwardTimedC;
                break;
                  
              case GoForwardTimedC:
                System.out.println("Going Forward");
                rOut.setActiveFloorDn();

                if (myDrive.TimedGo(0.75, 0.0, 0.5, MyTimer)) { //null fixed, doesn't need MyTimer.                      
                    rOut.setLeftDrive(0.0);
                    rOut.setRightDrive(0.0);
                    //MyTimer.stop();
                    //MyTimer.reset();
                    autoStateC = GoForwardDistFastC;
                }
                
                break;
              case GoForwardDistFastC:
                System.out.println("Going Forward Dist");
                rOut.setActiveFloorDn();

                if (this.timeOrDistLessThanGo(0.50, 0.0, 7.0, 25)) {                    
                    rOut.setLeftDrive(0.0);
                    rOut.setRightDrive(0.0);
                    MyTimer.stop();
                    MyTimer.reset();
                    autoStateC = GoForwardDistSlowC;
                }
                
                break;
              case GoForwardDistSlowC:
                System.out.println("Going Forward Dist");
                rOut.setActiveFloorDn();

                if (this.timeOrDistLessThanGo(0.25, 0.0, 1.0, 15)) {                      
                    rOut.setLeftDrive(0.125);
                    rOut.setRightDrive(-0.125);
                    try{
//                        rOut.leftJaguarBack.configNeutralMode(CANJaguar.NeutralMode.kBrake);
//                        rOut.rightJaguarBack.configNeutralMode(CANJaguar.NeutralMode.kBrake);
//                        rOut.rightJaguarFront.configNeutralMode(CANJaguar.NeutralMode.kBrake);
//                        rOut.leftJaguarFront.configNeutralMode(CANJaguar.NeutralMode.kBrake); 
                    }catch (Exception e){          
                    }
                    MyTimer.stop();
                    MyTimer.reset();
                    autoStateC = ArmToMidC;
                }
                break;  
              case ArmToMidC:
                System.out.println("Arm Going Up");
//                rOut.setLeftDrive(0.125);
//                rOut.setRightDrive(-0.125);                
                if(handler.moveArmUpPastMid() == true)
                {    
                    autoStateC = ArmToTopC;
                }    
                break;
                  
              case ArmToTopC:
                System.out.println("Arm Going Up");
                if(rIn.getArmTopLimit() == false) {
                    handler.moveArmToTop();
                } else if(rIn.getArmTopLimit() == true){
                    handler.stopArm();
                    MyTimer.start();                    
                    autoStateC = ReleaseBallsC;
                }
                break;   
                  
              case ReleaseBallsC:
                System.out.println("Releasing Balls");
                //if(myDrive.TimedGo(0.0, 0.0, 0.5, MyTimer))
                //{    
                rOut.openGripper();
                //}
                if(myDrive.TimedGo(0.0, 0.0, 2.0, MyTimer))
                {    
                    MyTimer.stop();
                    //MyTimer.reset();
                    autoStateC = ArmToBottomC;
                }
                break;
                  
              case ArmToBottomC:
                System.out.println("Arm Going Down");
                    handler.moveArmToGround();
                if(rIn.getArmBotLimit() == true){
                    handler.stopArm();
                    MyTimer.reset();
                    MyTimer.start();
                    autoStateC = Turn45TowardAllianceBridgeC;
                }
                break;
                    
              case Turn45TowardAllianceBridgeC:
                System.out.println("Turning");
                rOut.setRightDrive(1.0);
                rOut.setLeftDrive(0.0);
                if(MyTimer.get() >= 0.5)
                {
                    rOut.setRightDrive(0.0);
                    rOut.setLeftDrive(0.0); 
                    MyTimer.stop();
                    MyTimer.reset();
                    MyTimer.start();
                    autoStateC = GoForwardTowardBridgeC;
                }       
                break;
                    
              case GoForwardTowardBridgeC:
                System.out.println("Going Forward Timed");
                if(myDrive.TimedGo(-0.75, 0.0, 1.0, MyTimer)) 
                {
                    rOut.setLeftDrive(0.0);
                    rOut.setRightDrive(0.0);
                    MyTimer.stop();
                    MyTimer.reset();
                    MyTimer.start();
                    autoStateC = TurnTowardCoOpBridgeC;  
                }    
                break;
                  
              case TurnTowardCoOpBridgeC:
                System.out.println("Turning");
                rOut.setRightDrive(0.0);
                rOut.setLeftDrive(-1.0);
                if(MyTimer.get() >= 0.5)
                {
                    rOut.setRightDrive(0.0);
                    rOut.setLeftDrive(0.0); 
                    MyTimer.stop();
                    MyTimer.reset();
                    MyTimer.start();
                    autoStateC = GoForwardTowardCoOpBridgeC;
                }                  
                break;
                  
              case GoForwardTowardCoOpBridgeC:
                System.out.println("Going Forward Timed");
                if(myDrive.TimedGo(-0.75, 0.0, 1.0, MyTimer)) 
                {
                    rOut.setLeftDrive(0.0);
                    rOut.setRightDrive(0.0);
                    MyTimer.stop();
                    autoStateC = Stop;  
                }                  
                break;  
                  
              case Stop:
                //System.out.println("STOP!");
                handler.stopArm();
                rOut.setLeftDrive(0.0);
                rOut.setRightDrive(0.0);
                autoStateB = Stop;
                break;
            }
        }
        else
        {
            //Do Nothing
        }    
  }
  /**
   * Normal 2-Ball-Scoring, back up to coopertition bridge.
   */
  public void AutonomousD()
  {
      try
      {
//        rOut.leftJaguarBack.configNeutralMode(CANJaguar.NeutralMode.kBrake);
//        rOut.rightJaguarBack.configNeutralMode(CANJaguar.NeutralMode.kBrake);
//        rOut.rightJaguarFront.configNeutralMode(CANJaguar.NeutralMode.kBrake);
//        rOut.leftJaguarFront.configNeutralMode(CANJaguar.NeutralMode.kBrake); 
      }
      catch (Exception e)
      {
          e.printStackTrace();          
      }
      System.out.println("in auto D");
      System.out.println(autoStateD);
      if(this.stage == 4)
      {
          System.out.println("AutoD");
          this.getUltrasonicVals();
          switch(autoStateD)
          {
              case StartD:
                  System.out.println("AutonomousD Start State");                  
                  rOut.setLeftDrive(0.0);
                  rOut.setRightDrive(0.0);
                  handler.stopArm();
                  autoStateD = Stop;
                  break;
                  
              case Stop:
                  rOut.setLeftDrive(0.0);
                  rOut.setRightDrive(0.0);
                  handler.stopArm();
                  autoStateD = Stop;
                  break;
          }
       }   
  }        

    /**
     * ***************************************************************************
     */
    public void go(double speed, double dir) {
        rOut.setLeftDrive((speed * LEFT_MULT) + dir);
        rOut.setRightDrive((speed * RIGHT_MULT) - dir);
    }

    public boolean timeOrDistGo(double speed, double dir, double goTime, double lDist)
    {
        System.out.println("calcDist: " + calcDist);
        if(myDrive.TimedGo(speed, dir, goTime, MyTimer) || (lDist>=(calcDist +2)&& lDist>= (calcDist -2))){
            myDrive.arcadeDrive(0.0, 0.0, false);
            return true;
        }
        else
        {
            return false;
        }    
    }
    
    public boolean timeOrDistGreaterThanGo(double speed, double dir, double goTime, double lDist)
    {
        System.out.println("greater than: " + calcDist +" target:" + lDist);
        if(myDrive.TimedGo(speed, dir, goTime, MyTimer) || calcDist >=lDist){
            myDrive.arcadeDrive(0.0, 0.0, false);
            return true;
        }
        else
        {
            return false;
        }    
    }
    public boolean timeOrDistLessThanGo(double speed, double dir, double goTime, double lDist)
    {
        System.out.println("less than: " + calcDist +" target:" + lDist);        
        
        if(myDrive.TimedGo(speed, dir, goTime, MyTimer) || calcDist <= lDist){
            myDrive.arcadeDrive(0.0, 0.0, false);
            return true;
        }
        else
        {
            return false;
        }    
    }    
    
    
    public double getUltrasonicVals()
    {
        ultraAverage[nextSampleLocation] = rIn.getLeftUltrasonicValue() /2;
        //System.out.println("ultra value:: " + ultraAverage[nextSampleLocation]/2);
        if(nextSampleLocation < (maxNumValues-1))
        {
            nextSampleLocation++;
        }
        else
        {
            nextSampleLocation = 0;
        }
        if(numSamples < maxNumValues)
        {
            numSamples++;
        }
        calcDist = 0;
        //System.out.println("\t1 CalcDist: " + calcDist);

        for(itr = 0; itr < numSamples; itr++)
        {
            calcDist = calcDist + ultraAverage[itr];
        }
        //System.out.println("\t2 CalcDist: " + calcDist + "  samples: " + numSamples);

        calcDist = calcDist / numSamples;
        //System.out.println("\t3 CalcDist: " + calcDist);
        return calcDist;
    }        


}


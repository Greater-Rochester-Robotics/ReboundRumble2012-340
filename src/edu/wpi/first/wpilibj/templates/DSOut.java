package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Dashboard;

        



/**
 * Class DSOut
 */
public class DSOut {

      private RobotOut rOut;
    /**
     * 
     */
    private RobotIn rIn;
    private DSIn dsIn;
    private optonomousPrime auto;
    private AutoBalance balance;
    //private BallProcessor bProcessor;
    private int updateDashCount = 0;
    private boolean prevShiftState;  

  public DSOut (RobotIn myRIn, RobotOut myROut, DSIn mydsIn, optonomousPrime myAuto, AutoBalance balance) { 
      this.rIn = myRIn; 
      this.rOut = myROut;
      this.dsIn= mydsIn;
      this.auto = myAuto;
      this.balance = balance;
  };
  

  private float getCurrent(){
      return 0;
  }
  private byte numCANError(){
      return 0;
  }
  void updateDash () { 
    if (updateDashCount == 0) {
        Dashboard lowDashData = DriverStation.getInstance().getDashboardPackerLow();
        lowDashData.addCluster();
        {
            //lowDashData.addInt(bProcessor.numballsIn);
            //lowDashData.addInt(bProcessor.gripperNumballs);
            //lowDashData.addFloat((float)rIn.getArmPositionPotValue());
            //lowDashData.addFloat(this.getCurrent());
            lowDashData.addByte(this.numCANError());
            lowDashData.addBoolean(balance.areWeBalanced());
            //lowDashData.addBoolean((rOut.stingerControl.get() == true) && balance.areWeBalanced());
            lowDashData.addBoolean(rIn.getBanner() == true);
            lowDashData.addByte(auto.stage);
            
        }
        lowDashData.finalizeCluster();
        lowDashData.commit();
    
        updateDashCount++; 
    } else if (updateDashCount >= 4) {
        updateDashCount = 0;
    } else {
        updateDashCount++;        
    }
  
  }


  /**
   */
  private boolean hasShifted(){
      if(rOut.isHighGear() != prevShiftState){
          prevShiftState = rOut.isHighGear();
          return true;
      }else{
        prevShiftState = rOut.isHighGear();
        return false;
      }
  }
  
  public void startRumbleGamePad(  )
  {
  }


  /**
   */
  public void stopRumbleGamePad(  )
  {
  }


}

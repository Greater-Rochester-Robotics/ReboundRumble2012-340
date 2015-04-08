/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import com.sun.squawk.util.MathUtils;
//import java.text.DecimalFormat;


/**
 *
 * @author Rex
 */
public class AutoBalance {
    
    private RobotOut rOut;
    private RobotIn rIn;
    private DSIn dsIn;
    private Drive myDrive;
    
    private double xDeg1, yDeg1, zDeg1;
    
    private double xVal, yVal, zVal;
    public double xOffSet, yOffSet, zOffSet;
    private double myPval, myIval, myDval;
    public int smplNum, smplTot, smplTotLast;
    private int itr1, itr2, itr3;
    private boolean smplRdy;
    private double[] xVals;
    private double[] yVals;
    private double[] zVals;
    private double factor = 1e5;
    private double tltDeg, rotDeg;
    private double xDeg, yDeg, zDeg;
    private boolean modValsLast;
    private boolean prnInitVals;
    private boolean newInitVals;
    private double myValue;
    private double speedMax;
    private double angleMax;
    private int driveCnt;
    private double inCmd;
    private double kP;
    private double revAngle;
    private boolean revActive;
    private int drCntMax;
    private double range = 1.0;

//    DecimalFormat dec8 = new DecimalFormat("0.00000000");
    
    /**
     * Constructor
     */
    public AutoBalance (RobotIn rIn, int smplNum) {
      // Map argument references to local names
      this.rIn = rIn;
      this.smplNum = smplNum;
      // Force at least two samples
      if (this.smplNum <= 1) {this.smplNum = 2;}
      // Create the averaging arrays
      xVals = new double[smplNum];
      yVals = new double[smplNum];
      zVals = new double[smplNum];
      // Initialize all values
      smplRdy = false;
      itr1 = 0;
      itr2 = 0;
      itr3 = 0;
      smplTot = 0;
      for (itr1 = 0; itr1 < smplNum; itr1++) {
          xVals[itr1] = 0;
          zVals[itr1] = 0;
          yVals[itr1] = 0;
      }
      speedMax = 0.5;
      angleMax = 15.0;
      driveCnt = 0;
      inCmd = 0.0;
      kP = 1.0;
      revAngle = 0;
      revActive = false;
      drCntMax = 20;

      System.out.println("AutoBalance: Object created with " + smplNum + " samples!");
    }

    /**
     * Print out the x, y, and z values
     */
    public void prnVals() {
      if (MathUtils.round(xDeg * factor) - (MathUtils.round(xDeg * (factor - 1)) * 10) == 0) {
          xDeg1 = (MathUtils.round(xDeg * factor)+1)/factor;
      } else {
          xDeg1 = (MathUtils.round(xDeg * factor))/factor;
      }
      if (MathUtils.round(yDeg * factor) - (MathUtils.round(yDeg * (factor - 1)) * 10) == 0) {
          yDeg1 = (MathUtils.round(yDeg * factor)+1)/factor;
      } else {
          yDeg1 = (MathUtils.round(yDeg * factor))/factor;
      }
      if (MathUtils.round(zDeg * factor) - (MathUtils.round(zDeg * (factor - 1)) * 10) == 0) {
          zDeg1 = (MathUtils.round(zDeg * factor)+1)/factor;
      } else {
          zDeg1 = (MathUtils.round(zDeg * factor))/factor;
      }
      if (prnInitVals == false) {
          System.out.println("xOffSet = " + xOffSet + ", yOffSet = " + yOffSet + ", zOffSet = " + zOffSet);
          prnInitVals = true;
      }
      System.out.println("xDeg = " + xDeg1 + ", yDeg = " + yDeg1 + ", zDeg = " + zDeg1);
//      System.out.println("tltDeg = " + tltDeg + ", rotDeg = " + rotDeg);
    }
    
    /**
     * read the current value and generate a total of all readings.
     * Since we are dividing later, we don't need to divide by the number
     * of samples taken to get an average.
     * @return - True when the number of samples needed to have a valid average 
     *           have been captured.
     */
    public boolean getVals() {
//        System.out.println("itr2 = " + itr2);
        xVals[itr2] = rIn.getAccelX();
        yVals[itr2] = rIn.getAccelY();
        zVals[itr2] = rIn.getAccelZ();
        // Select which location in array to store data
        if (itr2 < smplNum - 1) {itr2++;} else {itr2 = 0;}
        // Detect when the array is full
        if (smplTot < smplNum) {smplTot++;}
        // Perform average on valid data in array
        xVal = 0;
        yVal = 0;
        zVal = 0;
        for (itr3 = 0; itr3 < smplTot; itr3++) {
            xVal = xVal + xVals[itr3];
            yVal = yVal + yVals[itr3];
            zVal = zVal + zVals[itr3];
        }
        xVal = xVal / smplTot;
        yVal = yVal / smplTot;
        zVal = zVal / smplTot;
        System.out.println("xVal" + xVal);
        System.out.println("yVal" + yVal);
        System.out.println("zVal" + zVal);
        if (smplTotLast == smplTot && smplRdy == false) {
           smplRdy = true;
           xOffSet = xVal;
           yOffSet = yVal;
           zOffSet = zVal;
           System.out.println("xOffSet = " + xOffSet + ", yOffSet = " + yOffSet + ", zOffSet = " + zOffSet);
        }
        smplTotLast = smplTot;
        if (smplRdy == true) {
            xVal = xVal + xOffSet;
            yVal = yVal + yOffSet;
            zVal = zVal + 1 - zOffSet;
        }
        // Convert to degree values
        xDeg = Math.toDegrees(MathUtils.atan(xVal / (Math.sqrt((yVal * yVal) + (zVal * zVal)))));
        yDeg = Math.toDegrees(MathUtils.atan(yVal / (Math.sqrt((xVal * xVal) + (zVal * zVal)))));
        zDeg = Math.toDegrees(MathUtils.atan(zVal / (Math.sqrt((xVal * xVal) + (yVal * yVal)))));
        tltDeg = Math.toDegrees(MathUtils.acos(zVal / (Math.sqrt((xVal * xVal) + (yVal * yVal) + (zVal * zVal)))));
        rotDeg = Math.toDegrees(MathUtils.atan(xVal / yVal));
        return smplRdy;
    }
    
    /**
     * This drives the base using the values read by the Accelerometer
     * and feeds into a PID controller.
     * Only the P is implemented now.
     */
    
    public double getTiltCmdVal() {
      if (revActive == false) {
        driveCnt = 0;
        inCmd = 0.0;
        revAngle = 2.0;
      }
      if (Math.abs(yDeg) < revAngle || revActive == true) {
        revActive = true;
        if (driveCnt < drCntMax) {
          driveCnt++;
          inCmd = -0.2;
        }
        else {
          inCmd = 0.0;
        }
      }
      myValue = ((yDeg / angleMax) - inCmd) * kP;
      if (myValue >   speedMax ) {
        myValue =   speedMax ;
      }
      if (myValue < (-speedMax)) {
        myValue = -speedMax;
      }
      return myValue;
    }
    public boolean areWeBalanced()
    {
        if((yDeg1 > -range) && (yDeg1 < range))
        {
            return true;
        }
        else
        {
            return false;
        }    
    }        
}

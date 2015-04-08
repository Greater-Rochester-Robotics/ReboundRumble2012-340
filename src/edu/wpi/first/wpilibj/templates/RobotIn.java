/**
 * @author: Ben Meyers @email: meyers.bs@gmail.com @iPod Number: 1 (585)
 * 420-6635 (Texting) @Phone: 1 (585) 576-6180 (Don't call unless you are dying,
 * or dead, because I have extremely limited minutes)
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.ADXL345_I2C;

/**
 * Class RobotIn
 */
public class RobotIn {
    
    //Digital Inputs
    private DigitalInput autoSwitch1 = new DigitalInput(1);
    private DigitalInput autoSwitch2 = new DigitalInput(2);
    private DigitalInput banner      = new DigitalInput(3);
    private DigitalInput armBotLimit = new DigitalInput(4);
    private DigitalInput armTopLimit = new DigitalInput(5);
    private ADXL345_I2C accel = new ADXL345_I2C(1, ADXL345_I2C.DataFormat_Range.k2G);
    //private DigitalInput harvesterDownSwitch = new DigitalInput(8); //Maybe  
    //Analog Inputs
    private AnalogChannel armPositionPot = new AnalogChannel(3);
    private Gyro angleometer = new Gyro(2);
    private AnalogChannel leftUltrasonic = new AnalogChannel(4); //on left side. (crio side)
    private AnalogChannel rightUltrasonic = new AnalogChannel(5); //on right side.
    //Encoders
    public Encoder leftDriveEnc = new Encoder(9, 10, true, CounterBase.EncodingType.k1X);
    public Encoder rightDriveEnc = new Encoder(12, 11, true, CounterBase.EncodingType.k1X);
    public Encoder armDriveEnc = new Encoder(13, 6, true, CounterBase.EncodingType.k1X);

    public RobotIn() {
        leftDriveEnc.setDistancePerPulse(18.06 / 250);
        rightDriveEnc.setDistancePerPulse(18.06 / 250);
    }
  
  /**********BallScorer Methods**********************************************/
  /**
   * Gets a true or false value for the bottom limit switch on the arm.
   * @return boolean
   */
  public boolean getArmBotLimit() {
        return armBotLimit.get();
    }

    /**
     * Gets a true or false value for the top limit switch on the arm.
     *
     * @return boolean
     */
    public boolean getArmTopLimit() {
        return armTopLimit.get();
    }

    /**
     * Gets the potentiometer value.
     *
     * @return double
     */
    public double getArmPositionPotValue() {
        return armPositionPot.getVoltage();
    }

    /**
     * 
     *
     * @return double
     */
    public double getAccelX() {
        return accel.getAcceleration(ADXL345_I2C.Axes.kX);
    }

    /**
     * 
     *
     * @return double
     */
    public double getAccelY() {
        return accel.getAcceleration(ADXL345_I2C.Axes.kY);
    }

    /**
     * 
     *
     * @return double
     */
    public double getAccelZ() {
        return accel.getAcceleration(ADXL345_I2C.Axes.kZ);
    }

    public boolean getBanner()
    {
        return banner.get();
    }        

    /**********Ultrasonic Methods**********************************************/
    
    /**
     * Gets the value of the first ultrasonic sensor.
     *
     * @return double
     */
    public double getLeftUltrasonicValue() {
        return leftUltrasonic.getValue();
    }

    /**
     * Gets the value of the second ultrasonic sensor.
     *
     * @return double
     */
    public double getRightUltrasonicValue() {
        return rightUltrasonic.getValue();
    }
       
}

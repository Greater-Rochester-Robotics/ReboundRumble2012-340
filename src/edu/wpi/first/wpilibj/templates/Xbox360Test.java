/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Joystick;

/**
 *
 * @author Rex
 */
public class Xbox360Test {
   
    private static Joystick testJoy;

    public static int Iterator;
    public static final int LoopCnt = 20;
    public static final int NumBtns = 12;
    public static final int NumAxis = 5;
    public static final double myRange = 0.1;
    public static boolean[] TestBtnVal = new boolean[NumBtns];
    public static boolean[] TestBtnLst = new boolean[NumBtns];
    public static double[] TestAxisVal = new double[NumAxis];
    public static double[] TestAxisLst = new double[NumAxis];
    
    public static double CurVal;
    public static double TopVal;
    public static double BotVal;
    public static double LstVal;

    public static int Itr, ItrP1;
    
    // Contructor
    public Xbox360Test (int JStkNmbr) {
        testJoy = new Joystick (JStkNmbr);
        for (Itr = 0; Itr < NumBtns; Itr++){
            TestBtnVal[Itr] = false;
            TestBtnLst[Itr] = false;
        }
        for (Itr = 0; Itr < NumAxis; Itr++){
            TestAxisVal[Itr] = 0;
            TestAxisLst[Itr] = 0;
        }
    }
    
    public void DoTest() {
        // Read all buttons and put a message out when the value changes
        for (Itr = 0; Itr < NumBtns; Itr++){
            ItrP1 = Itr + 1;
            TestBtnVal[Itr] = testJoy.getRawButton(ItrP1); // Read Current Value
            if (TestBtnVal[Itr] != TestBtnLst[Itr]) {
                // Print message of change of state.
                switch (Itr) {
                    case 0 : System.out.println("    A = " + TestBtnVal[Itr] + "!"); break;
                    case 1 : System.out.println("    B = " + TestBtnVal[Itr] + "!"); break;
                    case 2 : System.out.println("    X = " + TestBtnVal[Itr] + "!"); break;
                    case 3 : System.out.println("    Y = " + TestBtnVal[Itr] + "!"); break;
                    case 4 : System.out.println("    L = " + TestBtnVal[Itr] + "!"); break;
                    case 5 : System.out.println("    R = " + TestBtnVal[Itr] + "!"); break;
                    case 6 : System.out.println(" Back = " + TestBtnVal[Itr] + "!"); break;
                    case 7 : System.out.println("Start = " + TestBtnVal[Itr] + "!"); break;
                    case 8 : System.out.println("LAnlg = " + TestBtnVal[Itr] + "!"); break;
                    case 9 : System.out.println("RAnlg = " + TestBtnVal[Itr] + "!"); break;
                    case 10: System.out.println(" ?10? = " + TestBtnVal[Itr] + "!"); break;
                    case 11: System.out.println(" ?11? = " + TestBtnVal[Itr] + "!"); break;
                    default: System.out.println(" DFLT = " + TestBtnVal[Itr] + "!"); break;
                }
            }
            TestBtnLst[Itr] = TestBtnVal[Itr]; // Store Last Value
        }
        // Read all Axis and put a message out when the value changes
        for (Itr = 0; Itr < NumAxis; Itr++){
            ItrP1 = Itr + 1;
            TestAxisVal[Itr] = testJoy.getRawAxis(ItrP1); // Read Current Value
            CurVal = Math.abs(TestAxisVal[Itr]);
            LstVal = Math.abs(TestAxisLst[Itr]);
            TopVal = LstVal + myRange;
            BotVal = LstVal - myRange;
            if (CurVal >= TopVal || CurVal <= BotVal || (CurVal == 1.0 && LstVal != 1.0)) {
            // Print message of change of state.
                switch (Itr) {
                    case 0 : 
                        System.out.println(Itr + "LStkHorz = " + TestAxisVal[Itr] + "!" + BotVal + "!" + TopVal);
                        TestAxisLst[Itr] = TestAxisVal[Itr]; // Store Last Value
                        break;
                    case 1 : 
                        System.out.println(Itr + "LStkVert = " + TestAxisVal[Itr] + "!" + BotVal + "!" + TopVal);
                        TestAxisLst[Itr] = TestAxisVal[Itr]; // Store Last Value
                        break;
                    case 2 : 
                        System.out.println(Itr + "Triggers = " + TestAxisVal[Itr] + "!" + BotVal + "!" + TopVal);
                        TestAxisLst[Itr] = TestAxisVal[Itr]; // Store Last Value
                        break;
                    case 3 : 
                        System.out.println(Itr + "RStkHorz = " + TestAxisVal[Itr] + "!" + BotVal + "!" + TopVal);
                        TestAxisLst[Itr] = TestAxisVal[Itr]; // Store Last Value
                        break;
                    case 4 : 
                        System.out.println(Itr + "RStkVert = " + TestAxisVal[Itr] + "!" + BotVal + "!" + TopVal); 
                        TestAxisLst[Itr] = TestAxisVal[Itr]; // Store Last Value
                        break;
                    default: 
                        break;
                }
            }
        }
    }
}

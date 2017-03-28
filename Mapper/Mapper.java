//***************************************************************************************
// Mapper.java
// David Michael Carter
//
// Created 27 March 2017
//
// Maps functions on the Cartesian plane
// INPUT:    xMax (prompted by program)
//           yMax (prompted by program)
//           minimum bound of integration (prompted by program)
//           maximum bound of integration (prompted by program)
//           function f(x) [input line 27]
//
// OUTPUT:   function mapped on cartesian plane
//           Maps on interval [-xMax, xMax]
//           Integral will be visualized
//***************************************************************************************

import javax.swing.*;
import java.awt.*;
import java.util.Scanner;

public class Mapper extends JFrame {
  //  USER INPUT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
  // ******************** SET f(x) ************************
  double f(double x) {
    return Math.sin(x/5);
  }
  String f = "sin(x/5)";
  //*******************************************************
  //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
  
  double currentX;              // current cursor x-position
  double currentY;              // current cursor y-position
  double xCoordToNumberLine;    
  double x2CoordToNumberLine;   
  double yNumberLineToCoord;    
  double y2NumberLineToCoord;   
  double fX;                    
  double f2X;                   
  public static double xMax;    // create xMax double to be set in main method
  public static double yMax;    // create yMax double to be set in main method
  public static double intMin;  // minimum bound of integration
  public static double intMax;  // maximum bound of integration
  int intMinToCoord;
  int intMaxToCoord;
  
  public void paint(Graphics g) {
   g.setColor(Color.black);
   
   // Set up x- and y-axis such that Origin = (450,450)
   //*****************************************************************
   g.drawLine(50,450,850,450);     // x-axis
   g.drawLine(50,450,55,445);      // x-axis arrows
   g.drawLine(50,450,55,455);
   g.drawLine(845,445,850,450);
   g.drawLine(845,455,850,450);
   g.drawString("x", 855,455);     // label "x"
   
   
   g.drawLine(450,50,450,850);     // y-axis
   g.drawLine(445,55,450,50);      // y-axis arrows
   g.drawLine(450,50,455,55);
   g.drawLine(445,845,450,850);
   g.drawLine(450,850,455,845);
   g.drawString("y", 448,866);     // label "y"
   
   g.drawString("f(x) = " + f, 30,50);  // label f(x)
   
   
   // Add tick marks and label them
   for (int i = 0; i<=10; i++) {
     g.drawLine((75 + i*75),445,(75 + i*75),455);    // add tick marks on x-axis
     g.drawLine(445, (75 + i*75),455,(75 + i*75));   // add tick marks on y-axis
     currentX = -(xMax) + 2*i*xMax/10;               // current x-position
     currentY = yMax - 2*i*yMax/10;                  // current y-position
     
     // label x-ticks
     g.drawString(String.format("%.2f", currentX), 70 + i*75, 470);
     
     // label y-ticks
     if (i==5) {continue;}                           // avoid redundant origin labeling
     else {
     g.drawString(String.format("%.2f", currentY), 460, 80 + i*75);
     }
   }
   //*****************************************************************
   
   
   
   // Map f(x)
   //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
   currentX = 75;                                              // Start mapping loop on the left cursor bound
   intMinToCoord = (int)(((intMin + xMax)/(2*xMax))*750 + 75); // transform minimum bound from [-xMax, xMax] 
                                                               //   to cursor position
   intMaxToCoord = (int)(((intMax + xMax)/(2*xMax))*750 + 75); // transform maximum bound from [-xMax, xMax] 
                                                               //   to cursor position
   
   while ((int)currentX < 825) {
     xCoordToNumberLine = ((currentX-75)/750)*2*xMax - xMax;   // transform java cursor to number on [-xMax, xMax]
     fX = f(xCoordToNumberLine);                               // f(x) computed at cursor position
     yNumberLineToCoord = (fX/(-2*yMax))*750 + 450;            // transform number on [-yMax, yMax] to java cursor
     x2CoordToNumberLine = ((currentX-74)/750)*2*xMax - xMax;  // transform java cursor to number on [-xMax, xMax]
     f2X = f(x2CoordToNumberLine);                             // f(x) computed at cursor position + 1
     y2NumberLineToCoord = (f2X /(-2*yMax))*750 + 450;         // transform java cursor + 1 to number on [-yMax, yMax]
     
     //Map by drawing line from current cursor position to cursor position + 1 evaluated by f
     g.drawLine((int)currentX, (int)yNumberLineToCoord, (int)(currentX + 1), (int)y2NumberLineToCoord);
     
     // Visualize Integral
     if ((currentX >= intMinToCoord) && (currentX <= intMaxToCoord)) {
       g.drawLine((int)currentX, 450, (int)currentX,(int)yNumberLineToCoord);
     }
     
     currentX += 1;
   }
   //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
   
   
  }
 
 public static void main(String[] args) throws Exception{
   Scanner s = new Scanner(System.in);
   
   System.out.print("Enter x-max: ");   // prompt domain maximum
   xMax = s.nextDouble();               // set domain max
   System.out.print("Enter y-max: ");   // prompt range maximum
   yMax = s.nextDouble();               // set range max
   
   System.out.print("Enter integration x-min: ");     // prompt integration minimum bound
   intMin = s.nextDouble();                           // set integration minimum bound
   System.out.print("Enter integration x-max: ");     // prompt integration maximum bound
   intMax = s.nextDouble();                           // set integration maximum bound

   
   Mapper m = new Mapper();
   m.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   m.setSize(900,900);
   m.setBackground(Color.white);
   m.setVisible(true);
 }
 
}



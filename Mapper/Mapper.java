//************************************************************************************************************
// Mapper.java
// David Michael Carter
// Created 27 March 2017
//
// Maps functions on the Cartesian plane with an adjustable domain and range
// Integrates function over specified interval using Composite Simpson's Rule
// 
// INPUT:    xMax - domain maximum             (prompted by program)
//           yMax - range maximum              (prompted by program)
//           a - minimum bound of integration  (prompted by program)
//           b - maximum bound of integration  (prompted by program)
//           h - step size                     (prompted by program)
//           function f(x) [code input line 34]
//
// OUTPUT:   function mapped on cartesian plane; interval displayed is [-xMax, xMax]
//           numerical approximation of integral
//           (Integral will be visualized)
//
//     Composite Simpson's Rule: https://en.wikipedia.org/wiki/Simpson%27s_rule#Composite_Simpson.27s_rule
//     ERROR BOUND:
//                   [(h^4)/180]*(b - a)*max{abs(4th derivative of f evaluated in [a, b])}
// 
//************************************************************************************************************

import javax.swing.*;
import java.awt.*;
import java.util.Scanner;

public class Mapper extends JFrame {
  //  USER INPUT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
  // ******************** SET f(x) ************************
  public static double f(double x) {
    return Math.cos(x) + 0.3*x;
  }
  public static String f = "cos(x) + 0.3x";
  //*******************************************************
  //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
  
  
  double currentX;                // current cursor x-position
  double currentY;                // current cursor y-position
  double xCoordToNumberLine;    
  double x2CoordToNumberLine;   
  double yNumberLineToCoord;    
  double y2NumberLineToCoord;   
  double fX;                    
  double f2X;                   
  public static double xMax;      // create domain right-bound
  public static double yMax;      // create range right-bound
  public static double a;         // left bound of integration
  public static double b;         // eight bound of integration
  public static double h;         // step size
  int intMinToCoord;
  int intMaxToCoord;
  public static int n;            // to be determined from step size
  public static double integral;  // integral approximation computed by simpson
  
 
  // Composite Simpson's Rule
  // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
  public static void simpson(double a, double b, double h) { 
    n = (int)((b - a) / h);
    double sum1 = 0;
    double sum2 = 0;
    
    if ((n % 2) != 0) {  // n needs to be even
      n++;
    }
    
    for (int i = 1; i <= ((n/2)-1); i++){
      sum1 += f(a + 2*i*h);
    }
    for (int j = 1; j <= (n/2); j++){
      sum2 += f(a + (2*j-1)*h);
    }    
    
    integral = (h/3)*(f(a) + 2*sum1 + 4*sum2 + f(b));
  }
  // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
  
  
  public void paint(Graphics g) {
   Color pos = new Color(111,145,225);
   Color neg = new Color(240,96,99); 
   
   // Map f(x)
   //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
   currentX = 75;                                              // Start mapping loop on the left bound
   intMinToCoord = (int)(((a + xMax)/(2*xMax))*750 + 75);      // transform minimum bound from [-xMax, xMax] 
                                                               //    to pixel space
   intMaxToCoord = (int)(((b + xMax)/(2*xMax))*750 + 75);      // transform maximum bound from [-xMax, xMax] 
                                                               //    to pixel space
   
   while ((int)currentX < 825) {
     
     xCoordToNumberLine = ((currentX-75)/750)*2*xMax - xMax;   // transform java cursor to number on [-xMax, xMax]
     fX = f(xCoordToNumberLine);                               // f(x) computed at cursor position
     yNumberLineToCoord = (fX/(-2*yMax))*750 + 450;            // transform number on [-yMax, yMax] to java cursor
     x2CoordToNumberLine = ((currentX-74)/750)*2*xMax - xMax;  // transform java cursor to number on [-xMax, xMax]
     f2X = f(x2CoordToNumberLine);                             // f(x) computed at cursor position + 1
     y2NumberLineToCoord = (f2X /(-2*yMax))*750 + 450;         // transform java cursor + 1 to number on [-yMax, yMax]
     
     //Map curve of f(x) on domain
     g.setColor(Color.black);
     g.drawLine((int)currentX, (int)yNumberLineToCoord, (int)(currentX + 1), (int)y2NumberLineToCoord);
     
     // Visualize Integral
     if ((currentX >= intMinToCoord) && (currentX <= intMaxToCoord)) {
       if (fX > 0) {
         g.setColor(pos);
       }
       else if (fX < 0) {
         g.setColor(neg);
       }
       else {
         g.setColor(Color.black);
       }
       
       g.drawLine((int)currentX, 450, (int)currentX,(int)yNumberLineToCoord);
     }
     
     currentX += 1;
   }
   //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
   
   
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
   
   g.drawString("f(x) = " + f, 30,45);  // label f(x)
   g.drawString("The integral approximation of " + f + " from " + a + " to " + b + " with step size " + h + " (using Composite Simpson's Rule) is " + integral, 30,880);  // label f(x)
   
   
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
   
   
  }
 
 public static void main(String[] args) throws Exception{
   Scanner s = new Scanner(System.in);
   
   System.out.print("Enter x-max: ");                 // prompt domain maximum
   xMax = s.nextDouble();                             // set domain max
   System.out.print("Enter y-max: ");                 // prompt range maximum
   yMax = s.nextDouble();                             // set range max
   
   System.out.print("Enter integration x-min: ");     // prompt integration minimum bound
   a = s.nextDouble();                                // set integration minimum bound
   System.out.print("Enter integration x-max: ");     // prompt integration maximum bound
   b = s.nextDouble();                                // set integration maximum bound
   
   System.out.print("Enter step size: ");             // prompt step size
   h = s.nextDouble();                                // set step size
   
   simpson(a, b, h);                                  // execute Composite Simpson's Rule with the given parameters
   
   Mapper m = new Mapper();
   m.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   m.setSize(900,900);
   m.setBackground(Color.white);
   m.setVisible(true);
 }
}



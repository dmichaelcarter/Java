//************************************************************************************************************
// TopoMapper.java
// David Michael Carter
// Created 2 April 2017
//
// Creates a topographic map in the first quadrant of functions of two variables
// Value is represented by shade
//     RGB = (0,0,0)        (black) is the minimum value on the domain
//     RGB = (255,255,255)  (white) is the maximum value on the domain
//     All other values of f(x,y) are displayed proportionally in the range [0,255]
// 
// INPUT:    Function f(x,y) [code input line 26]
//
// OUTPUT:   Topographical representation mapped on the Cartesian Plane
//           Keybindings: UP and DOWN arrow keys change the domain
//************************************************************************************************************

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TopoMapper extends JFrame {  
  //  USER INPUT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
  // ******************** SET f(x,y) **********************
  public static double f(double x, double y) {
    return Math.cos(Math.exp(Math.pow(x, 2) + Math.pow(y, 2)));
  }
  public static String f = "cos(e^(x^2 + y^2))";
  //*******************************************************
  //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
  
  public static double xMax;
  public static double yMax;
  public static double currentX;
  public static double currentY;
  public static double mapMax = 0;
  public static double mapMin = 0;
  
  // Constructor Method for TopoMapper Object
  public TopoMapper() {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(900,900);
    setBackground(Color.white);
    setVisible(true);
    
   // Use Key Bindings to scale the domain
    JRootPane rootPane = getRootPane();
    rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "Up");
    rootPane.getActionMap().put("Up", new ScaleAction(0));
    
    rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "Down");
    rootPane.getActionMap().put("Down", new ScaleAction(1));
    
    // Determine extrema on the domain
    for(int i = 75; i <= 825; i++) {                   
     for(int j = 75; j <= 825; j++) {
       if (f(pixelToX(i), pixelToY(j)) > mapMax) {
         mapMax = f(pixelToX(i), pixelToY(j));
       }
       if (f(pixelToX(i), pixelToY(j)) < mapMin) {
         mapMin = f(pixelToX(i), pixelToY(j));
       }
     }
   }
  }
  
  // Define the methods to be used when a keybinding is executed
  private class ScaleAction extends AbstractAction {
        int direction;

        ScaleAction(int direction) {
            this.direction = direction;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
          // Up Arrow makes the domain smaller to "zoom in"
          if (direction == 0) {
            xMax *= 0.8;
            yMax *= 0.8;
            repaint();
          }
          // Down arrow makes the domain larger to "zoom out"
          if (direction == 1) {
            xMax *= 1.2;
            yMax *= 1.2;
            repaint();
          }
        }
  }
  
  // Transform from x (pixel space) to x (in domain)
  public static double pixelToX (int i) {
    return ((i - 75) / 750.0) * xMax;
  }
 
  // Transform from y (pixel space) to y (in domain)
  public static double pixelToY (int j) {
    return ((825 - j) / 750.0) * yMax;
  }
  
  
  // Paint the graphics
  public void paint(Graphics g) {
    super.paint(g);                        // Clear the panel
    Color point = new Color(0,0,0);
  
    // Iterate through entire pixel space on domain and plot the value of f(x,y)
    for(int i = 75; i <= 825; i++) {
      for(int j = 75; j <= 825; j++) {
        int iRGB = (int)(((f(pixelToX(i), pixelToY(j)) - mapMin) / (mapMax - mapMin)) * 255);
        point = new Color(iRGB, iRGB, iRGB);
        g.setColor(point);
        g.drawLine(i,j,i,j);
      }
    }   
   
    // Set up x- and y-axis such that Origin = (75,825)
    //*****************************************************************
    g.setColor(Color.black);
    g.drawLine(50,825,850,825);     // x-axis
    g.drawLine(50,825,55,820);      // x-axis arrows
    g.drawLine(50,825,55,830);
    g.drawLine(845,820,850,825);
    g.drawLine(845,830,850,825);
    g.drawString("x", 855,830);     // label "x"
   
    g.drawLine(75,50,75,850);       // y-axis
    g.drawLine(70,55,75,50);        // y-axis arrows
    g.drawLine(75,50,80,55);
    g.drawLine(70,845,75,850);
    g.drawLine(75,850,80,845);
    g.drawString("y", 73,863);      // label "y"
   
    g.drawString("f(x,y) = " + f, 420,45);  // label f(x,y)
    g.drawString("USE THE ARROW KEYS UP/DOWN!!!", 400, 860);
   
    // Add tick marks and label them
    for (int i = 0; i<=10; i++) {
      g.drawLine((75 + i*75),820,(75 + i*75),830);    // add tick marks on x-axis
      g.drawLine(70, (75 + i*75),80,(75 + i*75));     // add tick marks on y-axis
      currentX =  i*xMax/10;                          // current x-position
      currentY = yMax - i*yMax/10;                    // current y-position
     
      // label x-ticks
      if (i==0) {
        g.drawString(String.format("%.2f", currentX), 45, 845);
      }
      else {
        g.drawString(String.format("%.2f", currentX), 65 + i*75, 845);
      }
     
      // label y-ticks
      if (i==10) {continue;}                           // avoid redundant origin labeling
      else {
        g.drawString(String.format("%.2f", currentY), 35, 80 + i*75);
      }
    }
    //*****************************************************************
  }
 
 public static void main(String[] args) throws Exception{
   // Test values to initiate domain
   xMax = 2.0;
   yMax = 2.0;
   
   // Create TopoMapper object; begin mapping
   new TopoMapper();
 }
}



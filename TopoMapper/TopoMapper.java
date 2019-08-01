//************************************************************************************************************
// TopoMapper.java
// David Michael Carter
// Created 2 April 2017
//
// Creates a topographic map in the first quadrant of functions of two variables
// Value is represented by shade
//     RGB = (0,0,0)        (black) represents the minimum value on the domain
//     RGB = (255,255,255)  (white) represents the maximum value on the domain
//     All other values of f(x,y) are displayed proportionally in the range [0,255]
// 
// INPUT:    Function f(x,y) [code input line 26]
//
// OUTPUT:   Topographical representation mapped on the Cartesian Plane
//           Keybindings: ARROW KEYS translate the domain
//                        <+> and <-> KEYS zoom the domain in/out
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
  
  public static double xMax = 2;
  public static double yMax = 2;
  public static double xTrans = 0;
  public static double yTrans = 0;
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
    
    JRootPane rootPane = getRootPane();
    
    // Use Key Bindings to scale the domain
    rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ADD, 0), "NumPadPlus");
    rootPane.getActionMap().put("NumPadPlus", new ScaleAction(0));
    
    rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, KeyEvent.SHIFT_DOWN_MASK), "MainPlus");
    rootPane.getActionMap().put("MainPlus", new ScaleAction(0));
    
    rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, 0), "NumPadMinus");
    rootPane.getActionMap().put("NumPadMinus", new ScaleAction(1));
    
    rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, KeyEvent.SHIFT_DOWN_MASK), "MainMinus");
    rootPane.getActionMap().put("MainMinus", new ScaleAction(1));
    
    // Use Key Bindings to translate the domain
    rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "Left");
    rootPane.getActionMap().put("Left", new TransAction(0));
    
    rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "Right");
    rootPane.getActionMap().put("Right", new TransAction(1));
    
    rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "Up");
    rootPane.getActionMap().put("Up", new TransAction(2));
    
    rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "Down");
    rootPane.getActionMap().put("Down", new TransAction(3));
    
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
  
  
  // Define the methods to be used when a scaling keybinding is executed
  private class ScaleAction extends AbstractAction {
        int direction;

        ScaleAction(int direction) {
            this.direction = direction;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
          // ScaleAction(0) makes the domain smaller to "zoom in"
          if (direction == 0) {
            xMax *= 1.0/1.2;
            yMax *= 1.0/1.2;
            repaint();
          }
          // ScaleAction(1) makes the domain larger to "zoom out"
          if (direction == 1) {
            xMax *= 1.2;
            yMax *= 1.2;
            repaint();
          }
        }
  }
  
  
  // Define the methods to be used when a translation keybinding is executed
  private class TransAction extends AbstractAction {
        int direction;

        TransAction(int direction) {
            this.direction = direction;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
          // TransAction(0) translates the x axis by adding a negative number (move image to the right)
          if (direction == 0) {
            xTrans -= xMax / 2; 
            repaint();
          }
          // TransAction(1) translates the x axis by adding a positive number (move image to the left)
          if (direction == 1) {
            xTrans += xMax / 2; 
            repaint();
          }
          // TransAction(2) translates the y axis by adding a negative number (move image down)
          if (direction == 2) {
            yTrans += yMax / 2; 
            repaint();
          }
          // TransAction(3) translates the y axis by adding a positive number (move image up)
          if (direction == 3) {
            yTrans -= yMax / 2; 
            repaint();
          }
        }
  }
  
  // Transform from x (pixel space) to x (in domain)
  public static double pixelToX (int i) {
    return ((i - 75) / 750.0) * 2*xMax - xMax + xTrans;
  }
 
  // Transform from y (pixel space) to y (in domain)
  public static double pixelToY (int j) {
    return ((825 - j) / 750.0) * 2*yMax - yMax + yTrans;
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
    g.drawString("USE +/- TO ZOOM, ARROW KEYS TO SHIFT DOMAIN", 300, 860);
   
    // Add tick marks and label them
    for (int i = 0; i<=10; i++) {
      g.drawLine((75 + i*75),820,(75 + i*75),830);    // add tick marks on x-axis
      g.drawLine(70, (75 + i*75),80,(75 + i*75));     // add tick marks on y-axis
      currentX =  i*2*xMax/10 - xMax + xTrans;        // current x-position
      currentY = yMax - i*2*yMax/10 +yTrans;          // current y-position
     
      // label x-ticks
      if (i==0) {
        g.drawString(String.format("%.2f", currentX), 65, 880);
      }
      else {
        g.drawString(String.format("%.2f", currentX), 65 + i*75, 845);
      }
     
      // label y-ticks
      if (i==10) {
        g.drawString(String.format("%.2f", currentY), 20, 80 + i*75);
      }
      else {
        g.drawString(String.format("%.2f", currentY), 35, 80 + i*75);
      }
    }
    //*****************************************************************
  }
 
 public static void main(String[] args) throws Exception{
   
   // Create TopoMapper object; begin mapping
   new TopoMapper();
 }
}



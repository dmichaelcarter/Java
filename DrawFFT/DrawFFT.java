//************************************************************************************************************
// DrawFFT.java
// David Michael Carter
// Created 03 August 2019
//
// INPUT:   Click & Drag to create a user-defined waveform
//
// OUTPUT:  User-defined waveform is transformed to frequency domain using the Fast Fourier Transform.
//          Frequency data is then transformed back into the time domain using the Inverse FFT.
//
// JAVA FFT SOURCE:             https://introcs.cs.princeton.edu/java/97data/FFT.java
// JAVA COMPLEX NUMBERS SOURCE: https://introcs.cs.princeton.edu/java/32class/Complex.java
//************************************************************************************************************

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class DrawFFT extends JFrame{


  public static Complex y[] = new Complex[256];
  public static Complex fftY[] = new Complex[256];
  public static Complex ifftY[] = new Complex[256];
  public static double amplitudes[] = new double[128];
  public static int intAmplitudes[] = new int[128];
  
  public static void reset(){
    for (int i = 0; i < 256; i++){
      y[i] = new Complex(100,0);
    }
    fftY = FFT.fft(y);
    fftAmplitudes();
    amplitudesToInt();
    ifftY = FFT.ifft(fftY);
  }
  
  public static void fftAmplitudes(){
    for (int j = 0; j < 128; j++){
      amplitudes[j] = fftY[j].abs();
    }
  }
  
  public static void amplitudesToInt(){
    int upperBound = 500;
    int lowerBound = 0;
    
    for (int k = 0; k < 128; k++){
      if ((int) Math.round(amplitudes[k]) > upperBound){
        intAmplitudes[k] = upperBound;
      }
      else if ((int) Math.round(amplitudes[k]) < lowerBound){
        intAmplitudes[k] = lowerBound;
      }
      else {intAmplitudes[k] = (upperBound -(int) Math.round(amplitudes[k]));}
    }
    
  }
   
  public static void main(String[] args){
    JFrame fft = new JFrame("DrawFFT"); 
    fft.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    fft.setSize(858, 700);
    fft.setBackground(Color.white);
    fft.setResizable(false);
   

    JPanel p = new JPanel(){
      {
        setLayout(null);
        
        addMouseMotionListener(new MouseMotionAdapter(){
          public void mouseDragged(MouseEvent point){

            if (((point.getX() > 25) && (point.getX() <= 281)) && ((point.getY() >= 450) && (point.getY() <=550))){
              y[point.getX() -26] = new Complex(point.getY()-400, 0);
              fftY = FFT.fft(y);
              fftAmplitudes();
              amplitudesToInt();
              ifftY = FFT.ifft(fftY);
              repaint();
            }
          }
        });
        
        JButton resetButton = new JButton("Reset");
        add(resetButton);
        resetButton.setBounds(90,570,125,25);
    
        resetButton.addActionListener(new ActionListener(){
          public void actionPerformed(ActionEvent e){
            reset();
            repaint();
          }
        });
      }
      
      public void paint(Graphics g){
        super.paint(g);
        g.setColor(Color.black);
        g.drawString("CLICK & DRAG TO DRAW A WAVE!", 50, 445);
        g.drawString("FAST FOURIER TRANSFORM", 345, 45);
        g.drawString("Frequency vs. Amplitude", 365, 565);
        g.drawString("INVERSE FAST FOURIER TRANSFORM", 600, 445);
        g.drawString("\"Fast Fourier Transform on a User-Defined Waveform\" by David Michael Carter (August 2019)", 150, 665);
        
        //Paint Mouse-dragged points
        for (int i = 0; i < 256; i++){
          
          //Paint all Y-values
          g.drawLine(i+25,(int)(Math.round(y[i].re()))+400, i+25, (int)(Math.round(y[i].re()))+400);
          
          //Vertically interpolate when adjacent Y-values are distant
          if(i < 255){
            if(Math.abs((int)(Math.round(y[i].re()))-(int)(Math.round(y[i+1].re()))) > 1){
              g.drawLine(i+25,(int)(Math.round(y[i].re()))+400, i+25, (int)(Math.round(y[i+1].re()))+400);
            }
          }
        }
        
        //Paint FFT
        for (int k = 0; k < 128; k++){
         
            g.drawLine(2*k+301,intAmplitudes[k]+50 , 2*k+301, intAmplitudes[k]+50);
            
            //Vertically interpolate when adjacent FFT frequencies are distant
            if(k < 127){
              if(Math.abs(intAmplitudes[k]-intAmplitudes[k+1]) > 1){
                g.drawLine(2*k+301, intAmplitudes[k]+50, 2*k+302, intAmplitudes[k+1]+50);
              }
            }
        }
        
        //Paint iFFT (drawing approximation) points
        for (int j = 0; j < 256; j++){
          g.drawLine(j+577,(int)(Math.round(ifftY[j].re()))+400, j+577, (int)(Math.round(ifftY[j].re()))+400);
          
          //Vertically interpolate when adjacent Y-values are distant
          if(j < 255){
            if(Math.abs((int)(Math.round(ifftY[j].re()))-(int)(Math.round(ifftY[j+1].re()))) > 1){
              g.drawLine(j+577,(int)(Math.round(ifftY[j].re()))+400, j+577, (int)(Math.round(ifftY[j+1].re()))+400);
            }
          }
        }
        
        //Draw PaintBox window elements
        g.drawLine(25,450,281,450);
        g.drawLine(25,450,25,550);
        g.drawLine(25,550,281,550);
        g.drawLine(281,450,281,550);
        //Draw FFT window elements
        g.drawLine(301,50,557,50);
        g.drawLine(301,50,301,550);
        g.drawLine(301,550,557,550);
        g.drawLine(557,50,557,550);
        //Draw iFFT window elements
        g.drawLine(577,450,833,450);
        g.drawLine(577,450,577,550);
        g.drawLine(577,550,833,550);
        g.drawLine(833,450,833,550);        
      }
    };
    
    reset();
    p.setOpaque(false);
    fft.add(p);
    fft.setVisible(true);
  }
}

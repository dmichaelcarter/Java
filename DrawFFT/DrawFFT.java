//************************************************************************************************************
// DrawFFT.java
// David Michael Carter
// Created 03 August 2019
//
// INPUT:   Click & Drag to create a user-defined waveform, or select/edit a preset wave.
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
  public static int frequency = 1;
  public static boolean isSine = true;
  public static boolean isSquare = false;
  public static boolean isSawtooth = false;
  
  //Reset wave to Sine with frequency = 1
  public static void reset(){
    isSine = true;
    isSquare = false;
    isSawtooth = false;
    frequency = 1;
    doSine();
    compute();
  }
  
  //Generate a Sine wave
  public static void doSine(){
    for (int u = 0; u < 256; u++){
          y[u] = new Complex(Math.sin((u+1.0)*frequency/(256.0/(2.0*Math.PI))),0);
        }
  }
  
  //Generate a Square wave
  public static void doSquare(){
    int squarePeriod = 256 / (frequency*2);
    int squareCounter = 0;
    Complex squareValue = new Complex(0.9,0);
    for (int v = 0; v<256; v++){
      if (squareCounter <= squarePeriod){
        y[v] = squareValue;
        squareCounter++;
      }
      else{
        squareCounter = 1;
        squareValue = squareValue.times(new Complex(-1,0));
        y[v] = squareValue;
      }
    }
  }
  
  //Generate a Sawtooth wave
  public static void doSawtooth(){
    int sawtoothPeriod = 256 / frequency;
    int sawtoothCounter = 0;
    double sawtoothSlope = 2.0 / sawtoothPeriod;
   // Complex sawtoothValue = new Complex(-1,0);
    for (int w = 0; w<256; w++){
      if (sawtoothCounter <= sawtoothPeriod){
        y[w] = new Complex(sawtoothSlope*sawtoothCounter -1.0, 0);
        sawtoothCounter++;
      }
      else{
        sawtoothCounter = 1;
        //sawtoothValue = new Complex(-1,0);
        y[w] = new Complex(-1,0);
      }
    }
  }
  
  //Compute FFT
  public static void compute(){
    fftY = FFT.fft(y);
    fftAmplitudes();
    amplitudesToInt();
    ifftY = FFT.ifft(fftY);
  }
  
  //Compute amplitudes of Complex FFT values
  public static void fftAmplitudes(){
    for (int j = 0; j < 128; j++){
      amplitudes[j] = fftY[j].abs();
    }
  }
  
  //Convert amplitude values from (double) to (int)
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
      else {intAmplitudes[k] = (upperBound -(int) Math.round(amplitudes[k]*3.6));}
    }
  }
  
  //Determine which type of wave is selected; generate it
  public static void doWave(){
    if (isSine){
      doSine();
      compute();
    }
    else if (isSquare){
      doSquare();
      compute();
    }
    else if (isSawtooth){
      doSawtooth();
      compute();
    }
    else{System.out.println("oops");}
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
        
        //Add mouse listener
        addMouseMotionListener(new MouseMotionAdapter(){
          public void mouseDragged(MouseEvent point){

            if (((point.getX() > 25) && (point.getX() <= 281)) && ((point.getY() >= 450) && (point.getY() <=550))){
              y[point.getX() -26] = new Complex(((100-(point.getY() -450))/50.0)-1, 0);
              compute();
              repaint();
            }
          }
        });
        
        //Create "Reset" button that resets data to Sine wave with frequency = 1
        JButton resetButton = new JButton("Reset");
        add(resetButton);
        resetButton.setBounds(90,590,125,25);
        resetButton.addActionListener(new ActionListener(){
          public void actionPerformed(ActionEvent e){
            reset();
            repaint();
          }
        });
        
        //Create a button to generate a Sine wave
        JButton sineButton = new JButton("Sine");
        add(sineButton);
        sineButton.setBounds(25,50,125,25);
        sineButton.addActionListener(new ActionListener(){
          public void actionPerformed(ActionEvent e){
            isSine = true;
            isSquare = false;
            isSawtooth = false;
            doWave();
            repaint();
          }
        });
        
        //Create a button to generate a Square wave
        JButton squareButton = new JButton("Square");
        add(squareButton);
        squareButton.setBounds(25,80,125,25);
        squareButton.addActionListener(new ActionListener(){
          public void actionPerformed(ActionEvent e){
            isSine = false;
            isSquare = true;
            isSawtooth = false;
            doWave();
            repaint();
          }
        });
        
        //Create a button to generate a Sawtooth wave
        JButton sawtoothButton = new JButton("Sawtooth");
        add(sawtoothButton);
        sawtoothButton.setBounds(25,110,125,25);
        sawtoothButton.addActionListener(new ActionListener(){
          public void actionPerformed(ActionEvent e){
            isSine = false;
            isSquare = false;
            isSawtooth = true;
            doWave();
            repaint();
          }
        });
        
        //Create button to increase frequency by 1
        JButton freqDownButton = new JButton("Frequency (-)");
        add(freqDownButton);
        freqDownButton.setBounds(25,560,125,25);
        freqDownButton.addActionListener(new ActionListener(){
          public void actionPerformed(ActionEvent e){
            if (frequency > 1){
              frequency--;
              doWave();
              repaint();
            }
          }
        });
        
        //Create cutton to decrease frequency by 1
        JButton freqUpButton = new JButton("Frequency (+)");
        add(freqUpButton);
        freqUpButton.setBounds(153,560,125,25);
        freqUpButton.addActionListener(new ActionListener(){
          public void actionPerformed(ActionEvent e){
            if (frequency < 127){
              frequency++;
              doWave();
              repaint();
            }
          }
        });
      }
      
      public void paint(Graphics g){
        super.paint(g);
        g.setColor(Color.black);
        g.drawString("CLICK & DRAG TO DRAW A WAVE!", 50, 445);
        g.drawString("FAST FOURIER TRANSFORM", 345, 45);
        g.drawString("INVERSE FAST FOURIER TRANSFORM", 600, 445);
        g.drawString("\"Fast Fourier Transform on a User-Defined Waveform\" by David Michael Carter (August 2019)", 150, 665);
        
        
        //Paint Mouse-dragged points
        for (int i = 0; i < 256; i++){          
          //Paint all Y-values
          g.drawLine(i+25,(int)(100-Math.round((y[i].re()+1)*50.0)+450), i+25, (int)(100-Math.round((y[i].re()+1)*50.0)+450));
          
          //Vertically interpolate when adjacent Y-values are distant
          if (i < 255){
            if(Math.abs((int)Math.round((y[i].re()+1)*50.0)-(int)Math.round((y[i+1].re()+1)*50.0)) > 1){
              g.drawLine(i+25,(int)(100-Math.round((y[i].re()+1)*50.0)+450), i+25, (int)(100-Math.round((y[i+1].re()+1)*50.0)+450));
            }
          }
        }
        
        //Paint FFT
        for (int j = 0; j < 128; j++){
         
            g.drawLine(2*j+301,intAmplitudes[j]+50 , 2*j+301, intAmplitudes[j]+50);
            
            //Vertically interpolate when adjacent FFT frequencies are distant
            if (j < 127){
              if(Math.abs(intAmplitudes[j]-intAmplitudes[j+1]) > 1){
                g.drawLine(2*j+301, intAmplitudes[j]+50, 2*j+302, intAmplitudes[j+1]+50);
              }
            }
        }
        
        //Paint iFFT (drawing approximation) points
        for (int k = 0; k < 256; k++){
          g.drawLine(k+577,(int)(100-Math.round((ifftY[k].re()+1)*50.0)+450), k+577, (int)(100-Math.round((ifftY[k].re()+1)*50.0)+450));
          
          //Vertically interpolate when adjacent Y-values are distant
           if (k < 255){
            if (Math.abs((int)(100-Math.round((ifftY[k].re()+1)*50.0)+450)-(int)(100-Math.round((ifftY[k+1].re()+1)*50.0)+450)) > 1){
              g.drawLine(k+577,(int)(100-Math.round((ifftY[k].re()+1)*50.0)+450), k+577, (int)(100-Math.round((ifftY[k+1].re()+1)*50.0)+450));
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
        
        int leftBorderFFT = 298;
        //Draw tick marks on DFT Frequency axis, spaced by 16hz
        for (int i = 0; i <= 256; i += 32){
          g.drawLine(301 + i,545,301 + i,555);
        }
        //Label frequency axis tick marks
        for (int j = 0; j <= 128; j += 16){
          g.drawString(String.valueOf(j), leftBorderFFT + 2*j, 570);
        }
        g.drawString("Hz", leftBorderFFT + 128, 595);
        
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

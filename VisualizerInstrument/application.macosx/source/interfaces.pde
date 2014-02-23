
/// abstract class for audio visualization

abstract class AudioRenderer implements AudioListener {
  int parameter = 50;
  int maxParameter = 100;
  int hue;
  int screenWidth;
  int screenHeight;
  
  float[] left;
  float[] right;
  synchronized void samples(float[] samp) { left = samp; }
  synchronized void samples(float[] sampL, float[] sampR) { left = sampL; right = sampR; }

  LeapMotion leap;
  PVector position;
  
  AudioRenderer(int sW, int sH, LeapMotion lm) {
    screenWidth = sW;
    screenHeight = sH;
    leap = lm;
  }
  
  abstract void setup();
  abstract void draw();
  void increaseParameter() {
    if(leap.getHands().size() > 0) {
      parameter = (int) map(getXY().y, 0, screenHeight, maxParameter, 0);
    }
    else if(parameter < maxParameter) {
      parameter++;
    }
    println("Parameter set to " + parameter);
  }
  void decreaseParameter() {
    if(parameter > 0) {
      parameter--;
    }
    println("Parameter set to " + parameter);
  }
  void setHue(int newHue) {
    hue = newHue;
  }
  abstract void attack();
  
  void glowCircle(int hue, int radius, int x, int y) {
    
    fill(map(hue, 0, 500, 0, 100), 100, 100, 10);
    for(int r = radius; r > radius*0.1; r/=1.3) {
      ellipse(x, y, r, r);
    }
    fill(0, 0, 100, 65);
    ellipse(x, y, radius*0.3, radius*0.3);
  }
  
  PVector getXY() {
    try {
      position = leap.getHands().get(0).getPosition();
    } 
    catch(Exception e) {
      //System.out.print(e);
      position = new PVector(mouseX, mouseY);
    }
    return position;
  }
}


// abstract class for FFT visualization

import ddf.minim.analysis.*;

abstract class Renderer extends AudioRenderer {
  
  FFT fft; 
  float maxFFT;
  float[] leftFFT;
  float[] rightFFT;
  
  Renderer(AudioSource source, int sW, int sH, LeapMotion lm) {
    super(sW, sH, lm);
    float gain = .125;
    fft = new FFT(source.bufferSize(), source.sampleRate());
    maxFFT =  source.sampleRate() / source.bufferSize() * gain;
    fft.window(FFT.HAMMING);
  }
  
  void calc(int bands) {
    if(left != null) {
      leftFFT = new float[bands];
      fft.linAverages(bands);
      fft.forward(left);
      for(int i = 0; i < bands; i++) leftFFT[i] = fft.getAvg(i);   
    }
  }
}

class BlankRenderer extends Renderer {
  
  BlankRenderer(AudioSource source, int sW, int sH) {
    super(source, sW, sH, null);
  }
  
  void setup() {
  }
  void draw() {
  }
  void attack() {
  }
}

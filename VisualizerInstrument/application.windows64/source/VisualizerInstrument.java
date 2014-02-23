import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import de.voidplus.leapmotion.*; 
import development.*; 
import ddf.minim.*; 
import ddf.minim.analysis.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class VisualizerInstrument extends PApplet {




/* Music Visualizer Instrument
 * An instrument that controls visualizers.
 *
 *
 *
 *
 */




Minim minim;

AudioSource groove;

int screenWidth;
int screenHeight;

int maxFadeSpeed = 20;
int fadeSpeed = maxFadeSpeed/2;
int maxHue = 500;
int hue = 0;
int maxCycleSpeed = 10;
int cycleSpeed = 5;
int maxBrightness = 100;
int brightness = 50;

AudioRenderer layers[];
boolean display[] = {false, false, false, false, false, false, false, false, false, false};

LeapMotion leap;

public void setup() {
  screenWidth = displayWidth;
  screenHeight = displayHeight;
  // setup graphics
  size(screenWidth, screenHeight);
  colorMode(HSB, 100);
  noStroke();
  fill(0);
  rect(0, 0, screenWidth, screenHeight);
  rectMode(CENTER);
  ellipseMode(RADIUS);

  // setup player
  minim = new Minim(this);
  groove = minim.getLineIn();

  leap = new LeapMotion(this);

  AudioRenderer blank = new BlankRenderer(groove, screenWidth, screenHeight);
  AudioRenderer linear = new LinearRenderer(groove, screenWidth, screenHeight, leap);
  AudioRenderer radial = new RadialRenderer(groove, screenWidth, screenHeight, leap);
  AudioRenderer streak = new StreakRenderer(groove, screenWidth, screenHeight, leap);
  AudioRenderer pixelStreak = new PixelStreakRenderer(groove, screenWidth, screenHeight, leap);
  AudioRenderer flowerTile = new FlowerTileRenderer(groove, screenWidth, screenHeight, leap);
  AudioRenderer glowSchool = new GlowSchoolRenderer(groove, screenWidth, screenHeight, leap);
  AudioRenderer fibonacciBloom = new FibonacciBloomRenderer(groove, screenWidth, screenHeight, leap);
  AudioRenderer concentric = new ConcentricRenderer(groove, screenWidth, screenHeight, leap);
  AudioRenderer fingertips = new FingertipsRenderer(groove, screenWidth, screenHeight, leap);
  layers = new AudioRenderer[] {blank, linear, radial, streak, pixelStreak, flowerTile, glowSchool, fibonacciBloom, concentric, fingertips};

  noCursor();
}

public void draw() {
  //fill(0, 0, 0, map(fadeSpeed, 0, maxFadeSpeed, 0, 100));
  //rect(screenWidth/2, screenHeight/2, screenWidth, screenHeight);

  for (int i = 0; i < layers.length; i++) {
    if (display[i]) {
      layers[i].draw();
    }
  }

  hue = (hue + cycleSpeed)%maxHue;
  for (int i = 0; i < layers.length; i++) {
    layers[i].setHue(hue + ((int) map(i, 0, layers.length, 0, maxHue/2)));
  }
  delay(10);
  
  fill(0, 0, 0, map(brightness, 0, maxBrightness, 100, 0));
  rect(screenWidth/2, screenHeight/2, screenWidth, screenHeight);
}

public void keyPressed() {
  if ('0' <= key && key <= '9') {
    int select = Character.getNumericValue(key);
    display[select] = !display[select];
    print("Layer " + layers[select].getClass().getName() + " is ");
    if (display[select]) {
      println("on");
    } 
    else {
      println("off");
    }
  }
  switch(key) {
  case '-':
    if (brightness > 0) {
      brightness--;
    }
    println("Brightness set to " + brightness);
    break;
  case '=':
    if(leap.getHands().size() > 0) {
      brightness = (int) map(layers[1].getXY().y, 0, screenHeight, maxBrightness, 0);
    }
    else if(brightness < maxBrightness) {
      brightness++;
    }
    println("Brightness set to " + brightness);
    break;
  case 'q':
    layers[1].increaseParameter();
    break;
  case 'w':
    layers[2].increaseParameter();
    break;
  case 'e':
    layers[3].increaseParameter();
    break;
  case 'r':
    layers[4].increaseParameter();
    break;
  case 't':
    layers[5].increaseParameter();
    break;
  case 'y':
    layers[6].increaseParameter();
    break;
  case 'u':
    layers[7].increaseParameter();
    break;
  case 'i':
    layers[8].increaseParameter();
    break;
  case 'o':
    layers[9].increaseParameter();
    break;
  case 'p':
    layers[0].increaseParameter();
    break;
  case 'a':
    layers[1].decreaseParameter();
    break;
  case 's':
    layers[2].decreaseParameter();
    break;
  case 'd':
    layers[3].decreaseParameter();
    break;
  case 'f':
    layers[4].decreaseParameter();
    break;
  case 'g':
    layers[5].decreaseParameter();
    break;
  case 'h':
    layers[6].decreaseParameter();
    break;
  case 'j':
    layers[7].decreaseParameter();
    break;
  case 'k':
    layers[8].decreaseParameter();
    break;
  case 'l':
    layers[9].decreaseParameter();
    break;
  case ';':
    layers[0].decreaseParameter();
    break;
  case 'z':
    layers[1].attack();
    break;
  case 'x':
    layers[2].attack();
    break;
  case 'c':
    layers[3].attack();
    break;
  case 'v':
    layers[4].attack();
    break;
  case 'b':
    layers[5].attack();
    break;
  case 'n':
    layers[6].attack();
    break;
  case 'm':
    layers[7].attack();
    break;
  case ',':
    layers[8].attack();
    break;
  case '.':
    layers[9].attack();
    break;
  case '/':
    layers[0].attack();
    break;
  case ' ':
    for (int i = 0; i < layers.length; i++) {
      if (display[i]) {
        layers[i].attack();
      }
    }
  default:
    break;
  }
  switch(keyCode) {
  case UP:
    if (fadeSpeed < maxFadeSpeed) {
      fadeSpeed++;
    }
    println("Fade speed set to " + fadeSpeed);
    break;
  case DOWN:
    if (fadeSpeed > 0) {
      fadeSpeed--;
    }
    println("Fade speed set to " + fadeSpeed);
    break;
  case RIGHT:
    if (cycleSpeed < maxCycleSpeed) {
      cycleSpeed++;
    }
    println("Cycle speed set to " + cycleSpeed);
    break;
  case LEFT:
    if (cycleSpeed > 0) {
      cycleSpeed--;
    }
    println("Cycle speed set to " + cycleSpeed);
    break;
  case TAB:
    fill(0, 0, 0, 100);
    rect(screenWidth/2, screenHeight/2, screenWidth, screenHeight);
    println("Screen cleared");
    break;
  default:
    break;
  }
}

public void stop() {
  groove.close();
  minim.stop();
  super.stop();
}


class ConcentricRenderer extends Renderer {
  
  int radius = 0;
  
  ConcentricRenderer(AudioSource source, int sW, int sH, LeapMotion lm) {
    super(source, sW, sH, lm);
  }

  public void setup() { 
  } 
  
  public void draw() {
    noFill();
    stroke(map(hue, 0, 500, 0, 100), 100, 100);
    strokeWeight(5);
    radius = (int) map(parameter, 0, maxParameter, 0, screenHeight/1.5f);
    ellipse(screenWidth/2, screenHeight/2, radius, radius);
    noStroke();
  }
  
  public void attack() {
  }

}



class FibonacciBloomRenderer extends Renderer {
  
  PVector cp1 = new PVector();
  PVector cp2 = new PVector();
  float iterAngle = 0;
  
  FibonacciBloomRenderer(AudioSource source, int sW, int sH, LeapMotion lm) {
    super(source, sW, sH, lm);
  }

  public void setup() {
  }

  public synchronized void draw() {
    stroke(map(hue, 0, 500, 0, 100), 100, 100, 30);
    fill(map(hue, 0, 500, 0, 100), 100, 100, 3);
    int magnitude = 50;
    iterAngle += map(parameter, 0, maxParameter, 0.05f, 0.01f);
    float angle = iterAngle;
    while(magnitude < map(parameter, 0, maxParameter, 0, screenWidth*5)) {
      cp1 = PVector.fromAngle(angle);
      cp1.setMag(magnitude);
      cp2 = PVector.fromAngle(angle + 45);
      cp2.setMag(magnitude);
      curve(cp1.x + screenWidth/2, cp1.y + screenHeight/2, screenWidth/2, screenHeight/2, screenWidth/2, screenHeight/2, cp2.x + screenWidth/2, cp2.y + screenHeight/2);
      magnitude += 150;
      angle += 30;
    }
    noStroke();
  }
  
  public void attack() {
    stroke(0, 0, 0);
    int magnitude = 50;
    float angle = iterAngle;
    while(magnitude < map(parameter, 0, maxParameter, 0, screenWidth*5)) {
      cp1 = PVector.fromAngle(angle);
      cp1.setMag(magnitude);
      cp2 = PVector.fromAngle(angle + 45);
      cp2.setMag(magnitude);
      curve(cp1.x + screenWidth/2, cp1.y + screenHeight/2, screenWidth/2, screenHeight/2, screenWidth/2, screenHeight/2, cp2.x + screenWidth/2, cp2.y + screenHeight/2);
      magnitude += 50;
      angle += 30;
    }
    delay(100);
    noStroke();
  }
}



class FingertipsRenderer extends Renderer {

  LeapMotion leap;
  PVector position;

  FingertipsRenderer(AudioSource source, int sW, int sH, LeapMotion lm) {
    super(source, sW, sH, lm);
    leap = lm;
  }

  public void setup() {
  } 

  public void draw() {
    try {
      for (Hand hand : leap.getHands()) {
        for (Finger finger : hand.getFingers()) {
          position = finger.getPosition();
          println(position);
          glowCircle(hue, (int) map(position.z, 0, 100, 10, 50), (int) map(position.x, -120, 1000, 0, screenWidth), (int) map(position.y, 0, 500, 0, screenHeight));
        }
      }
    } 
    catch(NullPointerException e) {
      System.out.print(e);
    }
  }

  public void attack() {
  }
}


class FlowerTileRenderer extends Renderer {
  
  int tileSize = 50;
  PVector position;
  
  FlowerTileRenderer(AudioSource source, int sW, int sH, LeapMotion lm) {
    super(source, sW, sH, lm);
  }

  public void setup() { 
  } 
  
  public void draw() {
    fill(map(hue, 0, 500, 0, 100), 100, 100);
    position = getXY();
    if(parameter%2 == 0) {
      ellipse(((int) position.x)/(tileSize)*(tileSize), ((int) position.y)/(tileSize)*(tileSize), tileSize, tileSize);
    } else {
      ellipse(((int) position.x)/(tileSize*2)*(tileSize*2), ((int) position.y)/(tileSize*2)*(tileSize*2), tileSize, tileSize);
    }
    stroke(map(hue, 0, 500, 0, 100), 100, 25);
    strokeWeight(2);
    noFill();
    boolean draw = true;
    for(int i = 0; i <= screenWidth; i += tileSize) {
      for(int j = 0; j <= screenHeight; j += tileSize) {
        if(draw) {
          ellipse(i, j, tileSize, tileSize);
        }
        draw = !draw;
      }
    }
    noStroke();
  }
  
  public void attack() {
    fill(map(hue, 0, 500, 0, 100), 100, 100);
    rect(screenWidth/2, screenHeight/2, screenWidth, screenHeight);
    draw();
    fill(0, 0, 100);
    if(parameter%2 == 0) {
      ellipse(((int) position.x)/(tileSize)*(tileSize), ((int) position.y)/(tileSize)*(tileSize), tileSize, tileSize);
    } else {
      ellipse(((int) position.x)/(tileSize*2)*(tileSize*2), ((int) position.y)/(tileSize*2)*(tileSize*2), tileSize, tileSize);
    }
  }

}



class GlowSchoolRenderer extends Renderer {
  
  int schoolCoords[][];
  int size;
  int chaos;
  
  GlowSchoolRenderer(AudioSource source, int sW, int sH, LeapMotion lm) {
    super(source, sW, sH, lm);
    chaos = 40;
    schoolCoords = new int[5][2];
    for(int i = 0; i < schoolCoords.length; i++) {
      schoolCoords[i][0] = mouseX + ((int) random(-chaos*5, chaos*5));
      schoolCoords[i][1] = mouseY + ((int) random(-chaos*5, chaos*5));
    }
  }

  public void setup() { 
  } 
  
  public void draw() {
    size = parameter/2;
    for(int i = 0; i < schoolCoords.length; i++) {
      glowCircle(hue, size, schoolCoords[i][0], schoolCoords[i][1]);
      schoolCoords[i][0] = (schoolCoords[i][0]*i + (int) getXY().x)/(i + 1) + ((int) random(-chaos, chaos));
      schoolCoords[i][1] = (schoolCoords[i][1]*i + (int) getXY().y)/(i + 1) + ((int) random(-chaos, chaos));
    }
  }
  
  public void attack() {
    chaos *= 10;
    draw();
    chaos /= 10;
  }

}



/// abstract class for audio visualization

abstract class AudioRenderer implements AudioListener {
  int parameter = 50;
  int maxParameter = 100;
  int hue;
  int screenWidth;
  int screenHeight;
  
  float[] left;
  float[] right;
  public synchronized void samples(float[] samp) { left = samp; }
  public synchronized void samples(float[] sampL, float[] sampR) { left = sampL; right = sampR; }

  LeapMotion leap;
  PVector position;
  
  AudioRenderer(int sW, int sH, LeapMotion lm) {
    screenWidth = sW;
    screenHeight = sH;
    leap = lm;
  }
  
  public abstract void setup();
  public abstract void draw();
  public void increaseParameter() {
    if(leap.getHands().size() > 0) {
      parameter = (int) map(getXY().y, 0, screenHeight, maxParameter, 0);
    }
    else if(parameter < maxParameter) {
      parameter++;
    }
    println("Parameter set to " + parameter);
  }
  public void decreaseParameter() {
    if(parameter > 0) {
      parameter--;
    }
    println("Parameter set to " + parameter);
  }
  public void setHue(int newHue) {
    hue = newHue;
  }
  public abstract void attack();
  
  public void glowCircle(int hue, int radius, int x, int y) {
    
    fill(map(hue, 0, 500, 0, 100), 100, 100, 10);
    for(int r = radius; r > radius*0.1f; r/=1.3f) {
      ellipse(x, y, r, r);
    }
    fill(0, 0, 100, 65);
    ellipse(x, y, radius*0.3f, radius*0.3f);
  }
  
  public PVector getXY() {
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



abstract class Renderer extends AudioRenderer {
  
  FFT fft; 
  float maxFFT;
  float[] leftFFT;
  float[] rightFFT;
  
  Renderer(AudioSource source, int sW, int sH, LeapMotion lm) {
    super(sW, sH, lm);
    float gain = .125f;
    fft = new FFT(source.bufferSize(), source.sampleRate());
    maxFFT =  source.sampleRate() / source.bufferSize() * gain;
    fft.window(FFT.HAMMING);
  }
  
  public void calc(int bands) {
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
  
  public void setup() {
  }
  public void draw() {
  }
  public void attack() {
  }
}

class LinearRenderer extends Renderer {

  LinearRenderer(AudioSource source, int sW, int sH, LeapMotion lm) {
    super(source, sW, sH, lm);
  }

  public void setup() {  
  }

  public synchronized void draw() {
    fill(map(hue, 0, 500, 0, 100), 100, 10);
    rect(screenWidth/2, screenHeight/2, map(parameter, 0, maxParameter, 0, screenWidth), 10);
  }
  
  public void attack() {
    fill(map(hue, 0, 500, 0, 100), 100, 100);
    rect(screenWidth/2, screenHeight/2, map(parameter, 0, maxParameter, 0, screenWidth), 10);
  }
  
}

class PixelStreakRenderer extends Renderer {
  
  int x;
  int y;
  int dx;
  int dy;
  
  PixelStreakRenderer(AudioSource source, int sW, int sH, LeapMotion lm) {
    super(source, sW, sH, lm);
    x = mouseX;
    y = mouseY;
    dx = (int) random(1, 10);
    dy = (int) random(1, 10);
  }

  public void setup() { 
  } 
  
  public void draw() {
    if(x > screenWidth || x < 0) {
      dx = -dx;
    }
    if(y > screenHeight || y < 0) {
      dy = -dy;
    }
    fill(map(hue, 0, 500, 0, 100), 100, 100);
    rect(x, y, parameter, parameter);
    x += dx;
    y += dy;
  }
  
  public void attack() {
    fill(map(hue, 0, 500, 0, 100), 100, 100);
    rect(x, y, parameter*4, parameter*4);
  }

}



class RadialRenderer extends Renderer {
  
  float highlightAngle = 0;
  
  RadialRenderer(AudioSource source, int sW, int sH, LeapMotion lm) {
    super(source, sW, sH, lm);
  }

  public void setup() { 
  } 
  
  public void draw() {
    float angle = 0;
    PVector pos = PVector.fromAngle(0);
    while(angle < PI*2) {
      fill(map(hue, 0, 500, 0, 100), 100, 60);
      if(angle < highlightAngle && angle > (highlightAngle - PI/7)) {
        fill(map(hue, 0, 500, 0, 100), 100, 5);
      }
      pos = PVector.fromAngle(angle);
      pos.setMag(parameter*3);
      angle += PI/7;
      ellipse(pos.x + screenWidth/2, pos.y + screenHeight/2, parameter/3, parameter/3);
    }
    highlightAngle = (highlightAngle + 1/((float) parameter*0.3f))%(PI*2);
  }
  
  public void attack() {
    float angle = 0;
    PVector pos = PVector.fromAngle(0);
    while(angle < PI*2) {
      pos = PVector.fromAngle(angle);
      pos.setMag(screenHeight/2 - 50);
      angle += PI/7;
      glowCircle(hue, parameter/2, (int) pos.x + screenWidth/2, (int) pos.y + screenHeight/2);
    }    
  }

}



class StreakRenderer extends Renderer {
  
  int x;
  int y;
  int dx;
  int dy;
  
  StreakRenderer(AudioSource source, int sW, int sH, LeapMotion lm) {
    super(source, sW, sH, lm);
    x = mouseX;
    y = mouseY;
    dx = (int) random(1, 10);
    dy = (int) random(1, 10);
  }

  public void setup() { 
  } 
  
  public void draw() {
    if(x > screenWidth || x < 0) {
      dx = -dx;
    }
    if(y > screenHeight || y < 0) {
      dy = -dy;
    }
    fill(map(hue, 0, 500, 0, 100), 100, 100);
    ellipse(x, y, parameter, parameter);
    x += dx;
    y += dy;
  }
  
  public void attack() {
    fill(map(hue, 0, 500, 0, 100), 100, 100);
    ellipse(x, y, parameter*4, parameter*4);
  }

}


  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--full-screen", "--bgcolor=#666666", "--stop-color=#cccccc", "VisualizerInstrument" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}

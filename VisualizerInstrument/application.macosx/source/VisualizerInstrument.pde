import de.voidplus.leapmotion.*;
import development.*;

/* Music Visualizer Instrument
 * An instrument that controls visualizers.
 *
 *
 *
 *
 */


import ddf.minim.*;

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

void setup() {
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

void draw() {
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

void keyPressed() {
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

void stop() {
  groove.close();
  minim.stop();
  super.stop();
}


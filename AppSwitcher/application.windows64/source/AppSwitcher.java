import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.awt.*; 
import java.awt.event.*; 
import peasy.test.*; 
import peasy.org.apache.commons.math.*; 
import peasy.*; 
import peasy.org.apache.commons.math.geometry.*; 
import oscP5.*; 
import netP5.*; 
import java.lang.reflect.*; 
import ddf.minim.*; 
import ddf.minim.analysis.*; 
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

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class AppSwitcher extends PApplet {





SuperformulaContainer sketch1;
VisualizerInstrument sketch2;
PApplet currentSketch;

public void setup() {
  size(displayWidth, displayHeight);
  sketch1 = new SuperformulaContainer();
  sketch2 = new VisualizerInstrument();
  sketch1.init();
//  sketch1.noLoop();
  sketch2.init();
//  sketch2.noLoop();
  frame.add(sketch1);
  frame.add(sketch2);
  setCurrentSketch(sketch1);
  registerSwitcherListener();
  this.setSize(1, 1);
  this.setVisible(false);
  stroke(0); fill(0); background(0);
}
public void draw() {
//  currentSketch.redraw();
}

public void registerSwitcherListener() {
  KeyboardFocusManager focusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
  focusManager.addKeyEventPostProcessor(new KeyEventPostProcessor() {
    
    public boolean postProcessKeyEvent(java.awt.event.KeyEvent ke) {
      if(ke.getID() == java.awt.event.KeyEvent.KEY_PRESSED && ke.getKeyCode() == java.awt.event.KeyEvent.VK_SHIFT) {
        switchSketch();
      }
      if(ke.getID() == java.awt.event.KeyEvent.KEY_PRESSED && ke.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE) {
        exit();
      }
      
      return false;
    }
    
  });
//  sketch.addKeyListener(new java.awt.event.KeyAdapter() {
//    public void keyPressed(java.awt.event.KeyEvent evt) {
//      println(evt);
//      if(evt.getKeyChar() == ' ') {
//        switchSketch();
//      }
//    }
//  });
}

public void switchSketch() {
  setCurrentSketch(currentSketch == sketch1 ? sketch2 : sketch1);
  currentSketch.requestFocus();
  println("switched sketch!");
}

public void setCurrentSketch(PApplet sketch) {
  if(currentSketch != null) {
//   frame.remove(currentSketch);
    currentSketch.setVisible(false);
  }
  currentSketch = sketch;
  currentSketch.setVisible(true);
//  frame.add(currentSketch);
//  currentSketch.redraw();
//  println(sketch.g.image);
}














class SuperformulaContainer extends PApplet {

PeasyCam cam;
OscP5 oscP5;
ReactiveSF voice1 = new ReactiveSF(),
           voice2 = new ReactiveSF();


public synchronized void setup() {
  size(displayWidth, displayHeight, P3D);

  cam = new PeasyCam(this, +745);
  cam.setActive(false);
//  cam.setRotations( -0.80, -0.30, +0.20);

  oscP5 = new OscP5(this,12000);
  background(0);
  noSmooth();
  noCursor();
}

public synchronized void draw() {
  background(0);
  lights();
  
  pushMatrix();
  translate(-420, 0, 0);
  voice1.update();
  popMatrix();
  
  translate(420, 0, 0);
  voice2.update();
  
//  println(frameRate);
}

public synchronized void oscEvent(OscMessage m) throws Exception {
  println("Got message: "+m.addrPattern()+", typetag: "+m.typetag()+", arguments: ");
  println(m.arguments());
  if(m.checkAddrPattern("bang")) {
  } else {
    try{
      String voice = m.addrPattern().substring(1, 7);
      ReactiveSF p1 = voice.equals("voice1") ? voice1 : voice2;
      
      Field f = ReactiveSF.class.getDeclaredField(m.addrPattern().substring(8));
      
      if(m.checkTypetag("f")) {
        f.setFloat(p1, m.get(0).floatValue());
      } else if(m.checkTypetag("d")) {
        f.setFloat(p1, (float)m.get(0).doubleValue());
      } else if(m.typetag().length() > 1) { //an array
        ArrayList<Float> list = new ArrayList<Float>();
        for(Object o : m.arguments()) {
          list.add(((Double)o).floatValue());
        }
        f.set(p1, list);
      }
      
    }catch(NoSuchFieldException e) {
//      println("couldn't find "+m.addrPattern().substring(1));
    }catch(Exception e) {
      e.printStackTrace();
    }
  }
}

class ReactiveSF {
  
  Superformula s;
  
  //holds the last values
  float pitch, brightness, loudness, noisiness, amplitude;
  ArrayList<Float> bark;
  ArrayList<Float> peaks;
  
  float rotX, rotY,
        timeA, timeB, timeC,
        ampA, ampB, ampC,
        dRotX, dRotY,
        dtA, dtB, dtC;
  
  public ReactiveSF() {
    bark = new ArrayList<Float>();
    peaks = new ArrayList<Float>();
    
    s = new Superformula(200, 100, 1, 1, 8, 2, 1, 8);
  }
  
  public void update() {
    
    dtA += sq(amplitude) * 1900;
    dtB += sq(amplitude) * 1900;
    dtC = 30;
    
    rotX += dRotX;
    rotY += dRotY;
    
    timeA += dtA;
    timeB += dtB;
    timeC += dtC;
    
    float drag = .95f;
    
    dRotX *= drag;
    dRotY *= drag;
    
    dtA *= drag;
    dtB *= drag;
    dtC *= drag;
    
    ampA += amplitude * .2f;
    ampC = constrain(ampC + brightness * .25f, 0, 7);
//    brightness = 0;
    float dragAmp = .5f;
    ampA *= dragAmp;
    ampB *= dragAmp;
    ampC *= .98f;
//    ampC *= dragAmp;
    
    ampB = .02f;
    
    rotX += 60 / 1250f;
    rotY += 60 / 750f;
    
    float a = 1 + ampA * sin(timeA * DEG_TO_RAD * .16f);
    float b = 1 + ampB * sin(timeB * DEG_TO_RAD * .125f);
    double m = 8 + ampC * sin(timeC * DEG_TO_RAD * .02f);
    
    double n2 = map(mouseY, 0, height, -5, 5);
    double n3 = map(mouseX, 0, width, -4, 4);
//    double n2 = 1,
//           n3 = 8;
    s.
      a(a).
      b(b).
      m(m).
      n2(n2).
      n3(n3).
      update(bark);
    rotateX(rotX);
    rotateY(rotY);
    s.drawMesh(SuperformulaContainer.this, false, false, true, false, cam.getPosition());
//    println(amplitude);
  }
}

public class Superformula {
    
  // math constants
  private static final double PI      = Math.PI;
  private static final double TWO_PI  = PI*2.0f;
  private static final double HALF_PI = PI*0.5f;
  private static final int WHITE = 255;
   
  // Params
  public double a, b, m , n1, n2, n3;
  private int lon_res, lat_res;
 
  // Precomputed values
  private float[] sf_lon_cos, sf_lon_sin; // longitude
  private float[] sf_lat_cos, sf_lat_sin; // lattitude
  
  public float maxDist;
 
   
  public Superformula(int res_x, int res_y, double a, double b, double m, double n1, double n2, double n3) {
    this
    .a(a)
    .b(b)
    .m(m)
    .n1(n1)
    .n2(n2)
    .n3(n3)
    .setResolution(res_x, res_y)
    .update(new ArrayList(1));
  }
   
  
  //////////////////////////////////////////////////////////////////////////////
  // PARAMETERS
  //////////////////////////////////////////////////////////////////////////////
     
  // SET SHAPE PARAMS
  public Superformula a (double a ){ this.a  = a ; return this; }
  public Superformula b (double b ){ this.b  = b ; return this; }
  public Superformula m (double m ){ this.m  = m ; return this; }
  public Superformula n1(double n1){ this.n1 = n1; return this; }
  public Superformula n2(double n2){ this.n2 = n2; return this; }
  public Superformula n3(double n3){ this.n3 = n3; return this; }
   
  public Superformula setResolution(int lon_res, int lat_res){
     
    this.lon_res = lon_res;
    this.lat_res = lat_res;
 
    sf_lon_cos = new float[this.lon_res]; 
    sf_lon_sin = new float[this.lon_res]; 
                                     
    sf_lat_cos = new float[this.lat_res];
    sf_lat_sin = new float[this.lat_res];
     
    return this;
  }
   
  // GET SHAPE PARAMS
  public double a (){ return a ; }
  public double b (){ return b ; }
  public double m (){ return m ; }
  public double n1(){ return n1; }
  public double n2(){ return n2; }
  public double n3(){ return n3; }
  public int resx(){ return lon_res; }
  public int resy(){ return lat_res; }
   
 
//  public void printParams(){
//    System.out.printf(Locale.ENGLISH, "--------- Superformula: Shape Params ----------\n" );
//    System.out.printf(Locale.ENGLISH, "res = %d/%d\n", lon_res, lat_res );
//    System.out.printf(Locale.ENGLISH, "a   = %+6.3f\n", a  );
//    System.out.printf(Locale.ENGLISH, "b   = %+6.3f\n", b  );
//    System.out.printf(Locale.ENGLISH, "m   = %+6.3f\n", m  );
//    System.out.printf(Locale.ENGLISH, "n1  = %+6.3f\n", n1 );
//    System.out.printf(Locale.ENGLISH, "n2  = %+6.3f\n", n2 );
//    System.out.printf(Locale.ENGLISH, "n3  = %+6.3f\n", n3 );
//  }
   
   
  //////////////////////////////////////////////////////////////////////////////
  // SUPERFORMULA
  //////////////////////////////////////////////////////////////////////////////
   
  private double SUPERFORMULA(final double f) {
    //http://en.wikipedia.org/wiki/Superformula
    final double s = m*f*0.25f;
    return Math.pow((  Math.pow(Math.abs(Math.cos(s)*1/a), n2) +
                       Math.pow(Math.abs(Math.sin(s)*1/b), n3) ), -1/n1 );
  }
   
  public void update(ArrayList<Float> bark){
    maxDist = 0;
    // longitude
    {
      double lon, sf_lon, lon_step = TWO_PI / (lon_res);
      for(int i = 0; i < lon_res; i++){
        lon = -PI + i*lon_step;
        sf_lon = SUPERFORMULA(lon);
        sf_lon_cos[i] = (float)(sf_lon * Math.cos(lon));
        sf_lon_sin[i] = (float)(sf_lon * Math.sin(lon));
      }
    }
     
    // lattitude
    {
      double lat, sf_lat, lat_step = PI / (lat_res-1);
      for(int i = 0; i < lat_res; i++){
        lat = -HALF_PI + i*lat_step;
        sf_lat = SUPERFORMULA(lat);
//        float mult = bark.isEmpty() ? 1 : map(bark.get((int)map(i, 0, lat_res, 0, bark.size())), -60, 0, .5, 2);
        float mult = 1;
        sf_lat_cos[i] = (float)(sf_lat * Math.cos(lat)) * 100 * mult;
        sf_lat_sin[i] = (float)(sf_lat * Math.sin(lat)) * 100 * mult;
      }
    }
  }
   
   
 
   
   
   
   
 
  //////////////////////////////////////////////////////////////////////////////
  // DRAW
  //////////////////////////////////////////////////////////////////////////////
   
  // V represents a small path of the surface, that gets shifted during
  // drawing, so each vertex gets only updated once !!
  private Vertex[][] V = new Vertex[0][4];
  
  // create/init/fill starting mesh-patch
  private void initPatch(){
    if( V.length != lat_res){
      V = new Vertex[lat_res][4];
      for(int i = 0; i < 4; i++)
        for(int j = 0; j < lat_res; j++)
          V[j][i] = new Vertex(j);
    }
   
    for(int j = 0; j < lat_res; j++){
      V[j][0].updatePos(0);
      V[j][1].updatePos(1);
      V[j][2].updatePos(2);
      V[j][3].updatePos(3);
    }
     
    for(int j = 0; j < lat_res; j++){
      V[j][1].updateNormalAndAO(0, 1, 2);
      V[j][2].updateNormalAndAO(1, 2, 3);
    }
  }
        
   
  // temporary buffers
  private final PVector P1 = new PVector(),    //_      P2    
                        P2 = new PVector(),    //_      |     
                        P3 = new PVector(),    //_ P1---P---P3
                        P4 = new PVector(),    //_      |     
                        C  = new PVector();    //_      P4  Face
                        
  private final int[] COLOR_SCHEME = new int[] {
    0xFF333333,
    0xFF66624D,
    0xFF999166,
    0xFFCCC080,
    0xFFFFEE99
  };
 
  private class Vertex{
    final int j; // j is fixed
    final PVector p = new PVector();
    final PVector n = new PVector();
    final PVector n_face = new PVector();;
    float ao = 1;
    int myColor;
 
    Vertex(int j){
      this.j = j;
    }
     
    private final void updatePos(int i){
      i = (i+lon_res)%lon_res;
      p.x = sf_lat_cos[j] * sf_lon_cos[i];
      p.y = sf_lat_cos[j] * sf_lon_sin[i];
      p.z = sf_lat_sin[j];
      recalculateColor();
      maxDist = max(maxDist, p.mag());
    }
    
    public void recalculateColor() {
      float d = dist(p.x, p.y, p.z, 0, 0, 0);
      float idx = map(d, 0, 400, 0, COLOR_SCHEME.length);
      if(idx < COLOR_SCHEME.length - 1) myColor = lerpColor(COLOR_SCHEME[(int)idx], COLOR_SCHEME[(int)(idx+1)], idx%1);
      else myColor = COLOR_SCHEME[COLOR_SCHEME.length - 1];
    }
 
    private final void updateNormalAndAO(int l, int c, int r){
      PVector.sub(V[j][l].p, p, P1);
      PVector.sub(V[j][r].p, p, P3);
       
      // normal
      n.set(0, 0, 0); // reset
      if( j > 0         ) {  PVector.sub(V[j-1][c].p, p, P2); n.add(PVector.cross(P1, P2, C     ));  n.add(PVector.cross(P2, P3, C)); }
      if( j < lat_res-1 ) {  PVector.sub(V[j+1][c].p, p, P4); n.add(PVector.cross(P3, P4, n_face));  n.add(PVector.cross(P4, P1, C)); }
      n.normalize();
       
      // face normal ... already done during normal computation (3 lines above)
//      if( j < lat_res-1 ) PVector.cross(P3, P4, n_face);
       
       
      // ambient occlusion factor
      ao = 1;
                           P1.normalize();  ao -= Math.max(0, P1.dot(n));
                           P3.normalize();  ao -= Math.max(0, P3.dot(n));
      if( j > 0         ){ P2.normalize();  ao -= Math.max(0, P2.dot(n)); }
      if( j < lat_res-1 ){ P4.normalize();  ao -= Math.max(0, P4.dot(n)); }
 
      ao = Math.max(ao,0);
    }
     
    // returns true, if a face is facing the camera
    public boolean frontface(){
      return PVector.sub(CAM, p, VIEW).dot(n_face) > 0;
    }
  }
   
   
  private final PVector CAM  = new PVector();
  private final PVector VIEW = new PVector();
  private PApplet papplet;
   
  public void drawMesh(PApplet papplet, boolean smooth, boolean ao, boolean faces, boolean edges, float[] cam_pos){
    this.papplet = papplet;
     
    this.CAM.x = cam_pos[0];
    this.CAM.y = cam_pos[1];
    this.CAM.z = cam_pos[2];
 
    initPatch();
 
    int id_, id0 = 0, id1 = 1, id2 = 2, id3 = 3;
     
    for(int i = 0; i < lon_res; i++){
 
      if( faces )  FACES_quadStrip( id1, id2, smooth, ao);
      if( edges )  EDGES_quads    ( id1, id2);
       
      // rotate id's, to shift mesh-patch!
      id_ = id0;  id0 = id1; id1 = id2; id2 = id3; id3 = id_;//(++id3)%4;
       
      // update next row ...
      for(int j = 0;j < lat_res; j++){ 
        V[j][id3].updatePos(i+4);                   // get new vertex position
        V[j][id2].updateNormalAndAO(id1, id2, id3); // get old new vertex normal and ao (and face normal for backface culling)
      }
    }
  
  }
   
   
  // draw faces using a quad-strip, ...is using backface-culling.
  private void FACES_quadStrip(int id1, int id2, boolean smooth, boolean ao){
     
    papplet.fill(WHITE); // in case ao=false
    papplet.noStroke();
    papplet.beginShape(PConstants.QUAD_STRIP);
    {
      boolean cut = false;
      Vertex A, B;
      for( int j = 0; j < lat_res; j++){ 
        // using degenerate quads: necessary when doing backface-culling!!!
        A = V[j][id1];
        B = V[j][id2];
//        if( A.frontface() ){
//          if( cut ){
//            vertex(A, A);
//            vertex(A, B, smooth, ao);
//            cut = false;
//          } else {
//            vertex(A, B, smooth, ao);
//          }
//        } else if( !cut ){
//            vertex(A, B, smooth, ao);
//            vertex(B, B, smooth, ao);
//            cut = true;
//        }
         
        vertex(A, B); // simple version: no backface-culling
 
      }
    }
    papplet.endShape();
  }
   
  // draw edges using quads (quad-strip didnt work) ...is using backface-culling.
  private void EDGES_quads(int id1, int id2){
    papplet.noFill();
    papplet.stroke(75, 25, 25);
    papplet.strokeWeight(1.5f);
    
    papplet.beginShape(PConstants.QUADS);
    {
      for( int j = 0; j < lat_res-1; j++){ 
        //  V1----V4
        //   |    | 
        //  V2----V3
        if( V[j][id1].frontface() ){
          quad(V[j  ][id1], V[j+1][id1], V[j+1][id2], V[j  ][id2]);
        }
      }
    }
    papplet.endShape();
     
  }
   
  final private void vertex(Vertex v1, Vertex v2){
//    float diff = dist(v1.p.x, v1.p.y, v1.p.z, v2.p.x, v2.p.y, v2.p.z);
    papplet.fill(v1.myColor);
    papplet.vertex(v1.p.x, v1.p.y, v1.p.z);
    papplet.fill(v2.myColor);
//    papplet.fill(lerpColor(v2.myColor, color(255), diff / 40));
    papplet.vertex(v2.p.x, v2.p.y, v2.p.z);
  }
   
 
  final private void quad(Vertex v1, Vertex v2, Vertex v3, Vertex v4){
    papplet.vertex(v1.p.x, v1.p.y, v1.p.z);
    papplet.vertex(v2.p.x, v2.p.y, v2.p.z);
    papplet.vertex(v3.p.x, v3.p.y, v3.p.z);
    papplet.vertex(v4.p.x, v4.p.y, v4.p.z);
  }
   
   
}
}
 
 
 
 

 
 
 
 
 
 
 
 

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
  if(keyPressed) {
    keyPressed2();
  }
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
}

public void keyPressed2() {
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

//
//  static public void main(String[] passedArgs) {
//    String[] appletArgs = new String[] { "--full-screen", "--bgcolor=#666666", "--stop-color=#cccccc", "VisualizerInstrument" };
//    if (passedArgs != null) {
//      PApplet.main(concat(appletArgs, passedArgs));
//    } else {
//      PApplet.main(appletArgs);
//    }
//  }
}

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--full-screen", "--bgcolor=#666666", "--stop-color=#cccccc", "AppSwitcher" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}

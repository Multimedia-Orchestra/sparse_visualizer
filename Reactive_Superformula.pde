import peasy.test.*;
import peasy.org.apache.commons.math.*;
import peasy.*;
import peasy.org.apache.commons.math.geometry.*;

import oscP5.*;
import netP5.*;

import java.lang.reflect.*;

import ddf.minim.*;
import ddf.minim.analysis.*;


PeasyCam cam;
OscP5 oscP5;
ReactiveSF voice1 = new ReactiveSF(),
           voice2 = new ReactiveSF();


synchronized void setup() {
  size(displayWidth, displayHeight, P3D);
  
  cam = new PeasyCam(this, +745);
//  cam.setRotations( -0.80, -0.30, +0.20);

  oscP5 = new OscP5(this,12000);
  background(0);
  noSmooth();
}

synchronized void draw() {
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

synchronized void oscEvent(OscMessage m) throws Exception {
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
    
    float drag = .95;
    
    dRotX *= drag;
    dRotY *= drag;
    
    dtA *= drag;
    dtB *= drag;
    dtC *= drag;
    
    ampA += amplitude * .2;
    ampC = constrain(ampC + brightness * .25f, 0, 7);
//    brightness = 0;
    float dragAmp = .5;
    ampA *= dragAmp;
    ampB *= dragAmp;
    ampC *= .98f;
//    ampC *= dragAmp;
    
    ampB = .02;
    
    rotX += 60 / 1250f;
    rotY += 60 / 750f;
    
    float a = 1 + ampA * sin(timeA * DEG_TO_RAD * .16);
    float b = 1 + ampB * sin(timeB * DEG_TO_RAD * .125);
    double m = 8 + ampC * sin(timeC * DEG_TO_RAD * .02);
    
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
    s.drawMesh(Reactive_Superformula.this, false, false, true, false, cam.getPosition());
//    println(amplitude);
  }
}

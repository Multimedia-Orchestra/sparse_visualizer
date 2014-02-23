
import java.awt.*;
import java.awt.event.*;

SuperformulaContainer sketch1;
VisualizerInstrument sketch2;
PApplet currentSketch;

void setup() {
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
void draw() {
//  currentSketch.redraw();
}

void registerSwitcherListener() {
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

void switchSketch() {
  setCurrentSketch(currentSketch == sketch1 ? sketch2 : sketch1);
  currentSketch.requestFocus();
  println("switched sketch!");
}

void setCurrentSketch(PApplet sketch) {
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

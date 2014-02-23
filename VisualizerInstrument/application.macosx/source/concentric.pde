
class ConcentricRenderer extends Renderer {
  
  int radius = 0;
  
  ConcentricRenderer(AudioSource source, int sW, int sH, LeapMotion lm) {
    super(source, sW, sH, lm);
  }

  void setup() { 
  } 
  
  void draw() {
    noFill();
    stroke(map(hue, 0, 500, 0, 100), 100, 100);
    strokeWeight(5);
    radius = (int) map(parameter, 0, maxParameter, 0, screenHeight/1.5);
    ellipse(screenWidth/2, screenHeight/2, radius, radius);
    noStroke();
  }
  
  void attack() {
  }

}



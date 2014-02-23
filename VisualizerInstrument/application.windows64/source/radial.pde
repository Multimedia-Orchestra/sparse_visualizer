
class RadialRenderer extends Renderer {
  
  float highlightAngle = 0;
  
  RadialRenderer(AudioSource source, int sW, int sH, LeapMotion lm) {
    super(source, sW, sH, lm);
  }

  void setup() { 
  } 
  
  void draw() {
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
    highlightAngle = (highlightAngle + 1/((float) parameter*0.3))%(PI*2);
  }
  
  void attack() {
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



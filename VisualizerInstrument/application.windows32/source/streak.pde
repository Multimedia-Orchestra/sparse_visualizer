
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

  void setup() { 
  } 
  
  void draw() {
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
  
  void attack() {
    fill(map(hue, 0, 500, 0, 100), 100, 100);
    ellipse(x, y, parameter*4, parameter*4);
  }

}



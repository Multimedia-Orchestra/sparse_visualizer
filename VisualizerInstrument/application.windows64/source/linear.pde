
class LinearRenderer extends Renderer {

  LinearRenderer(AudioSource source, int sW, int sH, LeapMotion lm) {
    super(source, sW, sH, lm);
  }

  void setup() {  
  }

  synchronized void draw() {
    fill(map(hue, 0, 500, 0, 100), 100, 10);
    rect(screenWidth/2, screenHeight/2, map(parameter, 0, maxParameter, 0, screenWidth), 10);
  }
  
  void attack() {
    fill(map(hue, 0, 500, 0, 100), 100, 100);
    rect(screenWidth/2, screenHeight/2, map(parameter, 0, maxParameter, 0, screenWidth), 10);
  }
  
}

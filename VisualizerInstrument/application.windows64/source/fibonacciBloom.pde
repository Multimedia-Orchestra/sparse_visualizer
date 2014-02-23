
class FibonacciBloomRenderer extends Renderer {
  
  PVector cp1 = new PVector();
  PVector cp2 = new PVector();
  float iterAngle = 0;
  
  FibonacciBloomRenderer(AudioSource source, int sW, int sH, LeapMotion lm) {
    super(source, sW, sH, lm);
  }

  void setup() {
  }

  synchronized void draw() {
    stroke(map(hue, 0, 500, 0, 100), 100, 100, 30);
    fill(map(hue, 0, 500, 0, 100), 100, 100, 3);
    int magnitude = 50;
    iterAngle += map(parameter, 0, maxParameter, 0.05, 0.01);
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
  
  void attack() {
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



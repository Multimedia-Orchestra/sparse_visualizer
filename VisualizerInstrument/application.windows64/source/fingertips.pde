
class FingertipsRenderer extends Renderer {

  LeapMotion leap;
  PVector position;

  FingertipsRenderer(AudioSource source, int sW, int sH, LeapMotion lm) {
    super(source, sW, sH, lm);
    leap = lm;
  }

  void setup() {
  } 

  void draw() {
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

  void attack() {
  }
}



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

  void setup() { 
  } 
  
  void draw() {
    size = parameter/2;
    for(int i = 0; i < schoolCoords.length; i++) {
      glowCircle(hue, size, schoolCoords[i][0], schoolCoords[i][1]);
      schoolCoords[i][0] = (schoolCoords[i][0]*i + (int) getXY().x)/(i + 1) + ((int) random(-chaos, chaos));
      schoolCoords[i][1] = (schoolCoords[i][1]*i + (int) getXY().y)/(i + 1) + ((int) random(-chaos, chaos));
    }
  }
  
  void attack() {
    chaos *= 10;
    draw();
    chaos /= 10;
  }

}



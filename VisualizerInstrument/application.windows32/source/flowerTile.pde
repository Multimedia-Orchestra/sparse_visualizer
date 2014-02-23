
class FlowerTileRenderer extends Renderer {
  
  int tileSize = 50;
  PVector position;
  
  FlowerTileRenderer(AudioSource source, int sW, int sH, LeapMotion lm) {
    super(source, sW, sH, lm);
  }

  void setup() { 
  } 
  
  void draw() {
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
  
  void attack() {
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



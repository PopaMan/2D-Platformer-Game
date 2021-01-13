import java.util.ArrayList;
import java.awt.image.BufferedImage;
public class Enemy {
  private double xPos;
  private double yPos;
  private double xVel;
  private double initXVel;
  private double yVel = 0;
  public double width;
  public double height;
  private int spriteIdx;
  private int spriteCount = 0;
  private int state = -1;
  private ArrayList<ArrayList<BufferedImage>> enemyImages;
  private BufferedImage currImage;
  public boolean leftBottomBlocked = true;
  public boolean rightBottomBlocked = true;
  public boolean rightBlocked = false;
  public boolean leftBlocked = false;
  
  public Enemy(double xPos, double yPos, double width, double height, double xVel, ArrayList<ArrayList<BufferedImage>> enemyImages) { 
    this.xPos = xPos;
    this.yPos = yPos;
    this.xVel = xVel*state;
    this.initXVel = xVel;
    this.width = width;
    this.height = height;
    this.enemyImages = enemyImages;
    this.currImage = enemyImages.get(Math.abs(this.state)-1).get(0);
  }
  
  public double getInitXVel() {
    return this.initXVel;
  }
  
  public double getXVel() {
    return this.xVel;
  }
  
  public void setXVel(double value) {
    this.xVel = value;
  }
  
  public BufferedImage getCurrImage() {
    return this.currImage;
  }
  
  public double getWidth() {
    return this.width;
  }
  
  public double getHeight() {
    return this.height;
  }
  
  public double getXPos() {
    return this.xPos;
  }
  
  public void setXPos(double value) {
    this.xPos = value;
  }
  
  public double getYPos() {
    return this.yPos;
  }
  
  public void setYPos(double value) {
    this.yPos = value;
  }
  
  public void setYVel(double value) {
    this.yVel = value;
  }
  
  public double getYVel() {
    return this.yVel;
  }
  
  public int getState() {
    return this.state;
  }
  
  public void setState(int value) {
    this.state = value;
  }
  
  public int getSpriteCount() {
    return this.spriteCount;
  }
  
  public void setSpriteCount(int value) {
    this.spriteCount = value;
  }
  
  public int getSpriteIdx() {
    return this.spriteIdx;
  }
  
  public void setSpriteIdx(int value) {
    this. spriteIdx = value;
  }
  
  public void move() {
    this.xPos += this.xVel;
    
    if(this.spriteIdx >= enemyImages.get(Math.abs(state)-1).size()) {
      
      if (Math.abs(this.state) != 2) {
        this.spriteIdx = 0;
      }
            
      else {
        this.spriteIdx = enemyImages.get(Math.abs(state)-1).size()-1;
      }
    }
    this.currImage = enemyImages.get(Math.abs(state)-1).get(this.spriteIdx);
  }
  
  
}

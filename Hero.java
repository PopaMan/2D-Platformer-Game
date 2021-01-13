import java.util.ArrayList;
import java.awt.image.BufferedImage;

public class Hero {
  private int health;
  public boolean jumpHold = false;
  public boolean shotHold = false;
  private int spriteIdx;
  private int spriteCount = 0;
  private boolean inAir = true;
  private boolean inJump = false;
  
  private int state = 1;
  public int tempState;
  public double width;
  public double height;
  public boolean rightBlocked = false;
  public boolean leftBlocked = false;
  public boolean bottomBlocked = false;
  
  private double xPos;
  private double yPos;
  private double speed;
  private ArrayList<ArrayList<BufferedImage>> heroImages;
  private BufferedImage currImage;
  
  public Hero(double xPos, double yPos, double width, double height, double speed, ArrayList<ArrayList<BufferedImage>> heroImages) { 
    this.heroImages = heroImages;
    this.currImage = heroImages.get(0).get(0);
    this.speed = speed;
    this.width = width;
    this.height = height;
    this.xPos = xPos;
    this.yPos = yPos;
  } //end of constructor
  
  public double getXPos() {
    return this.xPos;
  }
  
  public double getYPos() {
    return this.yPos;
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
  
  
  public boolean isInJump() {
    return this.inJump;
  }
  
  public void setJumpState(boolean value) {
    this.inJump = value;
  }
  
  public boolean isInAir() {
    return this.inAir;
  }
  
  public void setAirState(boolean value) {
    this.inAir = value;
  }
  
  public void setSpriteCount(int value) {
    this.spriteCount = value;
  }
  
  public double getSpeed() {
    return this.speed;
  }
  
  
  public int getSpriteIdx() {
    return this.spriteIdx;
  }
  
  public void setSpriteIdx(int value) {
    this. spriteIdx = value;
  }
  
  public void move() {
    if(this.spriteIdx >= heroImages.get(Math.abs(state)-1).size()) {
      
      if (Math.abs(this.state) != 3 && Math.abs(this.state)!= 4) {
        this.spriteIdx = 0;
        if (Math.abs(this.state) == 5) {
          //this.state =Math.abs(this.state) / this.state * 1;
          this.state = this.tempState;
          return;
        }
      }
            
      else {
        this.spriteIdx = heroImages.get(Math.abs(state)-1).size()-1;
      }
    }
    this.currImage = heroImages.get(Math.abs(state)-1).get(this.spriteIdx);
  }
    
  public BufferedImage getCurrImage() {
    return this.currImage;
  }
  
  
} //end of class

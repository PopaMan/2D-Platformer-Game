import java.awt.image.BufferedImage;
public class Block {
  private double width;
  private double height;
  private double xPos;
  private double yPos;
  private double xVel;
  private double yVel;
  private int blockType;
  private BufferedImage image;
  public Block(double width, double height, double xPos, double yPos, BufferedImage image, int blockType) { 
    this.width = width;
    this.height = height;
    this.xPos = xPos;
    this.yPos = yPos;
    this.image = image;
    this.blockType = blockType;
  } //end of constructor
  
  public int getBlockType() {
    return this.blockType;
  }
  
  public void move() {
    this.xPos += xVel;
    this.yPos += yVel;
  }
  
  public double getXPos() {
    return this.xPos;
  }
  
  public void setYPos(double value) {
    this.yPos = value;
  }
  
  public double getYPos() {
    return this.yPos;
  }
  
  public void setXVel(double value) {
    this.xVel = value;
  }
  
  public void setYVel(double value) {
    this.yVel = value;
  }
  
  public double getXVel() {
    return this.xVel;
  }
  
  public double getYVel() {
    return this.yVel;
  }
  
  public double getWidth() {
    return this.width;
  }
  
  public double getHeight() {
    return this.height;
  }
  public BufferedImage getImage() {
    return this.image;
  }
  
} //end of class

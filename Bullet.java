import java.awt.image.BufferedImage;
public class Bullet {
  private double xPos;
  private double yPos;
  private double xVel;
  private BufferedImage image;
  public Bullet(double xVel, double xPos, double yPos, BufferedImage image) { 
    this.xPos = xPos;
    this.yPos = yPos;
    
  }
  
  public void move() {
    this.xPos += this.xVel;
  }
  
}

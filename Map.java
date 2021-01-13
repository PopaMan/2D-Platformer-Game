import java.util.ArrayList;
public class Map {
  private ArrayList<Block> blocks;
  private ArrayList<Enemy> enemies;
  private double gravity = 2;
  public double blockXVel = 0;
  public double blockYVel = 0;
  public Map(ArrayList<Block> blocks) { 
    this.blocks = blocks;
  } //end of constructor
  
  public void setEnemies(ArrayList<Enemy> enemies) {
    this.enemies = enemies;
  }
  
  public ArrayList<Enemy> getEnemies() {
    return this.enemies;
  }
  
  public ArrayList<Block> getBlocks() {
    return this.blocks;
  }
  
  public double getGravity() {
    return this.gravity;
  }
} //end of class

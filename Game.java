import javax.swing.JFrame;
import javax.swing.JPanel;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.LineUnavailableException;
import java.lang.InterruptedException;

public class Game extends JPanel {
  static final long serialVersionUID = 0;
  static final int GAME_LOOP_DELAY = 40; //default delay for game loop
  static int screenLength;
  static int screenHeight;
  static ArrayList<Enemy> enemies = new ArrayList<Enemy>();
  
  public static ArrayList<Block> readMaps(String filename, ArrayList<BufferedImage> blockImages) throws FileNotFoundException, IOException{
    File mapFile = new File ("maps\\" + filename + ".txt");
    BufferedReader reader = new BufferedReader(new FileReader("maps\\" + filename + ".txt"));
    int lines = 0;
    while (reader.readLine() != null) lines++;
    reader.close();
    
    Scanner inputFile = new Scanner(mapFile);
    int rowNum = 0;
    int colNum = 0;
    String alphabet = "abcdefghijklmnopqrstuvwxyz";
    ArrayList<Block> blocks = new ArrayList<Block>();
    while (inputFile.hasNext()) {
      String line = inputFile.nextLine();
      
      double blockWidth = screenHeight / 10.0;
      double blockHeight = screenHeight / 10.0;
      double blockYPos = screenHeight - ((lines - rowNum)*blockHeight + (0.5 * blockHeight));
      
      for (int i = 0; i< line.length(); i++) {
        colNum = i+1;
        double blockXPos = (colNum-1)*blockWidth;
        char blockType = line.charAt(i);
        if(blockType != 'z' &&  blockType != 'X') {
          Block block = new Block(blockWidth, blockHeight, blockXPos - 400, blockYPos+400, blockImages.get(alphabet.indexOf(blockType)), alphabet.indexOf(blockType)+1);
          blocks.add(block);
        }
      }
      rowNum++;
    } // end of while loop
    inputFile.close();
    return blocks;
  } //end of readMaps()
  
  
  public static void main(String[] args) throws IOException, InterruptedException, UnsupportedAudioFileException, LineUnavailableException {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    screenLength = (int)(screenSize.getWidth());
    screenHeight = (int)(screenSize.getHeight());
    
    
    
    ArrayList<BufferedImage> blockImages = new ArrayList<BufferedImage>();
    for (int i = 0; i < 19; i++) {
      BufferedImage block = ImageIO.read(new File("mapImages\\Tiles\\" + (i+1) + ".png"));
      blockImages.add(block);
    }
    
    
    ArrayList<BufferedImage> heroIdle = new ArrayList<BufferedImage>();
    ArrayList<BufferedImage> heroJump = new ArrayList<BufferedImage>();
    ArrayList<BufferedImage> heroDie = new ArrayList<BufferedImage>();
    ArrayList<BufferedImage> enemy1Walk = new ArrayList<BufferedImage>();
    ArrayList<BufferedImage> enemy2Walk = new ArrayList<BufferedImage>();
    for (int i = 0; i < 10; i++) {
      BufferedImage idle = ImageIO.read(new File("femaleCowboy\\Idle (" + (i+1) + ").png"));
      BufferedImage jump = ImageIO.read(new File("femaleCowboy\\Jump (" + (i+1) + ").png"));
      BufferedImage die = ImageIO.read(new File("femaleCowboy\\Dead (" + (i+1) + ").png"));
      BufferedImage walk1 = ImageIO.read(new File("zombie1\\Walk (" + (i+1) + ").png"));
      BufferedImage walk2 = ImageIO.read(new File("zombie2\\Walk (" + (i+1) + ").png"));
      heroIdle.add(idle);
      heroJump.add(jump);
      heroDie.add(die);
      enemy1Walk.add(walk1);
      enemy2Walk.add(walk2);
    }
    
    
    ArrayList<BufferedImage> heroRun = new ArrayList<BufferedImage>();
    for (int i = 0; i < 8; i++) {
      BufferedImage run = ImageIO.read(new File("femaleCowboy\\Run (" + (i+1) + ").png"));
      heroRun.add(run);
    }
    
    ArrayList<BufferedImage> heroShoot = new ArrayList<BufferedImage>();
    for (int i = 0; i < 4; i++) {
      BufferedImage shoot = ImageIO.read(new File("femaleCowboy\\Shoot (" + (i+1) + ").png"));
      heroShoot.add(shoot);
    }
    
    ArrayList<BufferedImage> enemy1Die = new ArrayList<BufferedImage>();
    ArrayList<BufferedImage> enemy2Die = new ArrayList<BufferedImage>();
    for (int i = 0; i < 4; i++) {
      BufferedImage die1 = ImageIO.read(new File("zombie1\\Dead (" + (i+1) + ").png"));
      BufferedImage die2 = ImageIO.read(new File("zombie2\\Dead (" + (i+1) + ").png"));
      enemy1Die.add(die1);
      enemy2Die.add(die2);
    }
    
    ArrayList<ArrayList<BufferedImage>> heroImages = new ArrayList<ArrayList<BufferedImage>>();
    heroImages.add(heroIdle);
    heroImages.add(heroRun);
    heroImages.add(heroJump);
    heroImages.add(heroDie);
    heroImages.add(heroShoot);
    
    ArrayList<ArrayList<BufferedImage>> enemy1Images = new ArrayList<ArrayList<BufferedImage>>();
    enemy1Images.add(enemy1Walk);
    enemy1Images.add(enemy1Die);
    
    ArrayList<ArrayList<BufferedImage>> enemy2Images = new ArrayList<ArrayList<BufferedImage>>();
    enemy2Images.add(enemy2Walk);
    enemy2Images.add(enemy2Die);
    
    Hero hero = new Hero(250, 600, 188, 188, 4.5, heroImages);
    BufferedImage background = ImageIO.read(new File("mapImages\\BG\\BG.png"));
    startGame(hero, blockImages, enemy1Images, enemy2Images, background);
    
  }
  
  

  public static void startGame(Hero hero, ArrayList<BufferedImage> blockImages, ArrayList<ArrayList<BufferedImage>> enemy1Images,  
                               ArrayList<ArrayList<BufferedImage>> enemy2Images, BufferedImage background) 
    throws FileNotFoundException, IOException, InterruptedException, UnsupportedAudioFileException, LineUnavailableException{
    
    ArrayList<Block> blocks = readMaps("map2", blockImages);
    Map map = new Map(blocks);
    map.setEnemies(enemies);
    World w = new World(hero, background, map);
    
    
    JFrame jf = new JFrame();
    jf.setTitle("Platformer");
    jf.setSize(screenLength, screenHeight); 
    
    jf.add(w);
    jf.setVisible(true);
    jf.setResizable(false);
    jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
    
    while (true) { //while the game hasn't been lost
      long origTime = System.currentTimeMillis(); //get initial time
      while (true) { //while the game hasn't been lost   
          long currTime = System.currentTimeMillis(); //get the current time
          if (currTime-origTime >= GAME_LOOP_DELAY) { //if certain amount of time has passed
            while(currTime-origTime >= GAME_LOOP_DELAY){ //while difference between curr time and initial time greater than delay
              w.updateWorld(); //update the world
              origTime += GAME_LOOP_DELAY; //update initial time              
              w.repaint(); //repaint the world
            } //end of inner while loop
          } //end of time difference check
      } //end of game over check
    } //end of main game loop
  }
}
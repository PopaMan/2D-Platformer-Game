//import java.util.ArrayList;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import java.lang.InterruptedException;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.util.ArrayList;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.LineUnavailableException;
public class World extends JPanel implements KeyListener{
  
  private boolean levelOver = false;
  static final long serialVersionUID = 0;
  private BufferedImage background;
  private Map map;
  private Hero hero;
  private  boolean leftMoving = false;
  private boolean rightMoving = false;
  Clip runClip = null;
  static ArrayList<AudioInputStream> audioStreams = new ArrayList<AudioInputStream>();
  static ArrayList<Clip> currSounds = new ArrayList<Clip>();
  static String[] soundNames = {"windSound", "runSound", "jumpSound", "landSound", "deathSound", "shotSound"};
  static int numSounds = 0;
  public World(Hero hero, BufferedImage background, Map map) throws InterruptedException, UnsupportedAudioFileException, IOException, LineUnavailableException {
    this.addKeyListener(this);
    this.setFocusable(true);
    this.levelOver = false;
    this.map = map;
    this.hero = hero;
    this.background = background;
    
    playSound(0);  
    
  } //end of constructor
  
  public boolean getLevelStatus() {
    return this.levelOver;
  }
  
  
  public void paintComponent(Graphics g) {
    g.drawImage(this.background, 0, 0, (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(), (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight(), null); //draw the backlground
    
    for(Block block : this.map.getBlocks()) {
      g.drawImage(block.getImage(), (int)block.getXPos(), (int)block.getYPos(), (int)block.getWidth(), (int)block.getHeight(), null);
    }
    
    BufferedImage heroImage = this.hero.getCurrImage();
    if (this.hero.getState() < 0) {
      AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
      tx.translate(-heroImage.getWidth(null), 0);
      AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
      heroImage = op.filter(heroImage, null);
    }
    g.drawImage(heroImage, (int)this.hero.getXPos(), (int)this.hero.getYPos(), (int)this.hero.width, (int)this.hero.height, null);
    
    for (Enemy enemy : this.map.getEnemies()) {
      BufferedImage enemyImage = enemy.getCurrImage();
      if (enemy.getState() < 0) {
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-enemyImage.getWidth(null), 0);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        enemyImage = op.filter(enemyImage, null);
      }
      g.drawImage(enemyImage, (int)enemy.getXPos(), (int)enemy.getYPos(), (int)enemy.getWidth(), (int)enemy.getHeight(), null);
    }
    
  } // end of paintComponent() 
  
  
  public void updateWorld() throws IOException{ 
    
    ArrayList<Clip> soundList = new ArrayList<Clip>(currSounds);
    for (Clip sound : soundList) { //for each current sound
      if (!sound.isRunning()) { //if this sound is not running
        currSounds.remove(sound);
      } //end of sound running check
    } //end of loop through current sounds
    
    if (Math.abs(hero.getState())==2) {
      if (this.runClip == null || !this.runClip.isRunning()) {
        playSound(1);
      }
    }
    else if (runClip != null) {
      runClip.stop();
    }
    
    //update hero sprite
    hero.setSpriteCount(hero.getSpriteCount()+1); //update enemy's sprite counter  
    double tempSpeed = 1 / this.hero.getSpeed();
    if ((hero.getSpriteCount() / tempSpeed) >= 5) { //if it is time to update the sprite 
      hero.setSpriteIdx(hero.getSpriteIdx()+1);
      hero.setSpriteCount(0);
    }
    this.hero.move();
    
    //check for collision
    double heroXPos = this.hero.getXPos();
    double heroYPos = this.hero.getYPos();
    double heroWidth = this.hero.width;
    double heroHeight = this.hero.height;
    this.hero.bottomBlocked = false;
    this.hero.rightBlocked = false;
    this.hero.leftBlocked = false;
    
    if (this.hero.isInAir()) {
      if (Math.abs(this.hero.getState()) != 4) {
        if (this.map.blockYVel < 0) {
          this.hero.setJumpState(false);
        }
        
        if (this.hero.getState() < 0) {
          this.hero.setState(-3);
        }
        else {
          this.hero.setState(3);
        }
      }
    } //end of falling and jumping state check
    
    boolean touchingSpikes = false;
    for (Block block : this.map.getBlocks()) {
      
      double blockXPos = block.getXPos();
      double blockYPos = block.getYPos();
      double blockWidth = block.getWidth();
      double blockHeight =block.getHeight();
      
      if ((!this.hero.rightBlocked && rightMoving) || (!this.hero.leftBlocked && leftMoving)) {
        if (Math.abs(this.hero.getState()) == 3) {
          this.map.blockXVel = -1.5 * ((Math.abs(hero.getState()) / this.hero.getState())*2)*this.hero.getSpeed();
        }
        else {
          this.map.blockXVel = -1.5 * hero.getState()*this.hero.getSpeed();
        }
      }
      
      
      
      
      
      //CHECK FOR RIGHT COLLISION
      if ((     (heroXPos >= blockXPos-1.2*blockWidth) && (heroXPos < blockXPos)          ) && ((heroYPos + 0.93*hero.height <= blockYPos +blockHeight) && (heroYPos + 0.93*hero.height > blockYPos))) {
        this.hero.rightBlocked = true;
      } //end of right collision check
      
      
      //CHECK FOR LEFT COLLISION
      if ((     (heroXPos - 0.80*heroWidth <= blockXPos-blockWidth) && (heroXPos > blockXPos)          ) && ((heroYPos + 0.93*hero.height <= blockYPos +blockHeight) && (heroYPos + 0.93*hero.height > blockYPos))) {
        this.hero.leftBlocked = true;
        
      } //end of left collision check
      
      //CHECK FOR GROUND
      if ( (heroXPos + 0.60*heroWidth >= blockXPos && heroXPos <= blockXPos+0.30*blockWidth)) {             
        if ((heroYPos + heroHeight - this.map.blockYVel > blockYPos+0.05*blockHeight && heroYPos+ heroHeight < blockYPos + blockHeight)) {
          
          //KILL THE PLAYER
          if (block.getBlockType() == 19) {
            touchingSpikes = true;
            if (Math.abs(hero.getState()) != 4) {
              playSound(4);
              hero.setSpriteIdx(0);
            }
            hero.setState(((Math.abs(hero.getState()) / hero.getState())*4));
          }

          else  if (this.map.blockYVel <= 0 && !hero.isInJump()) {
            this.map.blockYVel = 0;
            double diff = (heroYPos +heroHeight-0.1*blockHeight) - blockYPos;
            for (Block currBlock : this.map.getBlocks()) {
              
              currBlock.setYPos(currBlock.getYPos() + diff);
            } //end of for loop
            this.hero.setAirState(false);
            this.hero.bottomBlocked = true;
            for (Enemy enemy : this.map.getEnemies()) {
              enemy.setYPos(enemy.getYPos() + diff);
            }
            
            
            if (Math.abs(hero.getState()) == 3) {
              if (!rightMoving && !leftMoving) {
                //play landing sound
                playSound(3);
                this.hero.setState((3 / hero.getState()));
              }
              else {
                this.hero.setState((3 / hero.getState())*2);
              }
            } 
          }
        } //end of ground check
      } //end of ground check
      
    } //end of blocks loop
    
    //handle hero collision & gravity
    if (this.hero.rightBlocked) {
      if (this.rightMoving) {
        this.map.blockXVel = 0;
      }
    }
    
    if (this.hero.leftBlocked) {
      if (this.leftMoving) {
        this.map.blockXVel = 0;
      }
    }
    
    if (!this.hero.bottomBlocked) {
      if (Math.abs(this.hero.getState()) != 4) {
        this.hero.setState((Math.abs(this.hero.getState()) / this.hero.getState()) * 3);
      }
      this.map.blockYVel -= this.map.getGravity();
    }
    
    if (Math.abs(this.hero.getState()) == 5) {
      this.map.blockXVel = 0;
    }
    
    //update  blocks
    for(Block block : this.map.getBlocks()) {
      block.setXVel(this.map.blockXVel);
      block.setYVel(this.map.blockYVel);
      if (!touchingSpikes && Math.abs(hero.getState()) == 4) {
        block.setXVel(0);
        for (Block currBlock :  this.map.getBlocks()) {
          currBlock.setYPos(currBlock.getYPos() - 0.04);
        }
        for (Enemy enemy : this.map.getEnemies()) {
          enemy.setYPos(enemy.getYPos() - 0.04);
        }
      }
      block.move();
    } //end of for loop
    
    for (Enemy enemy : this.map.getEnemies()) {

      enemy.setSpriteCount(enemy.getSpriteCount()+1); //update enemy's sprite counter  
      tempSpeed = 1 / Math.abs(enemy.getInitXVel());
      
      if ((enemy.getSpriteCount() / tempSpeed) >= 5) { //if it is time to update the sprite 
       enemy.setSpriteIdx(enemy.getSpriteIdx()+1);
        enemy.setSpriteCount(0);
      }
      enemy.setXPos(enemy.getXPos() + this.map.blockXVel);
      enemy.setYPos(enemy.getYPos() + this.map.blockYVel);
      enemy.move();
      
      double enemyXPos = enemy.getXPos();
      double enemyYPos = enemy.getYPos();
      double enemyWidth = enemy.width;
      double enemyHeight = enemy.height;
      
      enemy.leftBlocked = false;
      enemy.rightBlocked = false;
      enemy.rightBottomBlocked = false;
      enemy.leftBottomBlocked = false;
      
      for (Block block : this.map.getBlocks()) {
        double blockXPos = block.getXPos();
        double blockYPos = block.getYPos();
        double blockWidth = block.getWidth();
        double blockHeight =block.getHeight();
        
        if ((enemyYPos + enemyHeight > blockYPos+0.05*blockHeight && enemyYPos+ enemyHeight < blockYPos + blockHeight)) {
          if (enemyXPos + enemyWidth >= blockXPos && enemyXPos +0.5*enemyWidth <= blockXPos+0.30*blockWidth) {
            enemy.rightBottomBlocked = true;    
          }
          
          if (enemyXPos - 0.01*enemyWidth >= blockXPos && enemyXPos - 0.02*enemyWidth <= blockXPos+blockWidth) {
            enemy.leftBottomBlocked = true;      
          }
        }
        

        
        if (((enemyXPos >= blockXPos-1.2*blockWidth) && (enemyXPos < blockXPos)) && (enemyYPos + 0.93*enemy.height <= blockYPos +blockHeight) && (enemyYPos + 0.93*enemy.height > blockYPos)) {
          enemy.rightBlocked = true;
        } //end of right collision check
        
        if (((enemyXPos - enemyWidth <= blockXPos-blockWidth) && (enemyXPos > blockXPos)) && ((enemyYPos + 0.93*enemy.height <= blockYPos +blockHeight) && (enemyYPos + 0.93*enemy.height > blockYPos))) {
          enemy.leftBlocked = true;     
        } //end of left collision check
                      
      } //end of internal block loop
      
      if (enemy.leftBlocked || !enemy.leftBottomBlocked) {
        enemy.setXPos(enemy.getXPos()+10);
        enemy.setXVel(enemy.getXVel() + 2*enemy.getInitXVel());
        enemy.setState(enemy.getState() * -1);
      }
      if (enemy.rightBlocked || !enemy.rightBottomBlocked) {
        enemy.setXPos(enemy.getXPos()-10);
        enemy.setXVel(enemy.getXVel() - 2*enemy.getInitXVel());
        enemy.setState(enemy.getState() * -1);
      }

    } //end of enemy loop
    
    
  } //end of updateWorld()
  
  @Override
  public void keyTyped(KeyEvent e) {
    
  } //end of keyTyped()
  
  @Override
  public void keyPressed(KeyEvent e) {
    if (Math.abs(this.hero.getState()) != 4) {
      
      if(e.getKeyCode()==KeyEvent.VK_SPACE && this.hero.bottomBlocked && Math.abs(this.hero.getState()) != 5 && !this.hero.shotHold) {
        playSound(5);
        this.hero.shotHold = true;
        this.hero.tempState = this.hero.getState();
        this.hero.setState(Math.abs(this.hero.getState()) / this.hero.getState() * 5);  
        hero.setSpriteIdx(0);
      }
      
      
      if (e.getKeyChar() == 'w') {         
        if (this.hero.bottomBlocked && !this.hero.isInAir() && !this.hero.jumpHold) {
          playSound(2);
          this.hero.setAirState(true);
          this.hero.jumpHold = true;
          this.hero.setJumpState(true);
          this.map.blockYVel = 20;
          this.hero.bottomBlocked = false;
          for (Block block : this.map.getBlocks()) {
            block.setYPos(block.getYPos()+25);
          }
          for (Enemy enemy : this.map.getEnemies()) {
            enemy.setYPos(enemy.getYPos() + 25);
          }
        } //end of air check
      } //end of jump check
      
      if (e.getKeyChar() == 'a') {
        //update block velocities
        if (!this.leftMoving && Math.abs(this.hero.getState()) != 4) {
          this.leftMoving = true;
          this.hero.setState(-2);
          if (!this.hero.leftBlocked) {
            this.hero.rightBlocked = false;
            this.map.blockXVel = -1.5 * hero.getState()*this.hero.getSpeed();
          }
        } //end of left moving check
      } //end of left check
      
      if (e.getKeyChar() == 'd') {
        //update block velocities
        if (!this.rightMoving && Math.abs(this.hero.getState()) != 4) {
          this.rightMoving = true;
          this.hero.setState(2);
          if (!this.hero.rightBlocked) {
            this.hero.leftBlocked = false;
            this.map.blockXVel = -1.5 * hero.getState()*this.hero.getSpeed();
          }
        }
      } //end of right check
    } //end of dead check
  } //end of keyPressed()
  
  @Override
  public void keyReleased(KeyEvent e) {
    
    if (Math.abs(this.hero.getState()) != 4) {
      
      if(e.getKeyCode()==KeyEvent.VK_SPACE) {
        this.hero.shotHold = false;
      }
      
      if (e.getKeyChar() == 'w') {
        this.hero.jumpHold = false;
      }
      
      if (e.getKeyChar() == 'a') {
        //update block velocities
        this.leftMoving = false;
        if (!this.rightMoving) {
          this.hero.setState(-1);
          this.map.blockXVel = 0;
        }
        else {
          this.hero.setState(2);
          this.rightMoving = true;
          this.map.blockXVel = -1.5*this.hero.getState()*this.hero.getSpeed();
        }
      } //end of left check
      
      if (e.getKeyChar() == 'd') {
        this.rightMoving = false;
        if (!this.leftMoving) {
          this.hero.setState(1);
          this.map.blockXVel = 0;
        }
        else {
          this.hero.setState(-2);
          this.leftMoving = true;
          this.map.blockXVel = -1.5*this.hero.getState()*this.hero.getSpeed();
        }
      } //end of right check
    } //end of dead check
    
    
  } //end of keyReleased()
  
  public void playSound(int idx) {
    try {
      if (currSounds.size() <= 20) { //if less than 20 sounds currently playing
        //create new sound according to index provided
        File audioFile = new File("Sound\\" + soundNames[idx] + ".wav");
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
        DataLine.Info info = new DataLine.Info(Clip.class, audioStream.getFormat());
        Clip clip = (Clip) AudioSystem.getLine(info);
        currSounds.add(clip);
        
        if (soundNames[idx].equals("runSound")) { //if run sound is to be played
          if (runClip == null || !runClip.isRunning()) { //if no run sound playing at the moment,
            runClip = clip;
            runClip.setFramePosition(0);
          } //end of no running fire sound check
          else { //otherwise
            return; //return, and do not create a new firesound
          } //end of existing fire sound check
        } //end of run sound check
        
        clip.addLineListener(new MyLineListener());
        clip.open(audioStream);
        clip.start();
        numSounds++;
      } //end of numSounds check
    }catch (Exception e) {
      e.printStackTrace();
    } //end of catch
  } //end of playSound()
} //end of World

/**
 * MyLineListener.java by Sam Reznikov
 * January 19, 2019
 * Used to keep active sounds in check and close them if they stop running
 */

//import necessary libraries
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineEvent;
public class MyLineListener implements LineListener{
  
  /**
   * update()
   * Informs the listener that a line's state has changed.
   * @param a line event that describes the change
   */
  @Override
  public void update(LineEvent event) { 
        if (event.getType() == LineEvent.Type.STOP) {
          event.getLine().close(); 
        } //end of if statement
  } //end of update()
} //end of MyLineListener

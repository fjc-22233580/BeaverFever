import greenfoot.Actor;
import greenfoot.GreenfootImage;

/**
 * The PrincessOlive class represents a princess character in the game..
 */
public class PrincessOlive extends Actor {

    /**
     * Constructs a new PrincessOlive object and sets the initial image to a sad princess.
     */
    public PrincessOlive() {
        GreenfootImage sadOlive = new GreenfootImage("sad_princess.png");
        setImage(sadOlive);
    }

    /**
     * Sets the image of the princess to a happy princess.
     */
    public void setKeyCollected() {
        GreenfootImage happyOlive = new GreenfootImage("happy_princess.png");
        setImage(happyOlive);
    }    
}

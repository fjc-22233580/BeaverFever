import greenfoot.Actor;
import greenfoot.GreenfootImage;

public class PrincessOlive extends Actor {


    public PrincessOlive() {
        GreenfootImage image = new GreenfootImage("Happyprincessv2.png");
        setImage(image);
    }

    public void winGame(){
        GreenfootImage image = new GreenfootImage("sadprincessv2.png");
        setImage(image);
    }

    public void act() {
        
    }
    
}

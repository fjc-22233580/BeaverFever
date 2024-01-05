import greenfoot.Actor;
import greenfoot.GreenfootImage;

public class PrincessOlive extends Actor {


    public PrincessOlive() {
        GreenfootImage sadOlive = new GreenfootImage("sad_princess.png");
        setImage(sadOlive);
    }
    
    public void winGame(){
        GreenfootImage happyOlive = new GreenfootImage("happy_princess.png");
        setImage(happyOlive);
    }    
}

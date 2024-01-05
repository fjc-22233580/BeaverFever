// (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import greenfoot.Actor;
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;

/**
 * Write a description of class Button here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Button extends Actor
{
    public Button(String imtPath) {
        GreenfootImage startbutton = new GreenfootImage(imtPath);
        setImage(startbutton);
    }
    /**
     * Act - do whatever the Button wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        if (Greenfoot.mouseClicked(this)) {
            WorldManager manager = WorldManager.getInstance();

            //manager.setDevMode();

            manager.initialize();
            manager.beginGame();
        }
    }
}

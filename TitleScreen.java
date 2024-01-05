// (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import greenfoot.GreenfootImage;
import greenfoot.World;

/**
 * Write a description of class TitleScreen here.
 * 
 * @author (your name)
 * @version (a version number or a date)
 */
public class TitleScreen extends World {

    /**
     * Constructor for objects of class TitleScreen.
     * 
     */
    public TitleScreen() {

        super(240, 240, 1);

        GreenfootImage titlecard = new GreenfootImage("title card v3.png");
        setBackground(titlecard);

        Button button = new Button("Start.png");
        addObject(button, 120, 200);
    }
}

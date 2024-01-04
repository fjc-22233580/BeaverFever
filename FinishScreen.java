import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class FinishScreen here.
 * 
 * @author (your name)
 * @version (a version number or a date)
 */
public class FinishScreen extends World {

    /**
     * Constructor for objects of class FinishScreen.
     * 
     */
    public FinishScreen(Boolean win) {
        super(240, 240, 1);


        if (win) {
            GreenfootImage winimage = new GreenfootImage("game complete page.png");
            setBackground(winimage);

        } else {
            GreenfootImage loseimage = new GreenfootImage("Game over screen.png");
            setBackground(loseimage);
        }
    
        // TODO - Add a button to restart the game
    }
}

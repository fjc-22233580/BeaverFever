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
            Label label = new Label("YOU WIN!", 32);
            addObject(label, 120, 120);
        } else {
            Label label = new Label("YOU LOSE!", 32);
            addObject(label, 120, 120);
        }
    
        // TODO - Add a button to restart the game
    }
}

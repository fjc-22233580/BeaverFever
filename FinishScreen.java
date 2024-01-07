import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The FinishScreen class represents the screen displayed at the end of the game.
 */
public class FinishScreen extends World {

    /**
     * Constructor for objects of class FinishScreen.      * 
     * @param win a boolean value indicating whether the player won or lost the game
     */
    public FinishScreen(Boolean win) {
        super(240, 240, 1);

        // Display the appropriate image based on the win parameter
        if (win) {
            GreenfootImage winImage = new GreenfootImage("game complete page.png");
            setBackground(winImage);

        } else {
            GreenfootImage loseImage = new GreenfootImage("Game over screen.png");
            setBackground(loseImage);
        }
    }
}

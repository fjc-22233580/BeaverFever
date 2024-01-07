import greenfoot.Actor;
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;
/**
 * The Button class represents a clickable button in the game.
 * This class creates the button on the title screen and also initializes the game when clicked.
 */
public class Button extends Actor
{
    /**
     * Constructs a Button object with the specified image path.
     * 
     * @param imagePath the path to the image used for the button
     */
    public Button(String imagePath) {
        GreenfootImage startButton = new GreenfootImage(imagePath);
        setImage(startButton);
    }
    
    /**
     * Performs the button's action when clicked.
     * This method is called whenever the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        if (Greenfoot.mouseClicked(this)) {
            WorldManager manager = WorldManager.getInstance();

            // Uncomment the following line to enable the debug mode.
            //manager.setDevMode();

            manager.initialize();
            manager.beginGame();
        }
    }
}

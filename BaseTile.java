import greenfoot.*;
/**
 * The BaseTile class represents a tile in the game.
 * It is an abstract class that extends the Actor class,
 * used so that all different tiles can share functionality,
 * and can be created by a factory.
 */
public abstract class BaseTile extends Actor{    

    private ActorType type;

    /**
     * Constructs a BaseTile object with the specified tile path and actor type.
     * 
     * @param tilePath the path to the tile image
     * @param type the type of the actor
     */
    public BaseTile(String tilePath, ActorType type) {

        this.type = type;
        GreenfootImage tile = new GreenfootImage(tilePath);
        setImage(tile);
    }

    /**
     * This method is called when the act button in the environment is pressed.
     * Subclasses must implement this method to define the behavior of the tile.
     */
    public abstract void act();
}

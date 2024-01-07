/**
 * Represents a fence tile in the game.
 * Extends the BaseTile class.
 */
public class FenceTile extends BaseTile {

    /**
     * Constructs a FenceTile object with the specified tile path and actor type.
     * 
     * @param tilePath the path to the tile image
     * @param type the type of actor associated with the tile
     */
    public FenceTile(String tilePath, ActorType type) {
        super(tilePath, type);
    }
    
    /**
     * Performs the action associated with the fence tile.
     */
    public void act() {
    }
    
}

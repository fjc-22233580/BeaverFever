/**
 * Represents a walkway tile in the game.
 * Extends the BaseTile class.
 */
public class WalkWay extends BaseTile {

    /**
     * Constructs a WalkWay object with the specified tile path and actor type. *
     * 
     * @param tilePath the path to the image file for the tile
     * @param type     the type of actor associated with the tile
     */
    public WalkWay(String tilePath, ActorType type) {
        super(tilePath, type);
    }

    public void act() {
    }

}

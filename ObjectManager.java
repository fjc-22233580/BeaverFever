import java.awt.Point;
import java.util.List;
import greenfoot.*;

/**
 * The ObjectManager class is responsible for managing objects in the game world.
 * It provides methods to set a new world, remove objects, and replace tiles.
 */
public class ObjectManager {
    
    /**
     * The current world in which the beaver is navigating,
     * used to remove objects and replace objects.
     */
    private World currentWorld;

    //#region Cached objects - currently unused.
    private List<? extends Actor> currentObjects;    
    private Actor currentObject;
    //#endregion

    /**
     * Updates the current world reference to the given world.
     * Called by the beaver whilst navigating. 
     * 
     * @param world the new world to set
     */
    public void setNewWorld(World world){
        this.currentWorld = world;        
    }

    /**
     * Removes a list of objects from the current world.
     * 
     * @param objects the list of objects to remove
     */
    public void removeObjects(List<? extends Actor> objects) {        
        this.currentObjects = objects;

        for (Actor object : objects) {
            currentWorld.removeObject(object);
        }
    }

    /**
     * Removes an object from the current world.
     * 
     * @param object the object to remove
     */
    public void removeObject(Actor object) {
        this.currentObject = object;
        currentWorld.removeObject(object);
    }

    /**
     * Replaces an original tile with a new tile in the current world.
     * 
     * @param originalTile the original tile to be replaced
     * @param newTile the new tile to replace with
     */
    public void replaceTile(Actor originalTile, Actor newTile) {
        Point originalTileLocation = new Point(originalTile.getX(), originalTile.getY());
        currentWorld.removeObject(originalTile);
        currentWorld.addObject(newTile, originalTileLocation.x, originalTileLocation.y);
    }

}

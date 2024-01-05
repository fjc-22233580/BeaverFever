import java.awt.Point;
import java.util.List;

import greenfoot.*;

public class ObjectManager {
    
    private World currentWorld;
    private List<? extends Actor> currentObjects;
    private Actor currentObject;

    public ObjectManager() {

    }

    public void setNewWorld(World world){
        this.currentWorld = world;        
    }

    public void removeObjects(List<? extends Actor> objects) {        
        this.currentObjects = objects;

        for (Actor object : objects) {
            currentWorld.removeObject(object);
        }
    }

    public void removeObject(Actor object) {
        this.currentObject = object;
        currentWorld.removeObject(object);
    }

    // TODO - Implement this method
    public void replaceTile(Actor originalTile, Actor newTile) {
        // TODO - Implement this method

        Point originalTileLocation = new Point(originalTile.getX(), originalTile.getY());
        currentWorld.removeObject(originalTile);
        currentWorld.addObject(newTile, originalTileLocation.x, originalTileLocation.y);
        
    }

}

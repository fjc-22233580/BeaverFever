import java.util.List;

import greenfoot.*;

public class ObjectManager {
    
    private World currentWorld;
    private List<Actor> currentObjects;
    private Actor currentObject;

    public ObjectManager() {

    }

    public void transitionNewWorld(World world){
        this.currentWorld = world;        
    }

    public void removeObjects(List<Actor> objects) {        
        this.currentObjects = objects;

        for (Actor object : objects) {
            removeObject(object);
        }
    }

    public void removeObject(Actor object) {
        this.currentObject = object;
        currentWorld.removeObject(object);
    }

}

import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class ObjectTile here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ObjectTile extends Actor
{

    public ObjectTile(String tilePath) {
        
        GreenfootImage tile = new GreenfootImage(tilePath);
        setImage(tile);
    }
    /**
     * Act - do whatever the ObjectTile wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        // Add your action code here.
    }
}
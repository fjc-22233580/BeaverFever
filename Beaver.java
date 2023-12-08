import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Beaver here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Beaver extends Actor
{

    final int VELOCITY = 3;

    /**
     * Act - do whatever the Beaver wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() {
        // Up
        if (Greenfoot.isKeyDown("w")) {

            customSetLocation(getX(), getY() - VELOCITY);
        }

        // Left
        if (Greenfoot.isKeyDown("a")) {

            customSetLocation(getX() - VELOCITY, getY());
        }

        // Down
        if (Greenfoot.isKeyDown("s")) {

            customSetLocation(getX(), getY() + VELOCITY);
        }

        // Right
        if (Greenfoot.isKeyDown("d")) {

            customSetLocation(getX() + VELOCITY, getY());
        }
    }

    public boolean customSetLocation(final int x, final int y) {
        final int oldX = getX();
        final int oldY = getY();
        setLocation(x, y);
        if (isTouching(ObjectTile.class)) {

            // Collided with an object, so revert to previous position.
            setLocation(oldX, oldY);
            return false; // move was not allowed!
        }
        return true; // move was allowed!
    }
}

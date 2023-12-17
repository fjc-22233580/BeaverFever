import java.util.List;

import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Beaver here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Beaver extends Actor
{
    private World homeWorld;
    private WorldManager worldManager = WorldManager.getInstance();

    final int VELOCITY = 3;
    private ObjectManager objectManager;
    private PlayerStats playerStats;

    public Beaver(ObjectManager objectManager, PlayerStats playerStats) {
        this.objectManager = objectManager;
        this.playerStats = playerStats;

    }

    @Override
    protected void addedToWorld(World world) {
        // TODO Auto-generated method stub
        super.addedToWorld(world);

        homeWorld = world;

        objectManager.transitionNewWorld(world);

        System.out.println("Player added to different world.");

    }

    private boolean choppingKeyDown = false;
    private boolean isCollectingWood = false;
    private int counter = 0;
    private List<WoodTile> currentTree;

    /**
     * Act - do whatever the Beaver wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() {

        if(isCollectingWood) {
            System.out.println("Collecting wood.");
            
            if (counter > 120) {
                // TODO - Add chopping mechanic
                System.out.println("Finished Collecting.");
                objectManager.removeObjects(currentTree);
                playerStats.addWood();
                isCollectingWood = false;
                counter = 0;
            }

            counter++;

        } else {

            handleMovement();
    
            if (choppingKeyDown != Greenfoot.isKeyDown("p")) {
                
                choppingKeyDown = !choppingKeyDown;
                
                if(choppingKeyDown) {
                    
                    System.out.println("Looking for tree");
                    
                    List<WoodTile> currentWood = getObjectsInRange(25, WoodTile.class);
                    if(currentWood.size() > 0) {

                        System.out.println("Found some wood!");

                        currentTree = getObjectsInRange(50, WoodTile.class);
                        if (currentTree.size() > 0) {
                            System.out.println("Found a tree! : " + currentTree.size());
                            isCollectingWood = true;
                        }
                    }
                }
            }
        }
    }

    private void handleMovement() {
        // Up
        if (Greenfoot.isKeyDown("w")) {

            customSetLocation(getX(), getY() - VELOCITY);

            if(getY() < 0){

                worldManager.changeWorld(Direction.Up, getX(), getY(), getWorld(), this);
            }
        }

        // Left
        if (Greenfoot.isKeyDown("a")) {

            customSetLocation(getX() - VELOCITY, getY());

            if(getX() < 0){

                worldManager.changeWorld(Direction.Left, getX(), getY(), getWorld(), this);
            }
        }

        // Down
        if (Greenfoot.isKeyDown("s")) {

            customSetLocation(getX(), getY() + VELOCITY);

            if (getY() > homeWorld.getHeight()) {
                
                worldManager.changeWorld(Direction.Down, getX(), getY(), getWorld(), this);
            }
        }

        // Right
        if (Greenfoot.isKeyDown("d")) {

            customSetLocation(getX() + VELOCITY, getY());

            if (getX() > homeWorld.getWidth()) {
                worldManager.changeWorld(Direction.Right, getX(), getY(), getWorld(), this);                
            }
        }
    }

    public boolean customSetLocation(final int x, final int y) {
        final int oldX = getX();
        final int oldY = getY();
        setLocation(x, y);

        if (isTouching(ObjectTile.class) || isTouching(WoodTile.class) || isTouching(WaterTile.class)) {

            // Collided with an object, so revert to previous position.
            setLocation(oldX, oldY);
            return false; // move was not allowed!
        }

        return true; // move was allowed!
    }
}

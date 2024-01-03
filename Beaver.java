import java.awt.Point;
import java.awt.Rectangle;
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
    /**
     * The velocity of the beaver.
     */
    private final int VELOCITY = 3;

    /**
     * The maximum time it takes to chop wood - expressed as game cycles per second.
     */
    private final int MAX_CHOPPING_TIME = 120;
    
    /**
     * Reference to the current world - used to determine when cross the edge of the world. 
     */
    private GameMap currentWorld;
    
    /**
     * Reference to the world manager - used to to call the changeWorld method.
     */
    private WorldManager worldManager = WorldManager.getInstance();
    
    /**
     * Reference to the object manager - used to cache and remove objects from the world.
     */
    private ObjectManager objectManager;
    
    /**
     * Reference to the player stats - used to add wood and health.
     */
    private PlayerStats playerStats;
    
    /**
     * The current state of the beaver, can be one of the following:
     * MOVING, CHOPPING, SEARCHING_FOR_WOOD, RX_DAMAGE, RX_HEALTH
     */
    private BeaverState currentState;    
    
    /**
     * Counter to keep track of the time spent chopping wood.
     */
    private int woodChoppingTimeCounter = 0;
    
    /**
     * List of wood tiles that make up the current tree.
     */
    private List<WoodTile> currentTree;

    /**
     * Flag to determine if the chopping key is down - used to make sure the key is only pressed once.
     */
    private boolean choppingKeyDown = false;

    /**
     * Flag to determine if the building key is down - used to make sure the key is only pressed once.
     */
    private boolean buildingKeyDown = false;
    
    public Beaver(ObjectManager objectManager, PlayerStats playerStats) {
        this.objectManager = objectManager;
        this.playerStats = playerStats;
        
        // Set default state for beaver.
        currentState = BeaverState.MOVING; 
    }
    
    /**
     * Called when the object is added to the world.
     * Overrides the addedToWorld method from the superclass.     * 
     * @param world the world to which the object is added
     */
    @Override
    protected void addedToWorld(World world) {
        // TODO Auto-generated method stub
        super.addedToWorld(world);
        
        currentWorld = (GameMap)world;
        
        objectManager.setNewWorld(world);
        
        System.out.println("Player added to different world.");
        
    }

    /**
     * Sets the state of the beaver based on whether it is being attacked or not - this is called from the Enemy class.
     * If the beaver is being attacked, the state is set to RX_DAMAGE.
     * If the beaver is not being attacked, the state is set to MOVING.
     *
     * @param isBeingAttacked true if the beaver is being attacked, false otherwise
     */
    public void setBeingAttacked(boolean isBeingAttacked) {

        if (isBeingAttacked) {
            currentState = BeaverState.RX_ATTACK;        
        } else {
            currentState = BeaverState.RX_DAMAGE;            
        }
    }

    /**
     * Act - do whatever the Beaver wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() {

        // Carry out various actions based on the current state.
        switch (currentState) {
            case MOVING:
                handleMovement();
                break;
            case SEARCHING_FOR_WOOD:
                searchForWood();
                break;
            case CHOPPING:
                chopping();
                break;
            case RX_ATTACK:
                recieveAttack();
                break;
            case RX_DAMAGE:
                receiveDamage();
                break;
            case RX_HEALTH:
                collectHealth();
                break;
            case RX_KEY:
                collectKey();
                break;
            case BRIDGE_BUILDING:
                buildBridge();
                break;
            default:
                break;
        }
    }

    
    private void buildBridge() {
        // TODO - Add logic to build bridge
        // TODO - Rename world getter to something more appropriate.
        
        if (currentWorld.getHasBridge()) {

            
            Rectangle constructionBoundBox = new Rectangle(getX(), getY(), getImage().getWidth(), getImage().getHeight());
            if (constructionBoundBox.contains(currentWorld.getConstructionLocation())) {
                
                Actor waterTile = getOneObjectAtOffset(0, 16, WaterTile.class);
                if (waterTile != null) {
                    
                    System.out.println("Found water tile!");
                    
                    Point bridgeLocation = new Point(waterTile.getX(), waterTile.getY());
                    objectManager.removeObject(waterTile);
                    objectManager.addBridgeTile(bridgeLocation);
                }
            }            
        }
    }
    
    private void collectKey() {
        
        Actor key = getOneIntersectingObject(Key.class);
        if (key != null) {
            objectManager.removeObject(key);
            playerStats.collectKey();
            System.out.println("Key collected!");
        }
        
        currentState = BeaverState.MOVING;
    }
    
    private void chopping() {
        
        // TODO - Add chopping gif to start here and end once inside below if statement.
        System.out.println("Collecting wood.");
        
        if (woodChoppingTimeCounter > MAX_CHOPPING_TIME) {
            
            System.out.println("Finished Collecting.");
            
            // Remove selected wood tiles from the world.
            objectManager.removeObjects(currentTree);
            
            // Add wood to the player stats.
            playerStats.addWood();
            
            // Reset the state and time.
            currentState = BeaverState.MOVING;
            woodChoppingTimeCounter = 0;
        }
        
        woodChoppingTimeCounter++;
    }
    
    
    private void collectHealth() {
        
        if (playerStats.canAddLife()) {
            System.out.println("Health added!");
            Actor healthTiles = getOneIntersectingObject(BerryTile.class);
            objectManager.removeObject(healthTiles);
        }
        
        currentState = BeaverState.MOVING;
    }

    private void recieveAttack() {
        // Any code needed whilst being attacked, currently just to 
    }
    
    private void receiveDamage() {
        
        if (playerStats.decreaseLife()) {
            System.out.println("You died!");
            worldManager.loseGame();
        } else {
            System.out.println("Health decreased!");
        }
        currentState = BeaverState.MOVING;
    }
    
    
    private void handleMovement() {
        
        if (buildingKeyDown != Greenfoot.isKeyDown("b")) {
            
            buildingKeyDown = !buildingKeyDown;
            
            if (buildingKeyDown) {
                currentState = BeaverState.BRIDGE_BUILDING;
            }
        }
        
        if (isTouching(Key.class)) {
            System.out.println("Key found!");
            currentState = BeaverState.RX_KEY;
        }

        if (choppingKeyDown != Greenfoot.isKeyDown("p")) {

            choppingKeyDown = !choppingKeyDown;

            if (choppingKeyDown) {
                currentState = BeaverState.SEARCHING_FOR_WOOD;
            }
        }

        if (isTouching(BerryTile.class)) {

            if (playerStats.getLivesCount() < playerStats.getMAX_LIVES()) {                
                currentState = BeaverState.RX_HEALTH;            
            }
        }

        // Up
        if (Greenfoot.isKeyDown("w")) {

            collisionSetLocation(getX(), getY() - VELOCITY);

            if(getY() < 0){
                worldManager.changeWorld(Direction.Up, getX(), getY(), getWorld(), this);
            }
        }

        // Left
        if (Greenfoot.isKeyDown("a")) {

            collisionSetLocation(getX() - VELOCITY, getY());

            if(getX() < 0){
                worldManager.changeWorld(Direction.Left, getX(), getY(), getWorld(), this);
            }
        }

        // Down
        if (Greenfoot.isKeyDown("s")) {

            collisionSetLocation(getX(), getY() + VELOCITY);

            if (getY() > currentWorld.getHeight()) {                
                worldManager.changeWorld(Direction.Down, getX(), getY(), getWorld(), this);
            }
        }

        // Right
        if (Greenfoot.isKeyDown("d")) {

            collisionSetLocation(getX() + VELOCITY, getY());

            if (getX() > currentWorld.getWidth()) {
                worldManager.changeWorld(Direction.Right, getX(), getY(), getWorld(), this);                
            }
        }
    }

    private void searchForWood() {
        System.out.println("Looking for tree");

        // Look for a wood tile
        List<WoodTile> currentWood = getObjectsInRange(25, WoodTile.class);
        if (currentWood.size() > 0) {

            System.out.println("Found some wood!");

            // Now make sure we "get" the whole tree, or,
            // all wood type tiles that are touching each other.
            currentTree = getObjectsInRange(50, WoodTile.class);
            if (currentTree.size() > 0) {
                System.out.println("Found a tree! : " + currentTree.size());

                currentState = BeaverState.CHOPPING;
            }
        }
    }

    private void collisionSetLocation(final int x, final int y) {
        final int oldX = getX();
        final int oldY = getY();
        setLocation(x, y);

        // In case we collie with any of the following objects, revert to the previous position.
        if (isTouching(ObjectTile.class) || isTouching(WoodTile.class) || isTouching(WaterTile.class) || isTouching(Enemy.class)) {

            // Collided with an object, so revert to previous position.
            setLocation(oldX, oldY);
        }
    }
}

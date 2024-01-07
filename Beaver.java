import java.util.List;
import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The Beaver class represents a beaver character in the game.
 * The beaver can move, chop wood, collect items, and interact with the game world.
 * It has various states that determine its behavior.
 */
public class Beaver extends Actor
{   
    /**
     * The velocity of the beaver.
     */
    private final int VELOCITY = 3;

    /**
     * The radius to search for wood tiles around the beaver.
     */
    private final int WOOD_SEARCH_RADIUS = 25;

    /**
     * The radius to search for the whole tree once we've found a wood tile.
     */
    private final int TREE_SEARCH_RADIUS = 50;

    /**
     * The maximum time it takes to chop wood - expressed as game cycles per second.
     */
    private final int MAX_CHOPPING_TIME = 90;

    /**
     * The minimum wood count required to build a bridge portion.     
     */
    private final int MIN_WOODCOUNT = 1;
    
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

    /**
     * The chopping gif for the Beaver.
     */
    private GifImage choppingGif = new GifImage("beaver_chop.gif");;
    
    /**
     * The default image for the Beaver.
     */
    private GreenfootImage defaultImage = new GreenfootImage("nugget_beaver.png");;

    /**
     * Represents a beaver in the game.
     * The beaver can move and has a current state. 
     * @param objectManager the object manager for the game
     * @param playerStats the player's statistics
     */
    public Beaver(ObjectManager objectManager, PlayerStats playerStats) {
        this.objectManager = objectManager;
        this.playerStats = playerStats;
        
       setDefaultState();
    }
    
    /**
     * Called when the object is added to the world.
     * Overrides the addedToWorld method from the superclass.   
     * @param world the world to which the object is added
     */
    @Override
    protected void addedToWorld(World world) {        
        super.addedToWorld(world);
        
        // Cache the current world.
        currentWorld = (GameMap)world;   
        
        // Set the object manager's world to the current world.
        objectManager.setNewWorld(world);        
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

        handleNonMovementInput();

        // Carry out various actions based on the current state.
        switch (currentState) {
            case MOVING:
                handleMovement();
                break;
            case SEARCHING_FOR_WOOD:
                searchForWood();
                break;
            case WOOD_CHOPPING:
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
                case RESCUING_PRINCESS:
                worldManager.winGame();
                break;
            default:
                break;
        }
    }

    
    /**
     * Builds a bridge if the current map has a river.
     * The bridge is constructed by removing a water tile and adding a bridge tile at its place.
     * The player must in the desired location to build the bridge, then this algorithm will find the water tile adjacent to the player.
     */
    private void buildBridge() {

        final int tileSize = 16;

        if (isTouching(WalkWay.class)) {

            if(playerStats.getWoodCount() >= MIN_WOODCOUNT) {

                // Try to find either the water tile above or below the walkway tile.
                Actor waterTile = getOneObjectAtOffset(0, tileSize, WaterTile.class);
                if (waterTile == null) {
                    waterTile = getOneObjectAtOffset(0, -tileSize, WaterTile.class);    
                } 

                // If we found a water tile, replace it with a bridge tile.
                if (waterTile != null) {                    
                    objectManager.replaceTile(waterTile, new BridgeTile());
                    playerStats.removeWood(MIN_WOODCOUNT);
                }

            }
        }
        currentState = BeaverState.MOVING;
    }
    
    /**
     * Collects the key if the player intersects with it,
     * sets the player stats to having the key,
     * then removes the key from the world. 
     */
    private void collectKey() {
        
        Actor key = getOneIntersectingObject(Key.class);
        if (key != null) {
            objectManager.removeObject(key);
            playerStats.collectKey();
            worldManager.setKeyCollected();
        }        
        currentState = BeaverState.MOVING;
    }
    
    /**
     * Performs the chopping action, collecting wood from trees.
     * If the chopping time counter exceeds the maximum chopping time, 
     * then we remove the selected wood tiles from the world and adds wood to the player stats.
     */
    private void chopping() {
        
        woodChoppingTimeCounter++;
        setImage(choppingGif.getCurrentImage());
        
        if (woodChoppingTimeCounter > MAX_CHOPPING_TIME) {
            
            // Remove selected wood tiles from the world.
            objectManager.removeObjects(currentTree);
            
            // Add wood to the player stats.
            playerStats.addWood();
            
            // Reset the state and time.
            setImage(defaultImage);
            currentState = BeaverState.MOVING;
            woodChoppingTimeCounter = 0;
        }        
    }    
    
    /**
     * Collects health if the player's stats allow it.
     * removes the intersecting BerryTile object from the object manager,
     * and sets the current state to BeaverState.MOVING.
     */
    private void collectHealth() {
        
        if (playerStats.canAddLife()) {
            Actor healthTiles = getOneIntersectingObject(BerryTile.class);
            objectManager.removeObject(healthTiles);
        }        
        currentState = BeaverState.MOVING;
    }

    private void recieveAttack() {
        // Any code needed whilst being attacked. 
    }
    
    /**
     * Decreases the player's life and updates the game state accordingly.
     * If the player's life reaches zero, we transition to the game over screen.
     * Otherwise, the player's health is decreased.
     */
    private void receiveDamage() {
        
        if (playerStats.decreaseLife()) {
            worldManager.loseGame();
        } 
        currentState = BeaverState.MOVING;
    }    
    
    /**
     * Handles the movement of the Beaver object based on user input.
     * The Beaver can move up, down, left, or right using the 'w', 's', 'a', and 'd' keys respectively.
     * If the Beaver reaches the edge of the current world, it will change to the adjacent world in the corresponding direction.
     */
    private void handleMovement() {   

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

    /**
     * Handles non-movement input for the beaver character.
     * Checks for key, berry tile, building, and chopping inputs
     * and updates the current state accordingly.
     */
    private void handleNonMovementInput() {

        if (isTouching(PrincessOlive.class) && playerStats.hasKey()) {
            currentState = BeaverState.RESCUING_PRINCESS;
        }

        if (isTouching(Key.class)) {
            currentState = BeaverState.RX_KEY;
        }

        if (isTouching(BerryTile.class)) {

            if (playerStats.getLivesCount() < playerStats.getMAX_LIVES()) {                
                currentState = BeaverState.RX_HEALTH;            
            }
        }

        if (buildingKeyDown != Greenfoot.isKeyDown("b")) {  
            handleKeyDownEvent(buildingKeyDown, BeaverState.BRIDGE_BUILDING);
        }        

        if (choppingKeyDown != Greenfoot.isKeyDown("p")) {
            handleKeyDownEvent(choppingKeyDown, BeaverState.SEARCHING_FOR_WOOD);
        }
    }

    /**
     * Handles the key down event for the beaver.
     * 
     * @param isKeyDown the current state of the key (true if key is down, false if key is up)
     * @param state the new state to set for the beaver
     */
    private void handleKeyDownEvent(boolean isKeyDown, BeaverState state) {
        isKeyDown = !isKeyDown;
        if (isKeyDown) {
            currentState = state;
        }        
    }

    /**
     * Searches for a wood tile within a certain radius.
     * If a wood tile is found, it checks if there is a whole tree (all wood type tiles touching each other).
     * If a tree is found, the beaver's state is set to chopping.
     * If no wood tile is found, the beaver's state is set to moving.
     */
    private void searchForWood() {        
       
        // Look for a wood tile
        List<WoodTile> currentWood = getObjectsInRange(WOOD_SEARCH_RADIUS, WoodTile.class);
        if (currentWood.size() > 0) {

            // Now make sure we "get" the whole tree, or,
            // all wood type tiles that are touching each other.
            currentTree = getObjectsInRange(TREE_SEARCH_RADIUS, WoodTile.class);
            if (currentTree.size() > 0) {
                currentState = BeaverState.WOOD_CHOPPING;
            }
        } else { currentState = BeaverState.MOVING; }
    }
    
    /**
     * Caches the current locations beffore checking for collisions with other objects.
     * If a collision occurs, the beaver's location is reverted to the cached position.
     * @param x the x-coordinate of the new location
     * @param y the y-coordinate of the new location
     */
    private void collisionSetLocation(final int x, final int y) {
        final int oldX = getX();
        final int oldY = getY();
        setLocation(x, y);

        // In case we collie with any of the following objects, revert to the previous position.
        if (isTouching(ObjectTile.class) || isTouching(WoodTile.class) || isTouching(WaterTile.class)
                || isTouching(Enemy.class) || isTouching(FenceTile.class)) {

            // Collided with an object, so revert to previous position.
            setLocation(oldX, oldY);
        }
    }

    /**
     * Sets the state of the beaver to the default state.
     */
    public void setDefaultState() {

         // Set default state for beaver.
        currentState = BeaverState.MOVING; 
    }
}

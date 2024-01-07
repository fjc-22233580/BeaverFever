import java.awt.Point;
import java.util.List;
import greenfoot.Greenfoot;
import greenfoot.World;

/**
 * The WorldManager class is responsible for instantiating all the maps in the world and manages the transition between them.
 * It implements the Singleton design pattern to ensure that only one instance of the WorldManager exists throughout the game.
 */
public class WorldManager { 

    // #region WorldManager Singleton Instance

    private boolean isInitialised = false;
    
    private static WorldManager instance;
    
    public static WorldManager getInstance(){
        if (instance == null) {
            instance = new WorldManager();
        }
        return instance;
    }
    
    // #endregion
    
    // #region Constants
    
    private final int ROWS = 3;
    private final int COLS = 3;
    private final int MAP_WIDTH = 240;
    private final int MAP_HEIGHT = 240;
    
    private final Point WORLD_CENTRE_LOCATION = new Point(MAP_WIDTH/2, MAP_HEIGHT/2);
    private final Point BEAVER_LOCATION = new Point(40, 40);
    
    // #endregion
    
    // Create our 2D array of worlds
    private World[][] worlds = new World[ROWS][COLS];
    
    // Create out management classes
    private MapParser mapParser;    
    private ObjectManager objectManager;
    private PlayerStats playerStats;
    private EnemyPathsManager enemyPathsManager;    
    
    private FinishScreen finishScreen;
    private GameMap startingMap;

    // Create our actors
    private PrincessOlive princess;
    private Beaver beaver;

    // Set the initial world to the top left
    private int currentRow = 0;
    private int currentCol = 0;   
    
    // Set the dev mode flag, will be set in the Button class,
    // and used to determine whether to add the non-terrain tiles
    // in dev mode we can create the points for enemy patrol paths.
    private boolean isDevMode = false; 

    /**
     * Initializes the WorldManager by creating and initializing the necessary objects and maps.
     * If the WorldManager has already been initialized, it resets the player stats.
     */
    public void initialize() {
        
        // Cache our worlds - first time only.
        if (isInitialised == false) {
            
            enemyPathsManager = new EnemyPathsManager();
            objectManager = new ObjectManager();
            playerStats = new PlayerStats();
            beaver = new Beaver(objectManager, playerStats);
            princess = new PrincessOlive();
            
            for (int i = 0; i < ROWS; i++) {
                for (int j = 0; j < COLS; j++) {                    
                    // Get the 1D index of the world, this will be become the worldId in each map.
                    int worldIndex_1d = i * COLS + j;                    
                    worlds[i][j] = new GameMap(MAP_WIDTH, MAP_HEIGHT, false, isDevMode, worldIndex_1d); 
                }
            }
            
            // Create our map parser, passing in our 2d array of worlds and prepare all the maps
            mapParser = new MapParser(worlds);
            mapParser.prepareAllMaps();   

            // Cache the starting map
            startingMap = (GameMap)worlds[0][0];
            
            if(isDevMode == false) { 
                addNonTerrainTiles();
            }
            
            isInitialised = true;
        } else {
            // Reset the player stats
            resetGame();
        }
    }
    public EnemyPathsManager getEnemyPathsManager() {
        return enemyPathsManager;
    }
    
    /**
     * Adds non-terrain tiles to the worlds.
     * For each tile in the worlds grid, it checks if it needs to add a beaver and a status bar for the first map.
     * For the rest of the maps, if the map has an enemy path, it adds an enemy.
     * It also adds a princess object to the second tile of the first row and a key object to the first tile of the third row.
     */
    private void addNonTerrainTiles(){
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                
                int mapId = i * COLS + j;
                
                // For our first map, add the beaver and the status bar
                if (i == 0 && j == 0) {                    
                    worlds[i][j].addObject(beaver, BEAVER_LOCATION.x, BEAVER_LOCATION.y);
                    playerStats.addStatusBarToWorld(worlds[i][j]);
                }
                
                // For the rest of our maps, if the map has an enemy path, add an enemy
                if (enemyPathsManager.getAllWorldIds().contains(mapId)) {
                    Enemy enemy = new Enemy(enemyPathsManager.getEnemyPath(mapId), WORLD_CENTRE_LOCATION);
                    worlds[i][j].addObject(enemy, WORLD_CENTRE_LOCATION.x, WORLD_CENTRE_LOCATION.y);
                }
            }
        }        
        
        Point princessPoint = new Point(112, 45);        
        worlds[0][1].addObject(princess, princessPoint.x, princessPoint.y);
        
        Point keyPoint = new Point(58,197);
        Key key = new Key();
        worlds[2][0].addObject(key, keyPoint.x, keyPoint.y);
    }
    
    public void beginGame(){ 
        Greenfoot.setWorld(startingMap);
    }   

  
    public void resetGame() {
        // Set world to top left.
        currentCol = 0;
        currentRow = 0;

        playerStats.resetStats();        
        beaver.setDefaultState();
        mapParser.prepareAllMaps();
        
        if (isDevMode == false) {
            addNonTerrainTiles();
        }       
        
        beginGame();
    }    

    /**
     * Stops the background music, creates a finish screen with a win message,
     * and sets the finish screen as the current world.
     */
    public void winGame() {

        finishScreen = new FinishScreen(true);
        Greenfoot.setWorld(finishScreen);
    }

    /**
     * Stops the music, creates a finish screen indicating that the game has been lost,
     * and sets the finish screen as the current world.
     */
    public void loseGame() {

        finishScreen = new FinishScreen(false);        
        Greenfoot.setWorld(finishScreen);
    }

    /**
     * Sets the key as collected by the player.
     * This method updates the state of the princess and removes fences from the world.
     */
    public void setKeyCollected() {
        // Update princess image
        princess.setKeyCollected();

        // Remove fences
        List<FenceTile> fences = worlds[0][1].getObjects(FenceTile.class);
        worlds[0][1].removeObjects(fences); 
    }
    
    /**
     * Sets the development mode of the WorldManager.
     * When in development mode, no enemies are added to the world and path creation is enabled.
     */
    public void setDevMode() {
        isDevMode = true;
    }

    /**
     * Changes the world based on the given direction and coordinates.
     * Removes the calling wombat from the current world, resets the enemy,
     * and adds the calling wombat to the new world at the specified spawn coordinates.
     * Updates the current row and column indices based on the direction.
     * Adds the player stats status bar to the new world.
     * Sets the new world as the active world.
     * 
     * @param direction the direction to change the world
     * @param x the x-coordinate of the calling wombat's spawn point
     * @param y the y-coordinate of the calling wombat's spawn point
     * @param callingWorld the current world
     * @param callingWombat the calling wombat object
     */
    public void changeWorld(Direction direction, int x, int y, World callingWorld, Beaver callingWombat){

        callingWorld.removeObject(callingWombat);
        resetEnemy(callingWorld);

        // Set the spawn coordinates based on the direction, and exit point.
        int spawnX = 0;
        int spawnY = 0;

        // Set the approriate world index based on the direction
        if (direction == Direction.Up && currentRow > 0) {
            currentRow--;
            spawnX = x;
            spawnY = MAP_HEIGHT;
        } else if (direction == Direction.Down && currentRow < ROWS - 1) {
            currentRow++;
            spawnX = x;
            spawnY = 0;
        } else if (direction == Direction.Left && currentCol > 0) {
            currentCol--;
            spawnX = MAP_WIDTH;;
            spawnY = y;

        } else if (direction == Direction.Right && currentCol < COLS - 1) {
            currentCol++;
            spawnX = 0;
            spawnY = y;
        }
        
        GameMap currentMap = (GameMap)worlds[currentRow][currentCol];        
        currentMap.addObject(callingWombat, spawnX, spawnY);
        playerStats.addStatusBarToWorld(currentMap);
        Greenfoot.setWorld(currentMap);
    }    

    /**
     * Resets the position of the enemy in the calling world.   
     * @param callingWorld the world from which to reset the enemy position
     */
    private void resetEnemy(World callingWorld) {

        List<Enemy> enemies = callingWorld.getObjects(Enemy.class);

        if (enemies.size() > 0) {
            Enemy enemy = enemies.get(0);
            enemy.resetEnemyPosition();            
        }        
    }
}

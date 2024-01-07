import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;
import greenfoot.World;

public class WorldManager { 

    // #region WorldManager Singleton Instance

    private static WorldManager instance;

    public static WorldManager getInstance(){
        if (instance == null) {
            instance = new WorldManager();
        }
        return instance;
    }
    
    // #endregion

    private ObjectManager objectManager;
    private PlayerStats playerStats;

    private final int ROWS = 3;
    private final int COLS = 3;

    private final int MAP_WIDTH = 240;
    private final int MAP_HEIGHT = 240;

    private Point initialLocation = new Point(MAP_WIDTH/2, MAP_HEIGHT/2);

    private Point beaverLocation = new Point(40, 40);

    // Set the initial position
    private int currentRow = 0;
    private int currentCol = 0;
    
    private World[][] worlds = new World[ROWS][COLS];
    
    private boolean isInitialised = false;
    
    private boolean isDevMode = false;
    
    private EnemyPathsManager enemyPathsManager;
    
    private PrincessOlive princess = new PrincessOlive();
    private Beaver beaver;
    private MapParser mapParser;


    public EnemyPathsManager getEnemyPathsManager() {
        return enemyPathsManager;
    }

    public void initialize() {

        // Cache our worlds - first time only.
        if (isInitialised == false) {

            enemyPathsManager = new EnemyPathsManager();
            objectManager = new ObjectManager();
            playerStats = new PlayerStats();
            beaver = new Beaver(objectManager, playerStats);
            
            for (int i = 0; i < ROWS; i++) {                
                
                for (int j = 0; j < COLS; j++) {
                    
                    int worldIndex_1d = i * COLS + j;
                    
                    // Initialise our maps - for the first one we have already instantiated it (to add the player),
                    // so add the correct ref, else create new ones. 
                    if (i == 0 && j == 0) {
                        worlds[i][j] = new GameMap(MAP_WIDTH, MAP_HEIGHT, false, isDevMode, 0);                                 
                    } else {
                        worlds[i][j] = new GameMap(MAP_WIDTH, MAP_HEIGHT, false, isDevMode, worldIndex_1d); 
                    }
                }
            }
            
            mapParser = new MapParser(worlds);
            mapParser.prepareAllMaps();   

            if(isDevMode == false) { 
                addNonTerrainTiles();
            }
            
            isInitialised = true;
        } else {
            // Reset the player stats
            resetGame();
        }
    }

    private void addNonTerrainTiles(){
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {

                int worldIndex_1d = i * COLS + j;
                // Initialise our maps - for the first one we have already instantiated it (to
                // add the player),
                // so add the correct ref, else create new ones.
                if (i == 0 && j == 0) {

                    worlds[i][j].addObject(beaver, beaverLocation.x, beaverLocation.y);
                    playerStats.addStatusBarToWorld(worlds[i][j]);
                }

                if (enemyPathsManager.getAllWorldIds().contains(worldIndex_1d)) {
                    Enemy enemy = new Enemy(enemyPathsManager.getEnemyPath(worldIndex_1d), initialLocation);
                    worlds[i][j].addObject(enemy, initialLocation.x, initialLocation.y);
                }
            }
        }


        Point princessPoint = new Point(112, 45);        
        worlds[0][1].addObject(princess, princessPoint.x, princessPoint.y);

        Point keyPoint = new Point(58,197);
        Key key = new Key();
        worlds[2][0].addObject(key, keyPoint.x, keyPoint.y);
    }

    public void setKeyCollected() {
        princess.setKeyCollected();
        removeFences();
    }

    private void removeFences() {
        List<FenceTile> fences = worlds[0][1].getObjects(FenceTile.class);
        worlds[0][1].removeObjects(fences);        
    }

    public void setDevMode() {
        isDevMode = true;
    }

    public void beginGame(){        
        Greenfoot.setWorld(worlds[0][0]);
    }     

    World currentMap;

    public void changeWorld(Direction direction, int x, int y, World callingWorld, Beaver callingWombat){

        callingWorld.removeObject(callingWombat);

        resetEnemy(callingWorld);

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
        
        currentMap = worlds[currentRow][currentCol];        
        currentMap.addObject(callingWombat, spawnX, spawnY);

        playerStats.addStatusBarToWorld(currentMap);

        Greenfoot.setWorld(currentMap);
    }

    public void winGame() {

        FinishScreen finishScreen = new FinishScreen(true);
        Greenfoot.setWorld(finishScreen);
    }

    public void resetGame() {
        currentCol = 0;
        currentRow = 0;

        playerStats.resetStats();

        mapParser.prepareAllMaps();

        if (isDevMode == false) {
            addNonTerrainTiles();
        }

        beginGame();
    }

    public void loseGame() {

        FinishScreen finishScreen = new FinishScreen(false);        
        Greenfoot.setWorld(finishScreen);
    }

    private void resetEnemy(World callingWorld) {

        List<Enemy> enemies = callingWorld.getObjects(Enemy.class);

        if (enemies.size() > 0) {
            Enemy enemy = enemies.get(0);
            enemy.resetEnemyPosition();            
        }        
    }
}

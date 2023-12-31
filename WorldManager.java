import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import greenfoot.Color;
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

    // Set the initial position
    private int currentRow = 0;
    private int currentCol = 0;

    private int maxX;
    private int maxY;

    private World[][] worlds = new World[ROWS][COLS];

    private boolean isInitialised = false;

    private boolean isDevMode = false;

    private EnemyPathsManager enemyPathsManager;


    public EnemyPathsManager getEnemyPathsManager() {
        return enemyPathsManager;
    }

    public void initialize() {

        // Cache our worlds - first time only.
        if (isInitialised == false) {

            enemyPathsManager = new EnemyPathsManager();
            
            
            // Test images for world identification
            List<GreenfootImage> mapNumbers = new ArrayList<>();
            mapNumbers.add(new GreenfootImage("1.png"));
            mapNumbers.add(new GreenfootImage("2.png"));
            mapNumbers.add(new GreenfootImage("3.png"));
            mapNumbers.add(new GreenfootImage("4.png"));
            mapNumbers.add(new GreenfootImage("5.png"));
            mapNumbers.add(new GreenfootImage("6.png"));
            mapNumbers.add(new GreenfootImage("7.png"));
            mapNumbers.add(new GreenfootImage("8.png"));
            mapNumbers.add(new GreenfootImage("9.png"));

            objectManager = new ObjectManager();
            playerStats = new PlayerStats();

            Beaver beaver = new Beaver(objectManager, playerStats);
            
            GameMap firstWorld = new GameMap(MAP_WIDTH, MAP_HEIGHT, false, mapNumbers.get(0), isDevMode, 0);

            
            maxX = firstWorld.getWidth();
            maxY = firstWorld.getHeight();
            
            for (int i = 0; i < ROWS; i++) {                
                
                for (int j = 0; j < COLS; j++) {
                    
                    int worldIndex_1d = i * COLS + j;
                    
                    // Initialise our maps - for the first one we have already instantiated it (to add the player),
                    // so add the correct ref, else create new ones. 
                    if (i == 0 && j == 0) {
                        worlds[i][j] = firstWorld;
                    } else {
                        GreenfootImage  img = mapNumbers.get(worldIndex_1d);
                        worlds[i][j] = new GameMap(MAP_WIDTH, MAP_HEIGHT, false, img, isDevMode, worldIndex_1d); 
                    }
                    
                    //System.out.print("(" + i + "," + j + " + " + imgIndex +")");                    
                    //System.out.println();
                }
                
                //System.out.println();
            }
            
            MapParser mapParser = new MapParser(worlds);
            mapParser.prepareAllMaps();

            firstWorld.addObject(beaver, 120, 120);
            firstWorld.addObject(playerStats.getStatusBar(), 40, 10);
            firstWorld.addObject(playerStats.getStatusBarLabel(), 25, 10);            

            if(isDevMode == false) {
                addEnemies();
            }
            
            isInitialised = true;
        }
    }

    private void addEnemies() {

        int x = enemyPathsManager.getEnemyPath(7).get(0).x;
        int y = enemyPathsManager.getEnemyPath(7).get(0).y;

        Point defaultLocation = new Point(x, y);

        Enemy enemy = new Enemy(enemyPathsManager.getEnemyPath(7), defaultLocation);
        worlds[2][1].addObject(enemy, x, y-20);
    }

    public void setDevMode() {
        isDevMode = true;
    }

    public void beginGame(){        
        Greenfoot.setWorld(worlds[0][0]);
    }     

    public void changeWorld(Direction direction, int x, int y, World callingWorld, Beaver callingWombat){

        callingWorld.removeObject(callingWombat);
        callingWorld.removeObject(playerStats.getStatusBar());
        callingWorld.removeObject(playerStats.getStatusBarLabel());

        resetEnemy(callingWorld);

        int spawnX = 0;
        int spawnY = 0;

        // Set the approriate world index based on the direction
        if (direction == Direction.Up && currentRow > 0) {
            currentRow--;
            spawnX = x;
            spawnY = maxY;
        } else if (direction == Direction.Down && currentRow < ROWS - 1) {
            currentRow++;
            spawnX = x;
            spawnY = 0;
        } else if (direction == Direction.Left && currentCol > 0) {
            currentCol--;
            spawnX = maxX;
            spawnY = y;

        } else if (direction == Direction.Right && currentCol < COLS - 1) {
            currentCol++;
            spawnX = 0;
            spawnY = y;
        }
        
        World destination = worlds[currentRow][currentCol];        
        destination.addObject(callingWombat, spawnX, spawnY);
        destination.addObject(playerStats.getStatusBar(), 40, 10);
        destination.addObject(playerStats.getStatusBarLabel(), 25, 10);

        Greenfoot.setWorld(destination);
    }

    private void resetEnemy(World callingWorld) {

        List<Enemy> enemies = callingWorld.getObjects(Enemy.class);

        if (enemies.size() > 0) {
            Enemy enemy = enemies.get(0);
            enemy.reset();            
        }

        
    }
}

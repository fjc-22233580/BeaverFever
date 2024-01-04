import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;
import greenfoot.World;

public class WorldManager { 

    // #region TODO List:

        // Code:

        // 1. Complete - Add counter for health - currently only wood is reported.
        // 2. WIP - Add bridge building mechanism - suggest another key binding that only works if near the river, in one spot, then each press,
        //    builds a portion of the bridge with a small time - can be based off the game cycle iteration.
        // 3. Complete - Key collection: Add key collection, once past 1st river, hold this as a boolean in the player stats
        // 4. WIP - Add "Finish" and "Game over" screens to the WorldManager class, and methods to get to those screens.
        // 5. Add logic for reaching the princess, and goes to "Finish" screen.
        // 6. Needs Testing - Add logic for running out of lives, and goes to "Game Over" screen.
        // 7. Add comments to everything
        // 8. Review all code, any duplication?
        // 9. Complete - Re-factor getNextPoint method
        // 10. Add "Dev Mode" button to title screen, which sets the isDevMode flag in the WorldManager class.
        // 11. BUG - Increase rad of tree collection - large tree is partially missed.
        // 12. BUG - Add logic to enemy so after initial attack, it does a loop of the map, and then attacks again - perhaps instead of delay timer?
        // 13. Pass the construction point to necessary worlds
        // 14. COMPLETE - Made factory for tiles and introced base class


        // ArtWork:

        // (Note: all artwork must either be .png or .gif, have a transparent background, and be reasonably sized to work with the game )
        // 1. Create beaver chopping gif - an axe already exists in the asset pack, suggest combining that with current beaver image to create a gif.
        // 2. Create the enemy attacking gif. 
        // 3. Create Title screen artwork  = Game name and key controls. Done
        // 4. Create "Game over, you win" screen artwork. Done
        // 5. Create "Game Overm you lose" screen artwork. Done
        // 6. Create "Bridge" artwork - needs to be same size as current game tiles. Done but size 15/13
        // 7. Create "Key" artwork - needs to be same size as current game tiles. done
        // 8. Create button art work: "Play", "Dev Mode", "Restart"  - needs to be approx size that a button might be, like the thing we have now. Done "start", "restart", "options"
        // 9. OPTIONAL: Are there any fence tiles in the asset pack? If so, add them to rivers, and then with a gap,
        //    and a different tile for the "construction" point - more obvious of where player needs to stand to build the bridge.

        // Map Design: Done latest update as off 22:34 on 3rd

        // 1. Tweak river path in map 7 to have a more horizontal portion - this will help coding the bridge building logic.
        // 2. Move trees in all maps so they are closer to the sides - this will help with the enemy patrol path logic.
        // 3. BUG - I think map 4 has a berry tile which has been incorrectly assigned as a tree tile - double check IDs in .csv files

    // #endregion

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
            
            boolean hasBridge = false;
            GameMap firstWorld = new GameMap(MAP_WIDTH, MAP_HEIGHT, false, mapNumbers.get(0), isDevMode, 0, hasBridge);

            
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
                        worlds[i][j] = new GameMap(MAP_WIDTH, MAP_HEIGHT, false, img, isDevMode, worldIndex_1d, hasBridge); 
                    }
                    
                    //System.out.print("(" + i + "," + j + " + " + imgIndex +")");                    
                    //System.out.println();
                }
                
                //System.out.println();
            }
            
            MapParser mapParser = new MapParser(worlds);
            mapParser.prepareAllMaps();

            firstWorld.addObject(beaver, 120, 120);
            
            playerStats.addStatusBarToWorld(firstWorld);           

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

        playerStats.addStatusBarToWorld(destination);

        Greenfoot.setWorld(destination);
    }

    public void winGame() {

        // TODO - Add princess and beaver to this world.
        FinishScreen finishScreen = new FinishScreen(true);
        Greenfoot.setWorld(finishScreen);
    }

    public void loseGame() {

        FinishScreen finishScreen = new FinishScreen(false);        
        Greenfoot.setWorld(finishScreen);
    }

    private void resetEnemy(World callingWorld) {

        List<Enemy> enemies = callingWorld.getObjects(Enemy.class);

        if (enemies.size() > 0) {
            Enemy enemy = enemies.get(0);
            enemy.reset();            
        }        
    }
}

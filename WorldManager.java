import greenfoot.Greenfoot;
import greenfoot.World;

public class WorldManager {    

    private static WorldManager instance;

    private final int ROWS = 3;
    private final int COLS = 3;

    // Set the initial position
    private int currentRow = 0;
    private int currentCol = 0;

    private int maxX;
    private int maxY;

    private World[][] worlds = new World[ROWS][COLS];

    private boolean isInitialised;

    public static WorldManager getInstance(){
        if (instance == null) {
            instance = new WorldManager();
        }
        return instance;
    }

    public void initialize() {

        // Cache our worlds - first time only.
        if (isInitialised == false) {

            GameMap firstWorld = new GameMap();
            firstWorld.addObject(new Beaver(), 120, 120);
            maxX = firstWorld.getWidth();
            maxY = firstWorld.getHeight();
            
            worlds[0][0] = firstWorld;
            worlds[0][1] = new GameMap();
            worlds[1][0] = new GameMap();
            worlds[1][1] = new GameMap();
            
            isInitialised = true;
        }
    }

    public void beginGame(){        
        Greenfoot.setWorld(worlds[0][0]);
    }

     

    public void changeWorld(Direction direction, int x, int y, World callingWorld, Beaver callingWombat){

        callingWorld.removeObject(callingWombat);

        int spawnX = 0;
        int spawnY = 0;

        //[1][2]
        //[3][4]

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
        Greenfoot.setWorld(destination);
        
    }


}
